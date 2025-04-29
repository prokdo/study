package ui

import (
	"context"
	"fmt"
	"sync/atomic"
	"time"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/data/binding"
	"fyne.io/fyne/v2/widget"

	"graphmis/app/utils"
	"graphmis/graph"
)

func NewCalculationPage(state *AppState) (fyne.CanvasObject, func()) {
	logText := binding.NewString()
	statusText := binding.NewString()
	statusText.Set("Статус: готово к запуску")
	progressVal := binding.NewFloat()

	statusLabel := widget.NewLabelWithData(statusText)
	statusLabel.Alignment = fyne.TextAlignCenter
	progressBar := widget.NewProgressBarWithData(progressVal)
	logEntry := widget.NewMultiLineEntry()
	logEntry.Bind(logText)
	logScroll := container.NewVScroll(logEntry)
	logScroll.SetMinSize(fyne.NewSize(800, 800))

	var cancel context.CancelFunc
	var isRunning atomic.Bool
	var wasCancelled atomic.Bool

	appendLog := func(msg string) {
		old, _ := logText.Get()
		_ = logText.Set(old + msg + "\n")
	}

	clearLog := func() {
		_ = logText.Set("")
	}

	runBtn := widget.NewButton("Запуск", nil)
	cancelBtn := widget.NewButton("Прервать", nil)

	resetUI := func() {
		if !wasCancelled.Load() {
			state.NavigationState.NextButton.Enable()
		} else {
			state.NavigationState.NextButton.Disable()
		}
		state.NavigationState.BackButton.Enable()
		isRunning.Store(false)
		runBtn.Enable()
		cancelBtn.Disable()
	}

	cancelBtn.OnTapped = func() {
		if cancel != nil {
			cancel()
			cancel = nil
		}
		wasCancelled.Store(true)
		appendLog("Прервано пользователем")
		_ = statusText.Set("Статус: прервано")
		state.Results = nil
		resetUI()
	}
	cancelBtn.Disable()

	runBtn.OnTapped = func() {
		if isRunning.Load() {
			return
		}
		state.NavigationState.BackButton.Disable()
		clearLog()
		isRunning.Store(true)
		wasCancelled.Store(false)
		runBtn.Disable()
		cancelBtn.Enable()

		state.Results = nil
		_ = statusText.Set("Статус: запуск")
		_ = progressVal.Set(0.0)

		ctx, c := context.WithCancel(context.Background())
		cancel = c

		go func() {
			total := float64(state.RunConfig.RunsNumber * 2)
			count := 0.0

			for i := range state.RunConfig.RunsNumber {
				if ctx.Err() != nil {
					return
				}

				var g graph.Graph[string]
				if state.RunConfig.IsGraphFixed {
					g = state.Graph
				} else {
					g = utils.GenerateGraph(
						state.GeneratorConfig.GraphType,
						state.GeneratorConfig.MinVerticesNumber,
						state.GeneratorConfig.MaxVerticesNumber,
						state.GeneratorConfig.GraphDensity,
					)
				}

				appendLog(fmt.Sprintf("--- Итерация №%d ---", i+1))
				_ = statusText.Set(fmt.Sprintf("Статус: активно (итерация %d из %d)", i+1, state.RunConfig.RunsNumber))

				var exactSolution []string

				for j, mc := range state.MethodConfigs {
					if ctx.Err() != nil {
						return
					}

					methodName := mc.MethodType()

					var solution []string
					start := time.Now()
					switch cfg := mc.(type) {
					case *MaghoutConfig:
						exactSolution = graph.MISMaghout(ctx, g, cfg.ParallelDepth)
						solution = exactSolution
					case *GreedySearchConfig:
						solution = graph.MISGreedySearch(ctx, g, cfg.Iterations)
					}
					elapsed := time.Since(start).Microseconds()

					if ctx.Err() != nil {
						return
					}

					var f1Factor float64
					switch mc.(type) {
					case *MaghoutConfig:
						f1Factor = 1.00
					case *GreedySearchConfig:
						f1Factor = graph.ComputeF1Factor(exactSolution, solution)
					}
					state.Results = append(state.Results, &Result{
						RunId:    i,
						Method:   string(methodName),
						Time:     elapsed,
						Result:   solution,
						F1Factor: f1Factor,
					})

					appendLog(fmt.Sprintf("[%s] время выполнения: %d мкс", methodName, elapsed))
					if j+1 == len(state.MethodConfigs) {
						appendLog("")
					}

					count++
					_ = progressVal.Set(count / total)
				}
			}

			if !wasCancelled.Load() {
				_ = statusText.Set("Статус: завершено")
			}

			resetUI()
		}()
	}

	controlCol := container.NewVBox(
		container.NewPadded(runBtn),
		container.NewPadded(cancelBtn),
		container.NewPadded(statusLabel),
	)
	controlCenter := container.NewCenter(controlCol)

	right := container.NewBorder(nil, progressBar, nil, nil, logScroll)
	split := container.NewHSplit(controlCenter, right)
	split.Offset = 0.3

	return split, func() {
		if len(state.Results) == 0 {
			state.NavigationState.NextButton.Disable()
		} else {
			state.NavigationState.NextButton.Enable()
		}
	}
}

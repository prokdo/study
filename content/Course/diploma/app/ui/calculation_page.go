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
	progressVal := binding.NewFloat()

	progressBar := widget.NewProgressBarWithData(progressVal)

	logEntry := widget.NewMultiLineEntry()
	logEntry.Bind(logText)
	logScroll := container.NewVScroll(logEntry)
	logScroll.SetMinSize(fyne.NewSize(800, 800))

	var cancel context.CancelFunc
	var isRunning atomic.Bool
	var wasCancelled atomic.Bool

	appendLog := func(msg string) {
		now := time.Now().Format("15:04:05")
		colored := fmt.Sprintf("[%s] %s", now, msg)
		old, _ := logText.Get()
		_ = logText.Set(old + colored + "\n")
	}

	clearLog := func() {
		_ = logText.Set("")
	}

	runBtn := widget.NewButton("Запуск", nil)
	cancelBtn := widget.NewButton("Прервать", nil)
	cancelBtn.Disable()

	resetUI := func() {
		isRunning.Store(false)
		wasCancelled.Store(false)

		runBtn.Enable()
		cancelBtn.Disable()

		if !wasCancelled.Load() && len(state.Results) > 0 {
			state.NavigationState.NextButton.Enable()
		} else {
			state.NavigationState.NextButton.Disable()
		}
		state.NavigationState.BackButton.Enable()
	}

	progressVal.AddListener(binding.NewDataListener(func() {
		val, _ := progressVal.Get()
		if val >= 1.0 {
			resetUI()
		}
	}))

	cancelBtn.OnTapped = func() {
		if cancel != nil {
			cancel()
			cancel = nil
		}
		wasCancelled.Store(true)
		state.Results = nil

		_ = progressVal.Set(1.0)
	}

	runBtn.OnTapped = func() {
		if isRunning.Load() {
			return
		}
		isRunning.Store(true)
		wasCancelled.Store(false)

		clearLog()
		runBtn.Disable()
		cancelBtn.Enable()
		state.NavigationState.BackButton.Disable()
		state.NavigationState.NextButton.Disable()
		state.Results = nil

		_ = progressVal.Set(0.0)

		ctx, c := context.WithCancel(context.Background())
		cancel = c

		go func() {
			defer func() {
				_ = progressVal.Set(1.0)
			}()

			totalSteps := float64(state.RunConfig.RunsNumber * 2)
			var currentStep float64

		loop:
			for i := range state.RunConfig.RunsNumber {
				if ctx.Err() != nil {
					break loop
				}

				var g graph.Graph[string]
				if state.RunConfig.IsGraphFixed {
					g = state.Graph
				} else {
					if state.GeneratorConfig == nil {
						appendLog("❌ Невозможно сгенерировать граф: конфигурация отсутствует")
						continue
					}
					g = utils.GenerateGraph(
						state.GeneratorConfig.GraphType,
						state.GeneratorConfig.MinVerticesNumber,
						state.GeneratorConfig.MaxVerticesNumber,
						state.GeneratorConfig.GraphDensity,
					)
					state.Graph = g
				}

				appendLog(fmt.Sprintf("🔄 Итерация #%d", i+1))

				var exactSolution []string

				for _, mc := range state.MethodConfigs {
					if ctx.Err() != nil {
						break loop
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

					var f1 float64
					if _, ok := mc.(*GreedySearchConfig); ok {
						f1 = graph.ComputeF1Factor(exactSolution, solution)
					} else {
						f1 = 1.0
					}

					state.Results = append(state.Results, &Result{
						RunId:    i + 1,
						Method:   string(methodName),
						Time:     elapsed,
						Result:   solution,
						F1Factor: f1,
					})

					appendLog(fmt.Sprintf("[%s] Время: %d мкс | F1: %.2f", methodName, elapsed, f1))

					currentStep++
					_ = progressVal.Set(currentStep / totalSteps)
				}

				appendLog("")
			}

			if ctx.Err() == nil {
				appendLog("✅ Расчёты завершены")
			} else {
				appendLog("\n❌ Прервано пользователем")
				state.Results = nil
			}
		}()
	}

	controls := container.NewVBox(
		runBtn,
		cancelBtn,
	)
	left := container.NewCenter(controls)

	statusProgress := container.NewBorder(nil, nil, nil, nil,
		container.NewStack(progressBar),
	)

	mainContainer := container.NewBorder(nil, statusProgress, nil, nil,
		logScroll,
	)

	split := container.NewHSplit(left, mainContainer)
	split.Offset = 0.2

	return split, func() {
		if len(state.Results) == 0 {
			state.NavigationState.NextButton.Disable()
		} else {
			state.NavigationState.NextButton.Enable()
		}
	}

}
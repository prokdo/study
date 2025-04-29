package ui

import (
	"bytes"
	"fmt"
	"image"
	"image/color"
	"image/png"
	"os"
	"time"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/canvas"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/dialog"
	"fyne.io/fyne/v2/widget"
	"gonum.org/v1/plot"
	"gonum.org/v1/plot/plotter"
	"gonum.org/v1/plot/vg"
)

func NewChartsPage(state *AppState) (fyne.CanvasObject, func()) {
	imageWidgets := []*canvas.Image{}
	updateFuncs := []func(){}

	saveImage := func(path string, img image.Image) error {
		file, err := os.Create(path)
		if err != nil {
			return err
		}
		defer file.Close()
		return png.Encode(file, img)
	}

	buildPlot := func(title, yLabel string, exactData, approxData plotter.XYs) image.Image {
		p := plot.New()
		p.Title.Text = title
		p.X.Label.Text = "ID запуска"
		p.Y.Label.Text = yLabel
		p.BackgroundColor = color.White

		exactLine, err := plotter.NewLine(exactData)
		if err != nil {
			panic(err)
		}
		exactLine.Color = color.RGBA{R: 0, G: 0, B: 255, A: 255}
		exactLine.Width = vg.Points(2)

		approxLine, err := plotter.NewLine(approxData)
		if err != nil {
			panic(err)
		}
		approxLine.Color = color.RGBA{R: 255, G: 0, B: 0, A: 255}
		approxLine.Width = vg.Points(2)

		p.Add(exactLine, approxLine)
		p.Legend.Add("Метод Магу", exactLine)
		p.Legend.Add("Жадный поиск", approxLine)

		buf := new(bytes.Buffer)
		writerTo, err := p.WriterTo(8*vg.Inch, 6*vg.Inch, "png")
		if err != nil {
			panic(err)
		}

		if _, err := writerTo.WriteTo(buf); err != nil {
			panic(err)
		}

		img, err := png.Decode(buf)
		if err != nil {
			panic(err)
		}

		return img
	}

	buildChartTab := func(title, yLabel string, getData func() (plotter.XYs, plotter.XYs), saveName string) fyne.CanvasObject {
		img := canvas.NewImageFromResource(nil)
		img.FillMode = canvas.ImageFillContain
		imageWidgets = append(imageWidgets, img)

		dateTime := "2006-01-02T15:04:05"
		saveBtn := widget.NewButton("Сохранить график", func() {
			dialog.ShowFolderOpen(func(uri fyne.ListableURI, err error) {
				if err != nil {
					dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
					return
				}
				if uri == nil {
					return
				}
				path := fmt.Sprintf("%s/%s_%s.png", uri.Path(), time.Now().Format(dateTime), saveName)
				if err := saveImage(path, img.Image); err != nil {
					dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
				}
			}, fyne.CurrentApp().Driver().AllWindows()[0])
		})

		updateFuncs = append(updateFuncs, func() {
			exactData, approxData := getData()
			newImg := buildPlot(title, yLabel, exactData, approxData)
			img.Image = newImg
			img.Refresh()
		})

		return container.NewBorder(nil, saveBtn, nil, nil, img)
	}

	getTimeData := func() (plotter.XYs, plotter.XYs) {
		var exact, approx plotter.XYs
		for _, res := range state.Results {
			x := float64(res.RunId)
			y := float64(res.Time)
			if res.Method == string(MaghoutMethod) {
				exact = append(exact, plotter.XY{X: x, Y: y})
			} else if res.Method == string(GreedySearchMethod) {
				approx = append(approx, plotter.XY{X: x, Y: y})
			}
		}
		return exact, approx
	}

	getF1Data := func() (plotter.XYs, plotter.XYs) {
		var exact, approx plotter.XYs
		for _, res := range state.Results {
			x := float64(res.RunId)
			y := res.F1Factor
			if res.Method == string(MaghoutMethod) {
				exact = append(exact, plotter.XY{X: x, Y: y})
			} else if res.Method == string(GreedySearchMethod) {
				approx = append(approx, plotter.XY{X: x, Y: y})
			}
		}
		return exact, approx
	}

	getCardinalityData := func() (plotter.XYs, plotter.XYs) {
		var exact, approx plotter.XYs
		for _, res := range state.Results {
			x := float64(res.RunId)
			y := float64(len(res.Result))
			if res.Method == string(MaghoutMethod) {
				exact = append(exact, plotter.XY{X: x, Y: y})
			} else if res.Method == string(GreedySearchMethod) {
				approx = append(approx, plotter.XY{X: x, Y: y})
			}
		}
		return exact, approx
	}

	dateTime := time.Now().Format("2006-01-02T15-04-05")
	tabs := container.NewAppTabs(
		container.NewTabItem("Время выполнения", buildChartTab("Время выполнения", "мс", getTimeData, fmt.Sprintf("%s_time", dateTime))),
		container.NewTabItem("Точность (F1)", buildChartTab("Точность (F1)", "F1-фактор", getF1Data, fmt.Sprintf("%s_f1", dateTime))),
		container.NewTabItem("Мощность решений", buildChartTab("Мощность решений", "Размер множества", getCardinalityData, fmt.Sprintf("%s_cardinality", dateTime))),
	)

	initFunc := func() {
		for _, f := range updateFuncs {
			f()
		}
		state.NavigationState.NextButton.Disable()
		state.NavigationState.BackButton.Enable()
	}

	return tabs, initFunc
}

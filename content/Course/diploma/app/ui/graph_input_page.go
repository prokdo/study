package ui

import (
	"fmt"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/canvas"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/dialog"
	"fyne.io/fyne/v2/layout"
	"fyne.io/fyne/v2/widget"

	"graphmis/app/utils"
	"graphmis/graph"
)

func NewGraphInputPage(state *AppState) (fyne.CanvasObject, func()) {
	initFunc := func() {
		state.NavigationState.BackButton.Disable()
		if state.Graph != nil {
			state.NavigationState.NextButton.Enable()
		} else {
			state.NavigationState.NextButton.Disable()
		}
	}

	title := canvas.NewText("Ввод графа", nil)
	title.TextStyle = fyne.TextStyle{Bold: true}
	title.Alignment = fyne.TextAlignCenter

	var preview = NewZoomableImage(nil)

	updatePreview := func(g graph.Graph[string]) {
		img, err := utils.RenderDotToImage(g)
		if err != nil {
			dialog.ShowError(fmt.Errorf("Ошибка визуализации: %w", err), fyne.CurrentApp().Driver().AllWindows()[0])
			return
		}
		preview.SetImage(img)
		state.NavigationState.NextButton.Enable()
	}

	// Интерактивные элементы для генерации графа и других настроек
	minVerts := widget.NewEntry()
	minVerts.SetPlaceHolder("Минимум вершин")

	maxVerts := widget.NewEntry()
	maxVerts.SetPlaceHolder("Максимум вершин")

	density := widget.NewEntry()
	density.SetPlaceHolder("Плотность графа [0.0 ... 1.0]")

	graphTypeSelector := widget.NewSelect([]string{"Неориентированный", "Ориентированный"}, nil)
	graphTypeSelector.SetSelected("Неориентированный")

	genButton := widget.NewButton("Генерация", func() {
		minV, err1 := utils.ParseUint(minVerts.Text)
		maxV, err2 := utils.ParseUint(maxVerts.Text)
		p, err3 := utils.ParseFloatCoefficient(density.Text)

		if err := utils.FindFirstError(err1, err2, err3); err != nil {
			dialog.ShowError(fmt.Errorf("Неверные параметры генерации: %w", err), fyne.CurrentApp().Driver().AllWindows()[0])
			return
		}

		if maxV < minV {
			dialog.ShowError(fmt.Errorf("Минимум вершин должен быть не больше максимума"), fyne.CurrentApp().Driver().AllWindows()[0])
			return
		}

		var gt graph.GraphType
		if graphTypeSelector.Selected == "Ориентированный" {
			gt = graph.Directed
		} else {
			gt = graph.Undirected
		}

		g := utils.GenerateGraph(gt, minV, maxV, p)
		if g == nil {
			dialog.ShowError(fmt.Errorf("Ошибка генерации графа"), fyne.CurrentApp().Driver().AllWindows()[0])
			return
		}

		state.Graph = g
		state.RunConfig = nil
		state.GeneratorConfig = &GeneratorConfig{
			MinVerticesNumber: minV,
			MaxVerticesNumber: maxV,
			GraphDensity:      p,
			GraphType:         gt,
		}
		updatePreview(g)
	})

	// Кнопка импорта графа
	importButton := widget.NewButton("Выбор .dot файла", func() {
		dialog.ShowFileOpen(func(reader fyne.URIReadCloser, err error) {
			if err != nil || reader == nil {
				return
			}
			defer reader.Close()

			g, err := graph.LoadFromDot(reader.URI().Path())
			if err != nil {
				dialog.ShowError(fmt.Errorf("Ошибка загрузки графа: %w", err), fyne.CurrentApp().Driver().AllWindows()[0])
				return
			}

			state.Graph = g
			state.GeneratorConfig = nil
			state.RunConfig = &RunConfig{IsGraphFixed: true}
			state.NavigationState.NextButton.Enable()
			updatePreview(g)
		}, fyne.CurrentApp().Driver().AllWindows()[0])
	})

	// Создание контента для левой панели
	label1 := widget.NewLabel("Загрузка графа из файла")
	label1.Alignment = fyne.TextAlignCenter
	label2 := widget.NewLabel("Случайная генерация графа")
	label2.Alignment = fyne.TextAlignCenter

	leftContent := container.NewVBox(
		container.NewPadded(title),
		container.NewPadded(label1),
		container.NewPadded(importButton),
		container.NewPadded(label2),
		container.NewPadded(minVerts),
		container.NewPadded(maxVerts),
		container.NewPadded(density),
		container.NewPadded(graphTypeSelector),
		container.NewPadded(genButton),
	)

	// Левый контейнер
	leftSide := container.NewVBox(
		layout.NewSpacer(),
		leftContent,
		layout.NewSpacer(),
	)

	// Контейнер для превью с картинкой
	previewContainer := container.NewPadded(preview)
	split := container.NewHSplit(leftSide, previewContainer)
	split.Offset = 0.3

	return container.NewBorder(nil, container.NewHBox(layout.NewSpacer()), nil, nil, split), initFunc
}

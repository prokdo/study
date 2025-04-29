package app

import (
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/layout"
	"fyne.io/fyne/v2/widget"

	"graphmis/app/ui"
)

func NewApp() fyne.Window {
	a := app.NewWithID("ru.prokdo.diploma")

	w := a.NewWindow("Поиск МВУМ")
	w.Resize(fyne.NewSize(1000, 700))

	state := &ui.AppState{
		GeneratorConfig: &ui.GeneratorConfig{},
		MethodConfigs:   make([]ui.MethodConfig, 0, 2),
		RunConfig:       &ui.RunConfig{},
		Results:         make([]*ui.Result, 0),
		NavigationState: &ui.NavigationState{},
	}

	var currentIndex int
	var pages *fyne.Container
	pageInitFuncs := make([]func(), 0)

	showPage := func(idx int) {
		if idx < 0 || idx >= len(pages.Objects) || idx == currentIndex {
			return
		}
		pages.Objects[currentIndex].Hide()
		pageInitFuncs[idx]()
		pages.Objects[idx].Show()
		currentIndex = idx
	}

	showNext := func() {
		if currentIndex+1 < len(pages.Objects) {
			showPage(currentIndex + 1)
		}
	}
	showPrev := func() {
		if currentIndex-1 >= 0 {
			showPage(currentIndex - 1)
		}
	}

	nextBtn := widget.NewButton("Далее", showNext)
	backBtn := widget.NewButton("Назад", showPrev)
	themeBtn := widget.NewButton("Тема", func() {
		ui.ToggleTheme(a)
	})

	state.NavigationState.NextButton = nextBtn
	state.NavigationState.BackButton = backBtn
	state.NavigationState.BackButton.Disable()
	state.NavigationState.NextButton.Disable()

	pageObjs := make([]fyne.CanvasObject, 0)

	var page fyne.CanvasObject
	var initFunc func()

	page, initFunc = ui.NewGraphInputPage(state)
	pageObjs = append(pageObjs, page)
	pageInitFuncs = append(pageInitFuncs, initFunc)

	page, initFunc = ui.NewMethodConfigPage(state)
	pageObjs = append(pageObjs, page)
	pageInitFuncs = append(pageInitFuncs, initFunc)

	page, initFunc = ui.NewRunConfigPage(state)
	pageObjs = append(pageObjs, page)
	pageInitFuncs = append(pageInitFuncs, initFunc)

	page, initFunc = ui.NewCalculationPage(state)
	pageObjs = append(pageObjs, page)
	pageInitFuncs = append(pageInitFuncs, initFunc)

	page, initFunc = ui.NewResultsPage(state)
	pageObjs = append(pageObjs, page)
	pageInitFuncs = append(pageInitFuncs, initFunc)

	page, initFunc = ui.NewChartsPage(state)
	pageObjs = append(pageObjs, page)
	pageInitFuncs = append(pageInitFuncs, initFunc)

	pages = container.NewStack(pageObjs...)
	for i := range pages.Objects {
		if i != 0 {
			pages.Objects[i].Hide()
		}
	}
	currentIndex = 0

	footer := container.NewHBox(
		container.NewPadded(backBtn),
		container.NewPadded(nextBtn),
		layout.NewSpacer(),
		container.NewPadded(themeBtn),
	)

	w.SetContent(container.NewBorder(nil, footer, nil, nil, pages))
	return w
}

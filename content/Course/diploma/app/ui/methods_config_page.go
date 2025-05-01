package ui

import (
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/canvas"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/dialog"
	"fyne.io/fyne/v2/layout"
	"fyne.io/fyne/v2/theme"
	"fyne.io/fyne/v2/widget"

	"graphmis/app/utils"
)

func NewMethodConfigPage(state *AppState) (fyne.CanvasObject, func()) {
	initFunc := func() {
		state.NavigationState.BackButton.Enable()

		if len(state.MethodConfigs) == 0 {
			state.NavigationState.NextButton.Disable()
		} else {
			state.NavigationState.NextButton.Enable()
		}
	}

	title := canvas.NewText("Настройка параметров методов", nil)
	title.TextStyle = fyne.TextStyle{Bold: true}
	title.Alignment = fyne.TextAlignCenter

	maghoutLabel := widget.NewLabel("Метод Магу")
	maghoutLabel.Alignment = fyne.TextAlignCenter
	maghoutLabel.TextStyle = fyne.TextStyle{Bold: true}

	parallelismDepthEntry := widget.NewEntry()
	parallelismDepthEntry.SetPlaceHolder("Глубина параллельности")

	hybridLabel := widget.NewLabel("Жадный поиск")
	hybridLabel.Alignment = fyne.TextAlignCenter
	hybridLabel.TextStyle = fyne.TextStyle{Bold: true}

	localSearchIterationsEntry := widget.NewEntry()
	localSearchIterationsEntry.SetPlaceHolder("Итерации локального поиска")

	saveButton := widget.NewButtonWithIcon("Сохранить", theme.ConfirmIcon(), func() {
		parallelDepth, err := utils.ParseUint(parallelismDepthEntry.Text)
		if err != nil {
			dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
			return
		}

		mCfg := &MaghoutConfig{ParallelDepth: parallelDepth}

		localIters, err := utils.ParseUint(localSearchIterationsEntry.Text)
		if err != nil {
			dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
			return
		}

		hCfg := &GreedySearchConfig{Iterations: localIters}

		state.MethodConfigs = nil
		state.MethodConfigs = append(state.MethodConfigs, mCfg, hCfg)

		state.NavigationState.NextButton.Enable()
	})

	form := container.NewVBox(
		layout.NewSpacer(),
		container.NewPadded(title),
		layout.NewSpacer(),
		container.NewPadded(maghoutLabel),
		container.NewPadded(parallelismDepthEntry),
		layout.NewSpacer(),
		container.NewPadded(hybridLabel),
		container.NewPadded(localSearchIterationsEntry),
		layout.NewSpacer(),
		container.NewPadded(saveButton),
		layout.NewSpacer(),
	)

	centered := container.NewCenter(container.NewVBox(
		form,
	))

	return container.NewBorder(nil, nil, nil, nil, container.NewPadded(centered)), initFunc
}

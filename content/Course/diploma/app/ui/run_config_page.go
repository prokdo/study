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

func NewRunConfigPage(state *AppState) (fyne.CanvasObject, func()) {
	initFunc := func() {
		state.NavigationState.BackButton.Enable()

		if state.RunConfig == nil {
			state.NavigationState.NextButton.Disable()
		} else {
			state.NavigationState.NextButton.Enable()
		}
	}

	title := canvas.NewText("Конфигурация запуска", nil)
	title.TextStyle = fyne.TextStyle{Bold: true}
	title.Alignment = fyne.TextAlignCenter

	runsEntry := widget.NewEntry()
	runsEntry.SetPlaceHolder("Количество запусков")

	fixGraphCheck := widget.NewCheck("", nil)
	if state.GeneratorConfig == nil && state.Graph != nil {
		fixGraphCheck.SetChecked(true)
		fixGraphCheck.Disable()
	}

	fixGraphLabel := widget.NewLabel("Фиксировать граф")
	fixGraphLabel.TextStyle = fyne.TextStyle{Bold: true}
	fixGraphLabel.Alignment = fyne.TextAlignLeading

	fixGraphContainer := container.NewHBox(fixGraphCheck, fixGraphLabel)

	saveButton := widget.NewButtonWithIcon("Сохранить", theme.ConfirmIcon(), func() {
		runs, err := utils.ParseUint(runsEntry.Text)
		if err != nil || runs == 0 {
			dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
			return
		}

		state.RunConfig = &RunConfig{
			IsGraphFixed: fixGraphCheck.Checked,
			RunsNumber:   runs,
		}
		state.NavigationState.NextButton.Enable()
	})

	form := container.NewVBox(
		layout.NewSpacer(),
		container.NewPadded(title),
		layout.NewSpacer(),
		container.NewPadded(runsEntry),
		container.NewPadded(fixGraphContainer),
		layout.NewSpacer(),
		container.NewPadded(saveButton),
		layout.NewSpacer(),
	)

	centered := container.NewCenter(container.NewVBox(form))
	return centered, initFunc
}

package ui

import (
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/theme"
)

var (
	LightTheme = theme.LightTheme()
	DarkTheme  = theme.DarkTheme()
)

func ApplyTheme(app fyne.App, dark bool) {
	if dark {
		app.Settings().SetTheme(DarkTheme)
	} else {
		app.Settings().SetTheme(LightTheme)
	}
}

func ToggleTheme(app fyne.App) {
	if app.Settings().Theme() == DarkTheme {
		app.Settings().SetTheme(LightTheme)
	} else {
		app.Settings().SetTheme(DarkTheme)
	}
}

func InitTheme(app fyne.App) {
	app.Settings().SetTheme(DarkTheme)
}

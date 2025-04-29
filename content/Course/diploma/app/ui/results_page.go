package ui

import (
	"encoding/csv"
	"fmt"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"time"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/data/binding"
	"fyne.io/fyne/v2/dialog"
	"fyne.io/fyne/v2/widget"
)

func NewResultsPage(state *AppState) (fyne.CanvasObject, func()) {
	headers := []string{"ID", "Время (мс)", "F1-фактор", "Мощность"}

	exactResults := binding.NewUntypedList()
	approxResults := binding.NewUntypedList()

	buildTable := func(results binding.UntypedList) *widget.Table {
		table := widget.NewTable(
			func() (int, int) {
				length := results.Length()
				return length + 1, len(headers)
			},
			func() fyne.CanvasObject {
				return widget.NewLabel("")
			},
			func(cell widget.TableCellID, o fyne.CanvasObject) {
				label := o.(*widget.Label)
				i, j := cell.Row, cell.Col

				if i == 0 {
					label.SetText(headers[j])
					label.TextStyle = fyne.TextStyle{Bold: true}
				} else {
					item, _ := results.GetValue(i - 1)
					if item == nil {
						label.SetText("")
						return
					}
					res := item.(*Result)

					switch j {
					case 0:
						label.SetText(strconv.Itoa(res.RunId))
					case 1:
						label.SetText(strconv.FormatInt(res.Time, 10))
					case 2:
						label.SetText(fmt.Sprintf("%.2f", res.F1Factor))
					case 3:
						label.SetText(strconv.Itoa(len(res.Result)))
					}
				}
			},
		)

		table.SetColumnWidth(0, 60)
		table.SetColumnWidth(1, 100)
		table.SetColumnWidth(2, 80)
		table.SetColumnWidth(3, 80)

		table.SetRowHeight(0, 30)
		return table
	}

	saveBtn := widget.NewButton("Сохранить в CSV", func() {
		dialog.ShowFolderOpen(func(uri fyne.ListableURI, err error) {
			if err != nil {
				dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
				return
			}
			if uri == nil {
				return
			}

			dir := uri.Path()

			save := func(filename string, results binding.UntypedList) error {
				path := filepath.Join(dir, filename)
				file, err := os.Create(path)
				if err != nil {
					return err
				}
				defer file.Close()

				writer := csv.NewWriter(file)
				defer writer.Flush()

				writer.Write([]string{"ID", "Время (мс)", "F1-фактор", "Мощность"})

				length := results.Length()
				for i := range length {
					item, _ := results.GetValue(i)
					res := item.(*Result)

					record := []string{
						strconv.Itoa(res.RunId),
						strconv.FormatInt(res.Time, 10),
						fmt.Sprintf("%.2f", res.F1Factor),
						strconv.Itoa(len(res.Result)),
					}
					writer.Write(record)
				}
				return nil
			}

			if err := save(fmt.Sprintf("%s_exact_results.csv", time.Now().Format("2006-01-02T15:04:05")), exactResults); err != nil {
				dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
				return
			}
			if err := save(fmt.Sprintf("%s_approx_results.csv", time.Now().Format("2006-01-02T15:04:05")), approxResults); err != nil {
				dialog.ShowError(err, fyne.CurrentApp().Driver().AllWindows()[0])
				return
			}
		}, fyne.CurrentApp().Driver().AllWindows()[0])
	})

	exactTable := buildTable(exactResults)
	approxTable := buildTable(approxResults)

	exactScroll := container.NewVScroll(exactTable)
	approxScroll := container.NewVScroll(approxTable)

	left := container.NewBorder(
		widget.NewLabel("Метод Магу"),
		nil, nil, nil,
		exactScroll,
	)

	right := container.NewBorder(
		widget.NewLabel("Жадный поиск"),
		nil, nil, nil,
		approxScroll,
	)

	split := container.NewHSplit(left, right)
	split.Offset = 0.5

	initFunc := func() {
		state.NavigationState.NextButton.Enable()
		state.NavigationState.BackButton.Enable()

		exactResults.Set([]any{})
		approxResults.Set([]any{})

		for _, res := range state.Results {
			method := strings.TrimSpace(res.Method)
			switch method {
			case string(MaghoutMethod):
				exactResults.Append(res)
			case string(GreedySearchMethod):
				approxResults.Append(res)
			}
		}

		exactTable.Refresh()
		approxTable.Refresh()
	}

	return container.NewBorder(nil, saveBtn, nil, nil, split), initFunc
}

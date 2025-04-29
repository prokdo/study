package utils

import (
	"context"
	"fmt"
	"image"
	"image/color"
	"io"
	"os"

	"github.com/goccy/go-graphviz"
	"gonum.org/v1/plot"
	"gonum.org/v1/plot/plotter"
	"gonum.org/v1/plot/vg"
	"gonum.org/v1/plot/vg/draw"
	"gonum.org/v1/plot/vg/vgimg"

	"graphmis/graph"
)

func RenderDotToWriter[T comparable](g graph.Graph[T], w io.Writer, verticesToColor ...[]T) error {
	dot := g.Dot(verticesToColor...)

	ctx := context.Background()
	gv, err := graphviz.New(ctx)
	if err != nil {
		return fmt.Errorf("viz: new graphviz: %w", err)
	}
	defer gv.Close()

	cg, err := graphviz.ParseBytes([]byte(dot))
	if err != nil {
		return fmt.Errorf("viz: parse DOT: %w", err)
	}
	defer cg.Close()

	if err = gv.Render(ctx, cg, graphviz.SVG, w); err != nil {
		return fmt.Errorf("viz: render to writer: %w", err)
	}

	return nil
}

func RenderDotToImage[T comparable](g graph.Graph[T], verticesToColor ...[]T) (image.Image, error) {
	dot := g.Dot(verticesToColor...)

	ctx := context.Background()
	gv, err := graphviz.New(ctx)
	if err != nil {
		return nil, fmt.Errorf("viz: new graphviz: %w", err)
	}
	defer gv.Close()

	cg, err := graphviz.ParseBytes([]byte(dot))
	if err != nil {
		return nil, fmt.Errorf("viz: parse DOT: %w", err)
	}
	defer cg.Close()

	img, err := gv.RenderImage(ctx, cg)
	if err != nil {
		return nil, fmt.Errorf("viz: render image: %w", err)
	}

	return img, nil
}

func RenderDotToFile[T comparable](g graph.Graph[T], filename string, verticesToColor ...[]T) error {
	f, err := os.Create(filename)
	if err != nil {
		return fmt.Errorf("viz: cannot create file %q: %w", filename, err)
	}
	defer f.Close()

	dot := g.Dot(verticesToColor...)

	ctx := context.Background()
	gv, err := graphviz.New(ctx)
	if err != nil {
		return fmt.Errorf("viz: new graphviz: %w", err)
	}
	defer gv.Close()

	cg, err := graphviz.ParseBytes([]byte(dot))
	if err != nil {
		return fmt.Errorf("viz: parse DOT: %w", err)
	}
	defer cg.Close()

	if err = gv.RenderFilename(ctx, cg, graphviz.PNG, filename); err != nil {
		return fmt.Errorf("viz: render to file: %w", err)
	}

	return nil
}

func RenderLineChart(title string, x []float64, y []float64, w io.Writer) error {
	if len(x) != len(y) {
		return fmt.Errorf("x and y slices must have same length")
	}

	p := plot.New()
	p.Title.Text = title
	p.X.Label.Text = ""
	p.Y.Label.Text = ""

	pts := make(plotter.XYs, len(x))
	for i := range x {
		pts[i].X = x[i]
		pts[i].Y = y[i]
	}

	line, err := plotter.NewLine(pts)
	if err != nil {
		return fmt.Errorf("creating line: %w", err)
	}
	line.Color = color.RGBA{R: 0, G: 128, B: 255, A: 255}
	line.Width = vg.Points(2)

	p.Add(line)

	canvas := vgimg.New(6*vg.Inch, 4*vg.Inch)
	dc := draw.New(canvas)
	p.Draw(dc)
	png := vgimg.PngCanvas{Canvas: canvas}
	if _, err := png.WriteTo(w); err != nil {
		return fmt.Errorf("writing line chart PNG: %w", err)
	}
	return nil
}

func RenderBarChart(title string, values []float64, labels []string, w io.Writer) error {
	if len(values) != len(labels) {
		return fmt.Errorf("values and labels must have same length")
	}

	p := plot.New()
	p.Title.Text = title
	p.Y.Label.Text = ""

	bars := make(plotter.Values, len(values))
	for i, v := range values {
		bars[i] = v
	}

	barWidth := vg.Points(20)
	bchart, err := plotter.NewBarChart(bars, barWidth)
	if err != nil {
		return fmt.Errorf("creating bar chart: %w", err)
	}
	bchart.LineStyle.Width = vg.Length(0)
	bchart.Color = color.RGBA{R: 255, G: 127, B: 0, A: 255}

	p.Add(bchart)
	p.NominalX(labels...)

	canvas := vgimg.New(6*vg.Inch, 4*vg.Inch)
	dc := draw.New(canvas)
	p.Draw(dc)
	png := vgimg.PngCanvas{Canvas: canvas}
	if _, err := png.WriteTo(w); err != nil {
		return fmt.Errorf("writing bar chart PNG: %w", err)
	}
	return nil
}

func RenderHistogram(title string, data plotter.Values, bins int, w io.Writer) error {
	p := plot.New()
	p.Title.Text = title
	p.X.Label.Text = ""
	p.Y.Label.Text = ""

	hist, err := plotter.NewHist(data, bins)
	if err != nil {
		return fmt.Errorf("creating histogram: %w", err)
	}
	hist.Color = color.RGBA{B: 200, A: 200}

	p.Add(hist)

	canvas := vgimg.New(6*vg.Inch, 4*vg.Inch)
	dc := draw.New(canvas)
	p.Draw(dc)
	png := vgimg.PngCanvas{Canvas: canvas}
	if _, err := png.WriteTo(w); err != nil {
		return fmt.Errorf("writing histogram PNG: %w", err)
	}
	return nil
}

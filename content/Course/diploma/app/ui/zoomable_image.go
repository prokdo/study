package ui

import (
	"image"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/canvas"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/driver/desktop"
	"fyne.io/fyne/v2/widget"
)

type ZoomableImage struct {
	widget.BaseWidget
	image     image.Image
	scale     float32
	offsetX   float32
	offsetY   float32
	dragStart fyne.Position
	dragging  bool
	img       *canvas.Image
	scroll    *container.Scroll
}

func NewZoomableImage(img image.Image) *ZoomableImage {
	z := &ZoomableImage{
		image:   img,
		scale:   1.0,
		offsetX: 0,
		offsetY: 0,
	}
	z.img = canvas.NewImageFromImage(img)
	z.img.FillMode = canvas.ImageFillOriginal
	z.scroll = container.NewScroll(z.img)
	z.scroll.SetMinSize(fyne.NewSize(800, 800))
	z.ExtendBaseWidget(z)
	return z
}

func (z *ZoomableImage) CreateRenderer() fyne.WidgetRenderer {
	return widget.NewSimpleRenderer(z.scroll)
}

func (z *ZoomableImage) Scrolled(e *fyne.ScrollEvent) {
	zoomFactor := float32(1.1)
	if e.Scrolled.DY > 0 {
		z.scale /= zoomFactor
	} else {
		z.scale *= zoomFactor
	}
	if z.scale < 0.2 {
		z.scale = 0.2
	}
	if z.scale > 5.0 {
		z.scale = 5.0
	}
	z.applyTransform()
}

func (z *ZoomableImage) MouseDown(e *desktop.MouseEvent) {
	z.dragging = true
	z.dragStart = e.Position
}

func (z *ZoomableImage) MouseUp(*desktop.MouseEvent) {
	z.dragging = false
}

func (z *ZoomableImage) MouseMoved(e *desktop.MouseEvent) {
	if z.dragging {
		deltaX := float32(e.Position.X - z.dragStart.X)
		deltaY := float32(e.Position.Y - z.dragStart.Y)
		z.offsetX += deltaX
		z.offsetY += deltaY
		z.dragStart = e.Position
		z.applyTransform()
	}
}

func (z *ZoomableImage) SetImage(img image.Image) {
	z.image = img
	z.img.Image = img
	z.img.Refresh()
	z.scale = 1.0
	z.offsetX = 0
	z.offsetY = 0
	z.applyTransform()
}

func (z *ZoomableImage) applyTransform() {
	if z.img.Image == nil {
		return
	}

	imgWidth := float32(z.img.Image.Bounds().Dx()) * z.scale
	imgHeight := float32(z.img.Image.Bounds().Dy()) * z.scale

	z.img.Resize(fyne.NewSize(imgWidth, imgHeight))

	z.img.Move(fyne.NewPos(z.offsetX, z.offsetY))

	z.img.Refresh()
	z.scroll.Refresh()
}

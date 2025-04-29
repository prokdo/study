package utils

import (
	"fmt"
	"image"
	"strconv"
)

func ParseUint(s string) (int, error) {
	i, err := strconv.Atoi(s)
	if err != nil {
		return i, err
	}

	if i <= 0 {
		return i, fmt.Errorf("параметр должен быть положительным целым числом")
	}

	return i, nil
}

func ParseFloatCoefficient(s string) (float64, error) {
	f, err := strconv.ParseFloat(s, 64)
	if err != nil {
		return f, err
	}

	if f < 0 || f > 1 {
		return f, fmt.Errorf("параметр должен быть в пределах [0; 1]")
	}

	return f, nil
}

func FindFirstError(errors ...error) error {
	for _, err := range errors {
		if err != nil {
			return err
		}
	}
	return nil
}

type ImageWriter struct {
	Image *image.RGBA
}

func (w *ImageWriter) Write(p []byte) (n int, err error) {
	copy(w.Image.Pix, p)
	return len(p), nil
}

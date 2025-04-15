package graph

import "math"

type graphMatrix[T comparable] interface {
	Size() int
	Get(i, j int) T
	Set(i, j int, v T)
}

type adjMatrix struct {
	size int
	data []bool
}

func (m *adjMatrix) Size() int {
	return m.size
}

func (m *adjMatrix) Get(i, j int) bool {
	return m.data[i*m.size+j]
}

func (m *adjMatrix) GetRow(i int) []bool {
	return m.data[i*m.size : (i+1)*m.size]
}

func (m *adjMatrix) Set(i, j int, v bool) {
	m.data[i*m.size+j] = v
}

func newAdjMatrix(size int) *adjMatrix {
	return &adjMatrix{
		size: size,
		data: make([]bool, size*size),
	}
}

var NO_WEIGHT = math.Inf(1)

type weightMatrix struct {
	size int
	data []float64
}

func (m *weightMatrix) Size() int {
	return m.size
}

func (m *weightMatrix) Get(i, j int) float64 {
	return m.data[i*m.size+j]
}

func (m *weightMatrix) GetRow(i int) []float64 {
	return m.data[i*m.size : (i+1)*m.size]
}

func (m *weightMatrix) Set(i, j int, v float64) {
	m.data[i*m.size+j] = v
}

func newWeightMatrix(size int) *weightMatrix {
	return &weightMatrix{
		size: size,
		data: make([]float64, size*size),
	}

}

type graphCache struct {
	AdjMatrix    *adjMatrix
	WeightMatrix *weightMatrix
}

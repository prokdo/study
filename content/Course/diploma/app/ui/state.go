package ui

import (
	"fyne.io/fyne/v2/widget"

	"graphmis/graph"
)

type GeneratorConfig struct {
	GraphType         graph.GraphType
	MinVerticesNumber int
	MaxVerticesNumber int
	GraphDensity      float64
}

type MethodType string

const (
	MaghoutMethod      MethodType = "Метод Магу"
	GreedySearchMethod MethodType = "Жадный поиск"
	// GeneticMethod      MethodType = "Генетический алгоритм"
)

type MethodConfig interface {
	MethodType() MethodType
}

type MaghoutConfig struct {
	MethodConfig
	ParallelDepth int
}

func (m *MaghoutConfig) MethodType() MethodType {
	return MaghoutMethod
}

type GreedySearchConfig struct {
	MethodConfig
	Iterations int
}

func (g *GreedySearchConfig) MethodType() MethodType {
	return GreedySearchMethod
}

type RunConfig struct {
	IsGraphFixed bool
	RunsNumber   int
}

type Result struct {
	RunId    int
	Method   string
	Time     int64
	Result   []string
	F1Factor float64
}

type NavigationState struct {
	NextButton *widget.Button
	BackButton *widget.Button
}

type AppState struct {
	GeneratorConfig *GeneratorConfig
	Graph           graph.Graph[string]
	MethodConfigs   []MethodConfig
	RunConfig       *RunConfig
	Results         []*Result
	NavigationState *NavigationState
}

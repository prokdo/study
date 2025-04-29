package graph

import (
	"fmt"
)

type GraphType int

const (
	Directed GraphType = iota
	Undirected
)

type EdgeOptions[T comparable] struct {
	From   *T
	To     *T
	Weight float64
}

func (eo EdgeOptions[T]) String() string {
	weightStr := ""
	if eo.Weight != NO_WEIGHT {
		weightStr = fmt.Sprintf(", weight: %v", eo.Weight)
	}
	return fmt.Sprintf("%v -> %v%v", *eo.From, *eo.To, weightStr)
}

type Graph[T comparable] interface {
	Type() GraphType
	Size() int

	HasLoop() bool
	HasWeights() bool
	IsAcyclic() bool

	AddEdge(from, to *T, weight ...float64) bool
	AddVertex(v *T) bool
	ClearCache()
	ClearWeights()
	ContainsEdge(from, to *T) bool
	ContainsVertex(v *T) bool
	Dot(verticesToColor ...[]T) string
	ExportToFile(path string, overwrite bool, verticesToColor ...[]T) error
	GetAllEdges() []*EdgeOptions[T]
	GetAllVertices() []T
	GetEdge(from, to *T) *EdgeOptions[T]
	GetEdgesOf(v *T) []*EdgeOptions[T]
	GetNeighbors(v *T) []T
	GetWeight(from, to *T) (float64, bool)
	RemoveEdge(from, to *T) bool
	RemoveVertex(v *T) bool
	Reset()
	SetWeight(from, to *T, weight float64) bool
}

func NewGraph[T comparable](gt GraphType) Graph[T] {
	switch gt {
	case Directed:
		return &graph[T]{
			gtype:         Directed,
			adjList:       make(map[T][]T),
			vertexToIndex: make(map[T]int),
		}
	case Undirected:
		return &graph[T]{
			gtype:         Undirected,
			adjList:       make(map[T][]T),
			vertexToIndex: make(map[T]int),
		}
	default:
		return nil
	}
}

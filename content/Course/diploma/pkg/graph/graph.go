package graph

import (
	"bytes"
	"fmt"
	"os"
	"sync"
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
	ExportToFile(path string, overwrite bool) error
	GetAllEdges() []*EdgeOptions[T]
	GetAllVertices() []*T
	GetEdge(from, to *T) *EdgeOptions[T]
	GetEdgesOf(v *T) []*EdgeOptions[T]
	GetNeighbors(v *T) []*T
	GetWeight(from, to *T) (float64, bool)
	RemoveEdge(from, to *T) bool
	RemoveVertex(v *T) bool
	Reset()
	SetWeight(from, to *T, weight float64) bool
	String() string
}

type graph[T comparable] struct {
	gtype GraphType
	size  int

	adjList map[T][]T

	cache graphCache

	hasLoop   *bool
	isAcyclic *bool

	indexToVertex []T
	vertexToIndex map[T]int
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

func (g *graph[T]) Type() GraphType {
	return g.gtype
}

func (g *graph[T]) Size() int {
	return g.size
}

func (g *graph[T]) HasLoop() bool {
	if g.size == 0 {
		return false
	}

	if g.hasLoop != nil {
		return *g.hasLoop
	}

	g.hasLoop = new(bool)
	if g.cache.AdjMatrix != nil {
		*g.hasLoop = checkForLoopsByAdjMatrix(*g.cache.AdjMatrix)
	} else {
		*g.hasLoop = checkForLoopsByAdjList(g.adjList)
	}

	return *g.hasLoop
}

func (g *graph[T]) HasWeights() bool {
	return g.cache.WeightMatrix != nil
}

func (g *graph[T]) IsAcyclic() bool {
	if g.size == 0 {
		return true
	}

	if g.isAcyclic != nil {
		return *g.isAcyclic
	}

	g.isAcyclic = new(bool)
	*g.isAcyclic = !checkForCycles(g)
	return *g.isAcyclic
}

func (g *graph[T]) AddEdge(from, to *T, weight ...float64) bool {
	if from == nil || to == nil {
		return false
	}

	if g.ContainsEdge(from, to) {
		return false
	}

	if !g.ContainsVertex(from) {
		g.AddVertex(from)
	}

	if !g.ContainsVertex(to) {
		g.AddVertex(to)
	}

	g.adjList[*from] = append(g.adjList[*from], *to)
	if g.gtype == Undirected {
		g.adjList[*to] = append(g.adjList[*to], *from)
	}

	if g.cache.AdjMatrix != nil {
		fromIdx := g.vertexToIndex[*from]
		toIdx := g.vertexToIndex[*to]
		g.cache.AdjMatrix.Set(fromIdx, toIdx, true)
		if g.gtype == Undirected {
			g.cache.AdjMatrix.Set(toIdx, fromIdx, true)
		}
	}

	if len := len(weight); len != 0 {
		if g.cache.WeightMatrix == nil {
			initWeightMatrix(g)
		}
		fromIdx := g.vertexToIndex[*from]
		toIdx := g.vertexToIndex[*to]
		g.cache.WeightMatrix.Set(fromIdx, toIdx, weight[len-1])
		if g.gtype == Undirected {
			g.cache.WeightMatrix.Set(toIdx, fromIdx, weight[len-1])
		}
	}

	if from == to {
		g.hasLoop = new(bool)
		*g.hasLoop = true
	}

	g.isAcyclic = nil

	return true

}

func (g *graph[T]) AddVertex(v *T) bool {
	if v == nil {
		return false
	}

	if g.ContainsVertex(v) {
		return false
	}

	g.vertexToIndex[*v] = len(g.indexToVertex)
	g.indexToVertex = append(g.indexToVertex, *v)

	g.adjList[*v] = []T{}

	g.size++

	return true
}

func (g *graph[T]) ClearCache() {
	g.cache = graphCache{}
}

func (g *graph[T]) ClearWeights() {
	g.cache.WeightMatrix = nil
}

func (g *graph[T]) ContainsEdge(from, to *T) bool {
	if from == nil || to == nil {
		return false
	}

	if g.cache.AdjMatrix == nil {
		initAdjMatrix(g)
	}

	fromIdx := g.vertexToIndex[*from]
	toIdx := g.vertexToIndex[*to]

	return g.cache.AdjMatrix.Get(fromIdx, toIdx)
}

func (g *graph[T]) ContainsVertex(v *T) bool {
	if v == nil {
		return false
	}

	_, ok := g.vertexToIndex[*v]
	return ok
}

func (g *graph[T]) ExportToFile(path string, overwrite bool) error {
	_, err := os.Stat(path)
	if err == nil {
		if !overwrite {
			return fmt.Errorf("файл %s уже существует", path)
		}
	} else if !os.IsNotExist(err) {
		return err
	}

	dotStr := g.String()

	err = os.WriteFile(path, []byte(dotStr), 0644)
	if err != nil {
		return err
	}
	return nil
}

func (g *graph[T]) GetAllEdges() []*EdgeOptions[T] {
	edges := make([]*EdgeOptions[T], 0)

	for from, neighbors := range g.adjList {
		for _, to := range neighbors {
			var weight float64
			if g.cache.WeightMatrix != nil {
				weight = g.cache.WeightMatrix.Get(g.vertexToIndex[from], g.vertexToIndex[to])
			} else {
				weight = NO_WEIGHT
			}
			edges = append(edges, &EdgeOptions[T]{
				From:   &from,
				To:     &to,
				Weight: weight,
			})
		}
	}

	return edges
}

func (g *graph[T]) GetAllVertices() []*T {
	vertices := make([]*T, 0)

	for _, v := range g.indexToVertex {
		vertices = append(vertices, &v)
	}

	return vertices
}

func (g *graph[T]) GetEdge(from, to *T) *EdgeOptions[T] {
	if from == nil || to == nil {
		return nil
	}

	if g.cache.AdjMatrix == nil {
		initAdjMatrix(g)
	}

	fromIdx, ok := g.vertexToIndex[*from]
	if !ok {
		return nil
	}
	toIdx, ok := g.vertexToIndex[*to]
	if !ok {
		return nil
	}
	if !g.cache.AdjMatrix.Get(fromIdx, toIdx) {
		return nil
	}
	if g.cache.WeightMatrix != nil {
		return &EdgeOptions[T]{
			From:   from,
			To:     to,
			Weight: g.cache.WeightMatrix.Get(fromIdx, toIdx),
		}
	}
	return &EdgeOptions[T]{
		From:   from,
		To:     to,
		Weight: NO_WEIGHT,
	}
}

func (g *graph[T]) GetEdgesOf(v *T) []*EdgeOptions[T] {
	if v == nil {
		return nil
	}

	if !g.ContainsVertex(v) {
		return nil
	}

	edges := make([]*EdgeOptions[T], 0)
	for _, to := range g.adjList[*v] {
		var weight float64
		if g.cache.WeightMatrix != nil {
			weight = g.cache.WeightMatrix.Get(g.vertexToIndex[*v], g.vertexToIndex[to])
		} else {
			weight = NO_WEIGHT
		}

		edges = append(edges, &EdgeOptions[T]{
			From:   v,
			To:     &to,
			Weight: weight,
		})

		if g.gtype == Undirected {
			edges = append(edges, &EdgeOptions[T]{
				From:   &to,
				To:     v,
				Weight: weight,
			})
		}
	}

	return edges
}

func (g *graph[T]) GetNeighbors(v *T) []*T {
	if v == nil {
		return nil
	}

	if !g.ContainsVertex(v) {
		return nil
	}

	if g.cache.AdjMatrix == nil {
		initAdjMatrix(g)
	}

	neighbors := make([]*T, 0)
	vIdx := g.vertexToIndex[*v]

	for i := range g.indexToVertex {
		if g.cache.AdjMatrix.Get(i, vIdx) {
			vertex := g.indexToVertex[i]
			neighbors = append(neighbors, &vertex)
		}
	}

	if g.gtype == Directed {
		for i := range g.indexToVertex {
			if g.cache.AdjMatrix.Get(vIdx, i) {
				vertex := g.indexToVertex[i]
				neighbors = append(neighbors, &vertex)
			}
		}
	}

	return neighbors
}

func (g *graph[T]) GetWeight(from, to *T) (float64, bool) {
	if from == nil || to == nil {
		return NO_WEIGHT, false
	}

	if !g.ContainsEdge(from, to) {
		return NO_WEIGHT, false
	}

	if g.cache.WeightMatrix == nil {
		return NO_WEIGHT, true
	}

	fromIdx := g.vertexToIndex[*from]
	toIdx := g.vertexToIndex[*to]

	return g.cache.WeightMatrix.Get(fromIdx, toIdx), true
}

func (g *graph[T]) RemoveEdge(from, to *T) bool {
	if from == nil || to == nil {
		return false
	}

	neighbors, exists := g.adjList[*from]
	if !exists {
		return false
	}

	for i, neighbor := range neighbors {
		if neighbor == *to {
			g.adjList[*from] = append(neighbors[:i], neighbors[i+1:]...)
			break
		}
	}

	if g.gtype == Undirected {
		neighbors, exists = g.adjList[*to]
		if exists {
			for i, neighbor := range neighbors {
				if neighbor == *from {
					g.adjList[*to] = append(neighbors[:i], neighbors[i+1:]...)
					break
				}
			}
		}
	}

	if g.cache.AdjMatrix != nil {
		fromIdx := g.vertexToIndex[*from]
		toIdx := g.vertexToIndex[*to]
		g.cache.AdjMatrix.Set(fromIdx, toIdx, false)
		if g.gtype == Undirected {
			g.cache.AdjMatrix.Set(toIdx, fromIdx, false)
		}
	}

	if g.cache.WeightMatrix != nil {
		fromIdx := g.vertexToIndex[*from]
		toIdx := g.vertexToIndex[*to]
		g.cache.WeightMatrix.Set(fromIdx, toIdx, NO_WEIGHT)
		if g.gtype == Undirected {
			g.cache.WeightMatrix.Set(toIdx, fromIdx, NO_WEIGHT)
		}
	}

	return true
}

func (g *graph[T]) RemoveVertex(v *T) bool {
	if v == nil {
		return false
	}

	if !g.ContainsVertex(v) {
		return false
	}

	delete(g.adjList, *v)

	for from, neighbors := range g.adjList {
		for i, neighbor := range neighbors {
			if neighbor == *v {
				g.adjList[from] = append(neighbors[:i], neighbors[i+1:]...)
				break
			}
		}
	}

	index := g.vertexToIndex[*v]
	delete(g.vertexToIndex, *v)
	g.indexToVertex = append(g.indexToVertex[:index], g.indexToVertex[index+1:]...)

	if g.cache.AdjMatrix != nil {
		newAdjMatrix := newAdjMatrix(g.size - 1)
		for i := range g.size {
			if i == index {
				continue
			}
			for j := range g.size {
				if j == index {
					continue
				}
				newI := i
				newJ := j
				if i > index {
					newI--
				}
				if j > index {
					newJ--
				}
				newAdjMatrix.Set(newI, newJ, g.cache.AdjMatrix.Get(i, j))
			}
		}
		g.cache.AdjMatrix = newAdjMatrix
	}

	if g.cache.WeightMatrix != nil {
		newWeightMatrix := newWeightMatrix(g.size - 1)
		for i := range g.size {
			if i == index {
				continue
			}
			for j := range g.size {
				if j == index {
					continue
				}
				newI := i
				newJ := j
				if i > index {
					newI--
				}
				if j > index {
					newJ--
				}
				newWeightMatrix.Set(newI, newJ, g.cache.WeightMatrix.Get(i, j))
			}
		}
		g.cache.WeightMatrix = newWeightMatrix
	}

	g.size--

	return true
}

func (g *graph[T]) Reset() {
	g.size = 0
	g.adjList = make(map[T][]T)
	g.cache = graphCache{}
	g.hasLoop = nil
	g.isAcyclic = nil
	g.indexToVertex = make([]T, 0)
	g.vertexToIndex = make(map[T]int)
}

func (g *graph[T]) SetWeight(from, to *T, weight float64) bool {
	if from == nil || to == nil {
		return false
	}

	if !g.ContainsEdge(from, to) {
		return false
	}

	if g.cache.WeightMatrix == nil {
		initWeightMatrix(g)
	}

	fromIdx := g.vertexToIndex[*from]
	toIdx := g.vertexToIndex[*to]

	g.cache.WeightMatrix.Set(fromIdx, toIdx, weight)

	if g.gtype == Undirected {
		g.cache.WeightMatrix.Set(toIdx, fromIdx, weight)
	}

	return true
}

func (g *graph[T]) String() string {
	var buf bytes.Buffer

	if g.gtype == Directed {
		buf.WriteString("digraph G {\n")
	} else {
		buf.WriteString("graph G {\n")
	}

	vertexLines := make([]string, len(g.indexToVertex))
	var wg sync.WaitGroup
	for i, v := range g.indexToVertex {
		wg.Add(1)
		go func(i int, v T) {
			defer wg.Done()
			vertexLines[i] = fmt.Sprintf("  %d [label=\"%v\"];\n", i, v)
		}(i, v)
	}
	wg.Wait()
	for _, line := range vertexLines {
		buf.WriteString(line)
	}

	var connector string
	if g.gtype == Directed {
		connector = "->"
	} else {
		connector = "--"
	}
	edgeChan := make(chan string, 100)
	for from, neighbors := range g.adjList {
		fromIdx, ok := g.vertexToIndex[from]
		if !ok {
			continue
		}
		wg.Add(1)
		go func(fromIdx int, neighbors []T) {
			defer wg.Done()
			for _, to := range neighbors {
				toIdx, ok := g.vertexToIndex[to]
				if !ok {
					continue
				}
				if g.gtype == Undirected && fromIdx > toIdx {
					continue
				}
				edgeStr := fmt.Sprintf("  %d %s %d", fromIdx, connector, toIdx)
				if g.cache.WeightMatrix != nil {
					weight := g.cache.WeightMatrix.Get(fromIdx, toIdx)
					if weight != NO_WEIGHT {
						edgeStr += fmt.Sprintf(" [label=\"%v\"]", weight)
					}
				}
				edgeStr += ";\n"
				edgeChan <- edgeStr
			}
		}(fromIdx, neighbors)
	}

	go func() {
		wg.Wait()
		close(edgeChan)
	}()

	for edgeLine := range edgeChan {
		buf.WriteString(edgeLine)
	}

	buf.WriteString("}\n")
	return buf.String()
}

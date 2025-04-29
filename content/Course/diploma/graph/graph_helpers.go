package graph

import (
	"context"
	"os"
	"slices"
	"sort"
	"strconv"
	"sync"
	"sync/atomic"

	"github.com/awalterschulze/gographviz"
)

func initAdjMatrix[T comparable](g *graph[T]) {
	matrix := newAdjMatrix(g.size)
	var wg sync.WaitGroup

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
				if g.gtype == Directed {
					matrix.Set(fromIdx, toIdx, true)
				} else {
					if fromIdx < toIdx {
						matrix.Set(fromIdx, toIdx, true)
						matrix.Set(toIdx, fromIdx, true)
					}
				}
			}
		}(fromIdx, neighbors)
	}

	wg.Wait()
	g.cache.AdjMatrix = matrix
}

func initWeightMatrix[T comparable](g *graph[T]) *weightMatrix {
	matrix := newWeightMatrix(g.size)
	var wg sync.WaitGroup

	for i := range g.size {
		wg.Add(1)
		go func(i int) {
			defer wg.Done()
			offset := i * g.size
			for j := range g.size {
				matrix.data[offset+j] = NO_WEIGHT
			}
		}(i)
	}

	wg.Wait()
	return matrix
}

func checkForLoopsByAdjList[T comparable](adjList map[T][]T) bool {
	var found atomic.Bool
	wg := sync.WaitGroup{}

	for from, neighbors := range adjList {
		wg.Add(1)
		go func(from T, neighbors []T) {
			defer wg.Done()
			if slices.Contains(neighbors, from) {
				found.Store(true)
			}
		}(from, neighbors)
	}

	wg.Wait()
	return found.Load()
}

func checkForLoopsByAdjMatrix(adjMatrix adjMatrix) bool {
	size := adjMatrix.Size()
	var found atomic.Bool
	wg := sync.WaitGroup{}

	for i := range size {
		wg.Add(1)
		go func(i int) {
			defer wg.Done()
			if adjMatrix.Get(i, i) {
				found.Store(true)
			}
		}(i)
	}

	wg.Wait()
	return found.Load()
}

func checkForCycles[T comparable](g *graph[T]) bool {
	if g.size == 0 {
		return false
	}

	var found atomic.Bool
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	wg := sync.WaitGroup{}

	for v := range g.adjList {
		if found.Load() {
			break
		}

		wg.Add(1)
		go func(start T) {
			defer wg.Done()
			visited := make(map[T]bool)
			recStack := make(map[T]bool)

			if checkForCyclesDFS(ctx, g, start, visited, recStack, nil) {
				found.Store(true)
				cancel()
			}
		}(v)
	}

	wg.Wait()
	return found.Load()
}

func checkForCyclesDFS[T comparable](
	ctx context.Context,
	g *graph[T],
	current T,
	visited map[T]bool,
	recStack map[T]bool,
	parent *T,
) bool {
	select {
	case <-ctx.Done():
		return false
	default:
	}

	visited[current] = true
	if g.gtype == Directed {
		recStack[current] = true
	}

	for _, neighbor := range g.adjList[current] {
		select {
		case <-ctx.Done():
			return false
		default:
		}

		if !visited[neighbor] {
			if checkForCyclesDFS(ctx, g, neighbor, visited, recStack, &current) {
				return true
			}
		} else {
			if g.gtype == Directed {
				if recStack[neighbor] {
					return true
				}
			} else if parent != nil && neighbor != *parent {
				return true
			}
		}
	}

	if g.gtype == Directed {
		recStack[current] = false
	}

	return false
}

func reduceMatrix[T comparable](old graphMatrix[T], new graphMatrix[T], index int) {
	var matrixWg sync.WaitGroup
	for i := range old.Size() {
		if i == index {
			continue
		}
		matrixWg.Add(1)
		go func(i int) {
			defer matrixWg.Done()
			for j := range old.Size() {
				if j == index {
					continue
				}
				newI, newJ := i, j
				if i > index {
					newI--
				}
				if j > index {
					newJ--
				}
				new.Set(newI, newJ, old.Get(i, j))
			}
		}(i)
	}
	matrixWg.Wait()
}

func LoadFromDot(path string) (Graph[string], error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}
	ast, err := gographviz.Parse(data)
	if err != nil {
		return nil, err
	}
	gmap := gographviz.NewGraph()
	if err := gographviz.Analyse(ast, gmap); err != nil {
		return nil, err
	}

	gt := Undirected
	if gmap.Directed {
		gt = Directed
	}
	g := NewGraph[string](gt)

	nodeMap := make(map[string]*gographviz.Node, len(gmap.Nodes.Nodes))
	for _, node := range gmap.Nodes.Nodes {
		nodeMap[node.Name] = node
	}

	origNames := make([]string, 0, len(nodeMap))
	for name := range nodeMap {
		origNames = append(origNames, name)
	}
	sort.Strings(origNames)

	origToValue := make(map[string]string, len(origNames))
	for _, orig := range origNames {
		node := nodeMap[orig]
		if lbl, ok := node.Attrs["label"]; ok {
			origToValue[orig] = lbl
		} else {
			origToValue[orig] = orig
		}
	}

	for _, orig := range origNames {
		v := origToValue[orig]
		vCopy := v
		g.AddVertex(&vCopy)
	}

	for _, edge := range gmap.Edges.Edges {
		fromOrig := edge.Src
		toOrig := edge.Dst

		fromVal, ok1 := origToValue[fromOrig]
		toVal, ok2 := origToValue[toOrig]
		if !ok1 || !ok2 {
			continue
		}

		f := fromVal
		t := toVal
		if wstr, ok := edge.Attrs["weight"]; ok {
			if w, err := strconv.ParseFloat(wstr, 64); err == nil {
				g.AddEdge(&f, &t, w)
				continue
			}
		}
		g.AddEdge(&f, &t)
	}

	return g, nil
}

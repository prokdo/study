package graph

import (
	"slices"
	"sync"
	"sync/atomic"
)

func MaghoutMethod[T comparable](g Graph[T], parallelDepth int) *[]T {
	if parallelDepth <= 0 {
		return nil
	}

	graph, ok := g.(*graph[T])
	if !ok {
		return nil
	}

	if graph.cache.AdjMatrix == nil {
		initAdjMatrix(graph)
	}
	n := graph.size
	bestSolution := make([]bool, n)
	var bestCount int32 = -1
	var mu sync.Mutex

	var wg sync.WaitGroup

	total := 1 << parallelDepth
	for mask := range total {
		wg.Add(1)
		go func(mask int) {
			defer wg.Done()

			current := make([]bool, n)
			for i := range parallelDepth {
				if (mask>>i)&1 == 1 {
					current[i] = true
					if !checkSolutionPartial(graph, current, i) {
						return
					}
				}
			}

			backtrack(graph, current, parallelDepth, &bestSolution, &bestCount, &mu, n)
		}(mask)
	}

	wg.Wait()

	var result []T
	for i, bit := range bestSolution {
		if bit {
			result = append(result, graph.indexToVertex[i])
		}
	}
	return &result
}

func backtrack[T comparable](
	g *graph[T], current []bool, i int,
	bestSolution *[]bool, bestCount *int32,
	mu *sync.Mutex, n int,
) {
	if i == n {
		if checkSolution(g, current) {
			count := int32(computeCardinality(current))
			if count > atomic.LoadInt32(bestCount) {
				mu.Lock()
				if count > *bestCount {
					*bestCount = count
					copy(*bestSolution, current)
				}
				mu.Unlock()
			}
		}
		return
	}

	current[i] = false
	backtrack(g, slices.Clone(current), i+1, bestSolution, bestCount, mu, n)

	current[i] = true
	if checkSolutionPartial(g, current, i) {
		backtrack(g, slices.Clone(current), i+1, bestSolution, bestCount, mu, n)
	}
}

func checkSolutionPartial[T comparable](g *graph[T], x []bool, i int) bool {
	for j := range i {
		if x[i] && x[j] && g.cache.AdjMatrix.Get(i, j) {
			return false
		}
	}
	return true
}

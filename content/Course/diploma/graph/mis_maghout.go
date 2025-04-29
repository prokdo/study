package graph

import (
	"context"
	"slices"
	"sync"
	"sync/atomic"
)

func MISMaghout[T comparable](ctx context.Context, g Graph[T], parallelDepth int) []T {
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
		if ctx.Err() != nil {
			return nil
		}
		wg.Add(1)
		go func(mask int) {
			defer wg.Done()
			current := make([]bool, n)
			for i := range parallelDepth {
				if ctx.Err() != nil {
					return
				}
				if (mask>>i)&1 == 1 {
					current[i] = true
					if !checkSolutionPartial(graph, current, i) {
						return
					}
				}
			}
			backtrack(ctx, graph, current, parallelDepth, &bestSolution, &bestCount, &mu, n)
		}(mask)
	}

	wg.Wait()

	if ctx.Err() != nil {
		return nil
	}

	var result []T
	for i, bit := range bestSolution {
		if ctx.Err() != nil {
			return nil
		}
		if bit {
			result = append(result, graph.indexToVertex[i])
		}
	}
	return result
}

func backtrack[T comparable](ctx context.Context, g *graph[T], current []bool, i int, bestSolution *[]bool, bestCount *int32, mu *sync.Mutex, n int) {
	if ctx.Err() != nil {
		return
	}
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
	backtrack(ctx, g, slices.Clone(current), i+1, bestSolution, bestCount, mu, n)

	if ctx.Err() != nil {
		return
	}

	current[i] = true
	if checkSolutionPartial(g, current, i) {
		backtrack(ctx, g, slices.Clone(current), i+1, bestSolution, bestCount, mu, n)
	}
}

func checkSolutionPartial[T comparable](g *graph[T], x []bool, i int) bool {
	if !x[i] {
		return true
	}

	for j := range i {
		if x[i] && x[j] && g.cache.AdjMatrix.Get(i, j) {
			return false
		}
	}
	return true
}

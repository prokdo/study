package graph

import (
	"context"
	"runtime"
	"sync"
)

func MISGreedy[T comparable](ctx context.Context, g Graph[T]) []T {
	graph, ok := g.(*graph[T])
	if !ok {
		return nil
	}

	n := graph.size
	deleted := make([]bool, n)
	var solutionIndices []int

	initAdjMatrix(graph)
	adjMatrix := graph.cache.AdjMatrix

	for {
		if ctx.Err() != nil {
			return nil
		}
		candidates := gatherCandidates(ctx, adjMatrix, deleted)
		if candidates == nil || len(candidates) == 0 {
			break
		}

		bestIdx := findMinDegree(ctx, adjMatrix, candidates, deleted)
		if ctx.Err() != nil {
			return nil
		}
		solutionIndices = append(solutionIndices, bestIdx)
		markDeleted(ctx, adjMatrix, bestIdx, deleted)
	}

	result := make([]T, len(solutionIndices))
	for i, idx := range solutionIndices {
		if ctx.Err() != nil {
			return nil
		}
		result[i] = graph.indexToVertex[idx]
	}
	return result
}

func gatherCandidates(ctx context.Context, adj *adjMatrix, deleted []bool) []int {
	n := adj.Size()
	chunkSize := (n + runtime.NumCPU() - 1) / runtime.NumCPU()
	candidates := make(chan int, n)
	var wg sync.WaitGroup

	for i := 0; i < n; i += chunkSize {
		end := min(i+chunkSize, n)
		wg.Add(1)
		go func(start, end int) {
			defer wg.Done()
			for j := start; j < end; j++ {
				if ctx.Err() != nil {
					return
				}
				if !deleted[j] {
					candidates <- j
				}
			}
		}(i, end)
	}

	go func() {
		wg.Wait()
		close(candidates)
	}()

	var result []int
	for idx := range candidates {
		if ctx.Err() != nil {
			return nil
		}
		result = append(result, idx)
	}
	return result
}

func markDeleted(ctx context.Context, adj *adjMatrix, idx int, deleted []bool) {
	deleted[idx] = true
	var wg sync.WaitGroup
	chunkSize := (adj.Size() + runtime.NumCPU() - 1) / runtime.NumCPU()

	for i := 0; i < adj.Size(); i += chunkSize {
		end := min(i+chunkSize, adj.Size())
		wg.Add(1)
		go func(start, end int) {
			defer wg.Done()
			for j := start; j < end; j++ {
				if ctx.Err() != nil {
					return
				}
				if adj.Get(idx, j) {
					deleted[j] = true
				}
			}
		}(i, end)
	}
	wg.Wait()
}

func findMinDegree(ctx context.Context, adj *adjMatrix, candidates []int, deleted []bool) int {
	type result struct {
		idx    int
		degree int
	}

	ch := make(chan result, len(candidates))
	var wg sync.WaitGroup

	for _, idx := range candidates {
		wg.Add(1)
		go func(i int) {
			defer wg.Done()
			degree := 0
			for j := 0; j < adj.Size(); j++ {
				if ctx.Err() != nil {
					return
				}
				if adj.Get(i, j) && !deleted[j] {
					degree++
				}
			}
			ch <- result{i, degree}
		}(idx)
	}

	go func() {
		wg.Wait()
		close(ch)
	}()

	best, ok := <-ch
	if !ok {
		return -1
	}
	for res := range ch {
		if ctx.Err() != nil {
			return best.idx
		}
		if res.degree < best.degree {
			best = res
		}
	}
	return best.idx
}

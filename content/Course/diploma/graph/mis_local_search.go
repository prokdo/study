package graph

import (
	"context"
	"runtime"
	"slices"
	"sync"
)

func MISLocalSearch[T comparable](ctx context.Context, g Graph[T], genome []bool, localIters int) []bool {
	graph, ok := g.(*graph[T])
	if !ok {
		return nil
	}

	initAdjMatrix(graph)
	adj := graph.cache.AdjMatrix
	best := slices.Clone(genome)
	bestFitness := computeCardinality(best)
	n := len(genome)

	for range localIters {
		if ctx.Err() != nil {
			return nil
		}
		improved := false
		var mu sync.Mutex

		chunkSize := (n + runtime.NumCPU() - 1) / runtime.NumCPU()
		var wg sync.WaitGroup

		for i := 0; i < n; i += chunkSize {
			end := min(i+chunkSize, n)
			wg.Add(1)
			go func(start, end int) {
				defer wg.Done()
				localImprove := false
				localBest := slices.Clone(best)
				localFitness := bestFitness

				for j := start; j < end; j++ {
					if ctx.Err() != nil {
						return
					}
					temp := slices.Clone(localBest)
					temp[j] = !temp[j]
					if checkSolutionFast(adj, temp, j) {
						currentFitness := computeCardinality(temp)
						if currentFitness > localFitness {
							localBest = temp
							localFitness = currentFitness
							localImprove = true
						}
					}
				}

				mu.Lock()
				if localImprove && localFitness > bestFitness {
					best = localBest
					bestFitness = localFitness
					improved = true
				}
				mu.Unlock()
			}(i, end)
		}

		wg.Wait()
		if !improved {
			break
		}
	}
	return best
}

func checkSolutionFast(adj *adjMatrix, genome []bool, flippedIdx int) bool {
	for j := range adj.Size() {
		if genome[j] && adj.Get(flippedIdx, j) {
			return false
		}
	}
	return true
}

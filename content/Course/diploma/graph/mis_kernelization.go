package graph

import (
	"sync"
)

type candidateType int

const (
	isolated = iota
	pendant
)

func KernelizationMethod[T comparable](g Graph[T]) ([]T, Graph[T]) {
	graph, ok := g.(*graph[T])
	if !ok {
		return nil, g
	}

	var solution []T
	changed := true

	for changed {
		changed = false

		keys := make([]T, 0, len(graph.adjList))
		for v := range graph.adjList {
			keys = append(keys, v)
		}

		var mu sync.Mutex
		var candidate T
		var candidateType candidateType
		candidateFound := false

		var wg sync.WaitGroup
		for _, v := range keys {
			wg.Add(1)
			go func(v T) {
				defer wg.Done()
				degree := len(graph.adjList[v])
				mu.Lock()
				defer mu.Unlock()
				if degree == 0 {
					candidate = v
					candidateType = isolated
					candidateFound = true
				} else if degree == 1 && candidateType != isolated {
					candidate = v
					candidateType = pendant
					candidateFound = true
				}
			}(v)
		}
		wg.Wait()

		if candidateFound {
			if candidateType == isolated {
				solution = append(solution, candidate)
				graph.RemoveVertex(&candidate)
				changed = true
			} else if candidateType == pendant {
				solution = append(solution, candidate)
				neighbor := graph.adjList[candidate][0]
				graph.RemoveVertex(&candidate)
				graph.RemoveVertex(&neighbor)
				changed = true
			}
		}
	}

	return solution, g
}

package graph

import "context"

func MISGreedySearch[T comparable](ctx context.Context, g Graph[T], localIters int) []T {
	graph, ok := g.(*graph[T])
	if !ok {
		return nil
	}

	greedySolution := MISGreedy(ctx, graph)
	if greedySolution == nil || ctx.Err() != nil {
		return nil
	}

	n := graph.size
	genome := make([]bool, n)
	for _, v := range greedySolution {
		if ctx.Err() != nil {
			return nil
		}
		genome[graph.vertexToIndex[v]] = true
	}

	improved := MISLocalSearch(ctx, g, genome, localIters)
	if improved == nil || ctx.Err() != nil {
		return nil
	}

	result := make([]T, 0, computeCardinality(improved))
	for i, inc := range improved {
		if ctx.Err() != nil {
			return nil
		}
		if inc {
			result = append(result, graph.indexToVertex[i])
		}
	}
	return result
}

package graph

import "slices"

func checkSolution[T comparable](g *graph[T], x []bool) bool {
	for i := range g.size {
		if x[i] {
			for j := range g.size {
				if i != j && x[j] && g.cache.AdjMatrix.Get(i, j) {
					return false
				}
			}
		}
	}
	return true
}

func computeCardinality(x []bool) int {
	sum := 0
	for _, v := range x {
		if v {
			sum++
		}
	}
	return sum
}

func ComputeF1Factor[T comparable](sample, solution []T) float64 {
	if len(sample) == 0 || len(solution) == 0 {
		return 0
	}

	count := 0
	for _, v := range solution {
		if slices.Contains(sample, v) {
			count++
		}
	}

	precision := float64(count) / float64(len(solution))
	recall := float64(count) / float64(len(sample))

	denominator := precision + recall
	if denominator == 0 {
		return 0
	}

	return 2 * precision * recall / denominator
}

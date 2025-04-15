package graph

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

func findConflicts[T comparable](g *graph[T], genome []bool) int {
	conflicts := 0
	for i, active := range genome {
		if active {
			for neighbor := range g.cache.AdjMatrix.GetRow(i) {
				if genome[neighbor] && neighbor > i {
					conflicts++
				}
			}
		}
	}
	return conflicts
}

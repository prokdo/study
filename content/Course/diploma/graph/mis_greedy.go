package graph

// ImprovedGreedyMIS ищет максимальное независимое множество,
// выбирая вершину с наименьшей «первичной степенью», а при равенстве —
// сравнивая вторичную, третичную и т.д. степени в соответствии с описанными правилами.
func ImprovedGreedyMIS[T comparable](g Graph[T]) *[]T {
	// Приводим интерфейс к конкретной реализации.
	graphImpl, ok := g.(*graph[T])
	if !ok {
		return nil
	}

	n := graphImpl.size

	// Множество (слайс) вершин, которые уже удалены из рассмотрения.
	// В Go можно использовать map[int]bool, но здесь, для наглядности,
	// пусть будет слайс (мы будем ставить true для «удалённых» вершин).
	deleted := make([]bool, n)

	var solution []T

	// Пока в графе остаются не удалённые вершины
	for {
		candidates := gatherCandidates(graphImpl, deleted)
		if len(candidates) == 0 {
			break
		}

		// Выбираем вершину с наименьшей «первичной степени»,
		// при равенстве — с наибольшей «вторичной» (если правило таково),
		// и так далее, согласно описанию.
		bestIdx := candidates[0]
		for _, idx := range candidates[1:] {
			if compareVertices(graphImpl, bestIdx, idx, deleted) < 0 {
				// Если в compareVertices вернулась < 0, значит idx лучше (по правилам).
				bestIdx = idx
			}
		}

		// Добавляем вершину в решение.
		solution = append(solution, graphImpl.indexToVertex[bestIdx])

		// Удаляем вершину и всех её соседей.
		deleteVertexAndNeighbors(graphImpl, bestIdx, deleted)
	}

	return &solution
}

// gatherCandidates возвращает все не удалённые вершины (их индексы).
func gatherCandidates[T comparable](g *graph[T], deleted []bool) []int {
	var result []int
	for i := 0; i < len(deleted); i++ {
		if !deleted[i] {
			result = append(result, i)
		}
	}
	return result
}

// compareVertices сравнивает две вершины v1, v2 по описанному правилу.
// Возвращает > 0, если v1 «лучше», < 0, если v2 «лучше», и 0, если равны.
// Согласно описанию, при равенстве n-й степеней, смотрим (n+1)-ю степень:
//   - если (n+1) чётно, берём вершину с большей (n+1)-й степенью
//   - если (n+1) нечётно, берём вершину с меньшей
//
// Для упрощения реализуем рекурсивное сравнение до некоторого предела
// (например, до n, или пока не найдём различие).
func compareVertices[T comparable](g *graph[T], v1, v2 int, deleted []bool) int {
	maxDepth := len(deleted) // в худшем случае
	for depth := 1; depth <= maxDepth; depth++ {
		d1 := computeDegreeN(g, v1, depth, deleted)
		d2 := computeDegreeN(g, v2, depth, deleted)
		if d1 != d2 {
			// Нашли различие
			if depth == 1 {
				// На 1-м уровне (первичная степень) выбираем вершину с МЕНЬШЕЙ степенью.
				return compareInt(d1, d2) // > 0 => v1 больше, значит v2 лучше
			} else {
				// depth > 1
				if (depth)%2 == 0 {
					// чётная глубина => выбираем вершину с БОЛЬШЕЙ степенью
					// т.е. если d1 > d2 => v1 лучше => return < 0 (чтобы v1 остался)
					if d1 > d2 {
						return 1 // v1 лучше
					} else {
						return -1 // v2 лучше
					}
				} else {
					// нечётная глубина => выбираем вершину с МЕНЬШЕЙ степенью
					if d1 < d2 {
						return 1 // v1 лучше
					} else {
						return -1 // v2 лучше
					}
				}
			}
		}
	}
	// Если все глубины до maxDepth совпали — считаем, что равны.
	return 0
}

// computeDegreeN вычисляет «n-ю степень» вершины idx, то есть
// сумму степеней (или кол-во вершин?) на расстоянии n.
// Здесь для простоты предполагаем, что "n-я степень" =
// кол-во соседей на расстоянии (n-1)?
// Но по описанию вам, возможно, нужно иначе.
// Можно реализовать BFS на n шагов и посчитать сумму степеней.
// Ниже — упрощённый вариант: depth=1 => обычная степень, depth=2 => сумма степеней соседей и т.д.
func computeDegreeN[T comparable](g *graph[T], idx int, depth int, deleted []bool) int {
	if depth == 1 {
		// Обычная степень (не удалённые соседи)
		v := g.indexToVertex[idx]
		count := 0
		for _, nb := range g.adjList[v] {
			nbIdx := g.vertexToIndex[nb]
			if !deleted[nbIdx] {
				count++
			}
		}
		return count
	}
	// Для depth>1 рекурсивно: сумма computeDegreeN для соседей, depth-1
	v := g.indexToVertex[idx]
	sum := 0
	for _, nb := range g.adjList[v] {
		nbIdx := g.vertexToIndex[nb]
		if !deleted[nbIdx] {
			sum += computeDegreeN(g, nbIdx, depth-1, deleted)
		}
	}
	return sum
}

// deleteVertexAndNeighbors удаляет вершину idx и всех её соседей,
// помечая их в deleted как true.
func deleteVertexAndNeighbors[T comparable](g *graph[T], idx int, deleted []bool) {
	deleted[idx] = true
	v := g.indexToVertex[idx]
	for _, nb := range g.adjList[v] {
		nbIdx := g.vertexToIndex[nb]
		deleted[nbIdx] = true
	}
}

// compareInt — вспомогательная функция для сравнения int:
// возвращает >0, если a < b (т.к. мы хотим «меньшая степень — лучше» на 1-м уровне)
func compareInt(a, b int) int {
	// Если a < b => a лучше => возвращаем >0,
	// чтобы вызывать return < 0 в месте, где мы используем compareVertices
	if a < b {
		return 1
	} else if a > b {
		return -1
	}
	return 0
}

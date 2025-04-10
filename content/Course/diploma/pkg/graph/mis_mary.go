package graph

// MaryMethod ищет максимальное независимое множество в графе с использованием точного перебора.
// Возвращает слайс вершин (из indexToVertex), входящих в найденное независимое множество.
func MaryMethod[T comparable](g *graph[T]) []T {
	// Если матрица смежности ещё не инициализирована, инициализируем её.
	if g.cache.AdjMatrix == nil {
		initAdjMatrix(g)
	}
	n := g.size

	// bestSolution[i] = 1, если вершина с индексом i входит в текущее лучшее множество.
	bestSolution := make([]int, n)
	bestCount := -1
	current := make([]int, n)

	// Рекурсивная функция для перебора всех вариантов.
	var backtrack func(i int)
	backtrack = func(i int) {
		if i == n {
			// Если мы назначили значение для всех вершин, проверяем, является ли решение допустимым.
			if isValidSolution(g, current) {
				count := countOnes(current)
				if count > bestCount {
					bestCount = count
					// Сохраняем копию текущего решения.
					bestSolution = make([]int, n)
					copy(bestSolution, current)
				}
			}
			return
		}

		// Вариант 1: не включать вершину i.
		current[i] = 0
		backtrack(i + 1)

		// Вариант 2: включить вершину i, если частичная проверка не выявила конфликт.
		current[i] = 1
		if isPartialValid(g, current, i) {
			backtrack(i + 1)
		}
		// Откатываем выбор
		current[i] = 0
	}

	backtrack(0)

	// Формируем результат: берем из indexToVertex те вершины, для которых bestSolution[i]==1.
	var result []T
	for i, bit := range bestSolution {
		if bit == 1 {
			result = append(result, g.indexToVertex[i])
		}
	}
	return result
}

// isValidSolution проверяет, что для полного решения x (слайс длины n)
// для любой пары вершин, включенных в решение (x[i]==1 и x[j]==1),
// между ними отсутствует ребро (т.е. g.cache.AdjMatrix.Get(i,j)==false).
func isValidSolution[T comparable](g *graph[T], x []int) bool {
	for i := range g.size {
		if x[i] == 1 {
			for j := 0; j < g.size; j++ {
				if i != j && x[j] == 1 && g.cache.AdjMatrix.Get(i, j) {
					return false
				}
			}
		}
	}
	return true
}

// isPartialValid проверяет, что выбор для вершин 0..i не нарушает условие независимости.
// При этом проверяются только пары (i,j) для j < i.
func isPartialValid[T comparable](g *graph[T], x []int, i int) bool {
	for j := range i {
		if x[i] == 1 && x[j] == 1 && g.cache.AdjMatrix.Get(i, j) {
			return false
		}
	}
	return true
}

// countOnes считает количество единиц в слайсе x.
func countOnes(x []int) int {
	sum := 0
	for _, v := range x {
		sum += v
	}
	return sum
}

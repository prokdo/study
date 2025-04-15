package graph

// HybridMethod сначала выполняет редукцию графа методом исключения подграфов,
// а затем применяет генетический алгоритм для поиска независимого множества в оставшемся графе.
// Параметры:
// - g: исходный граф
// - popSize, gens, mutRate, penalty, localIters, elitismCount: параметры генетического алгоритма
// - parallelDepth: параметр для метода Maghout (если используется отдельно)
// Возвращает объединённое решение (слайс вершин), состоящее из вершин, найденных в ходе редукции,
// и из решения генетического алгоритма, применённого к редуцированному графу.
func HybridMethod[T comparable](
	g Graph[T],
	populationSize, generations int,
	mutationRate, penalty float64,
	localIters, elitismCount int,
) *[]T {
	kernelSolution, reducedGraph := KernelizationMethod(g)

	if reducedGraph.Size() == 0 {
		return &kernelSolution
	}

	geneticSolution := GeneticMethod(reducedGraph, populationSize, generations, mutationRate, penalty, localIters, elitismCount)

	combined := append(kernelSolution, (*geneticSolution)...)

	return &combined
}

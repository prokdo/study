package graph

import (
	"math/rand"
	"runtime"
	"sort"
	"sync"
	"time"
)

type individual struct {
	genome  []bool
	fitness float64
}

func GeneticMethod[T comparable](
	g Graph[T],
	populationSize, generations int,
	mutationRate, penalty float64,
	localIters, elitismCount int,
) *[]T {
	graph, ok := g.(*graph[T])
	if !ok || mutationRate < 0 || mutationRate > 1 || populationSize <= 0 || generations <= 0 {
		return nil
	}

	if graph.cache.AdjMatrix == nil {
		initAdjMatrix(graph)
	}
	n := graph.size

	rnd := rand.New(rand.NewSource(time.Now().UnixNano()))
	randPool := &sync.Pool{New: func() any { return rand.New(rand.NewSource(rnd.Int63())) }}

	population := make([]individual, populationSize)
	var wg sync.WaitGroup
	wg.Add(populationSize)
	for i := range population {
		go func(i int) {
			defer wg.Done()
			genome := randomGenome(n, rnd)
			localSearch(graph, genome, penalty, localIters)
			population[i] = individual{
				genome:  genome,
				fitness: fitness(graph, genome, penalty),
			}
		}(i)
	}
	wg.Wait()

	bestIndividual := population[0]
	for _, ind := range population {
		if ind.fitness > bestIndividual.fitness {
			bestIndividual = ind
		}
	}

	workerCount := runtime.NumCPU()

	for range generations {
		sort.Slice(population, func(i, j int) bool {
			return population[i].fitness > population[j].fitness
		})

		jobs := make(chan int, populationSize)
		results := make(chan individual, populationSize)

		newPopulation := make([]individual, 0, populationSize)
		newPopulation = append(newPopulation, population[:min(elitismCount, populationSize)]...)

		for i := len(newPopulation); i < populationSize; i++ {
			jobs <- i
		}
		close(jobs)

		var processWg sync.WaitGroup
		processWg.Add(workerCount)
		for range workerCount {
			go func() {
				defer processWg.Done()
				for range jobs {
					r := randPool.Get().(*rand.Rand)
					defer randPool.Put(r)

					parent1 := selection(population, 3, r)
					parent2 := selection(population, 3, r)
					child := crossover(parent1.genome, parent2.genome, r)
					child = mutate(child, mutationRate, r)
					localSearch(graph, child, penalty, localIters)
					results <- individual{
						genome:  child,
						fitness: fitness(graph, child, penalty),
					}
				}
			}()
		}
		processWg.Wait()
		close(results)

		for res := range results {
			newPopulation = append(newPopulation, res)
		}

		population = newPopulation
		for _, ind := range population {
			if ind.fitness > bestIndividual.fitness {
				bestIndividual = ind
			}
		}
	}

	var result []T
	for i, selected := range bestIndividual.genome {
		if selected {
			result = append(result, graph.indexToVertex[i])
		}
	}
	return &result
}

func randomGenome(n int, rnd *rand.Rand) []bool {
	genome := make([]bool, n)
	for i := range genome {
		genome[i] = rnd.Intn(2) == 1
	}
	return genome
}

func crossover(p1, p2 []bool, rnd *rand.Rand) []bool {
	n := len(p1)
	child := make([]bool, n)
	point := rnd.Intn(n)
	copy(child[:point], p1[:point])
	copy(child[point:], p2[point:])
	return child
}

func mutate(genome []bool, rate float64, rnd *rand.Rand) []bool {
	for i := range genome {
		if rnd.Float64() < rate {
			genome[i] = !genome[i]
		}
	}
	return genome
}

func selection(pop []individual, k int, rnd *rand.Rand) individual {
	best := pop[rnd.Intn(len(pop))]
	for i := 1; i < k; i++ {
		candidate := pop[rnd.Intn(len(pop))]
		if candidate.fitness > best.fitness {
			best = candidate
		}
	}
	return best
}

func localSearch[T comparable](g *graph[T], genome []bool, penalty float64, iters int) {
	bestFit := fitness(g, genome, penalty)
	n := len(genome)

	for iter := 0; iter < iters; iter++ {
		improved := false
		for j := 0; j < n; j++ {
			if !genome[j] {
				continue
			}

			original := genome[j]
			genome[j] = false

			// Проверяем ВСЕ конфликты в геноме
			currentConflicts := findConflicts(g, genome)
			newFit := fitness(g, genome, penalty)

			// Обновляем решение только если нет конфликтов и фитнес улучшился
			if newFit > bestFit && currentConflicts == 0 {
				bestFit = newFit
				improved = true
			} else {
				genome[j] = original // Откатываем изменение
			}
		}
		if !improved {
			break
		}
	}
}

func countSelected(genome []bool) int {
	count := 0
	for _, g := range genome {
		if g {
			count++
		}
	}
	return count
}

func countConflicts[T comparable](g *graph[T], genome []bool, idx int) int {
	conflicts := 0
	for neighbor := range g.cache.AdjMatrix.GetRow(idx) {
		if genome[neighbor] {
			conflicts++
		}
	}
	return conflicts
}

func fitness[T comparable](g *graph[T], genome []bool, penalty float64) float64 {
	selected := countSelected(genome)
	conflicts := findConflicts(g, genome) // Используем полный подсчет конфликтов
	return float64(selected) - penalty*float64(conflicts)
}

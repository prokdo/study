package main

import (
	"fmt"
	"time"

	"graphmis/graph"
	"graphmis/internal/utils"
)

func main() {
	g := graph.NewGraph[string](graph.Directed)

	A := "A"
	B := "B"
	C := "C"
	D := "D"
	E := "E"
	F := "F"
	G := "G"
	H := "H"

	g.AddEdge(&A, &B)
	g.AddEdge(&A, &E)
	g.AddEdge(&A, &F)
	g.AddEdge(&B, &A)
	g.AddEdge(&B, &C)
	g.AddEdge(&B, &D)
	g.AddEdge(&D, &E)
	g.AddEdge(&E, &D)
	g.AddEdge(&E, &F)
	g.AddEdge(&F, &E)
	g.AddEdge(&F, &G)
	g.AddEdge(&G, &H)
	g.AddEdge(&H, &D)
	g.AddEdge(&H, &F)

	fmt.Println(g.String())

	g.ExportToFile("graph.dot", true)

	fmt.Println(g.IsAcyclic())
	fmt.Println(g.HasLoop())

	var result any

	start := time.Now()
	result = graph.MaghoutMethod(g, 3)
	elapsed := time.Since(start)
	fmt.Printf("%d (ms): %v\n", elapsed.Milliseconds(), result)

	start = time.Now()
	result = graph.GeneticMethod(g, 100, 500, 0.1, 1.0, 15, 10)
	// result = graph.ImprovedGreedyMIS(g)
	elapsed = time.Since(start)
	fmt.Printf("%d (ms): %v\n", elapsed.Milliseconds(), result)

	newG := utils.GenerateGraph(graph.Directed, 100, 100)
	newG.ExportToFile("graph.dot", true)

	start = time.Now()
	result = graph.MaghoutMethod(newG, 3)
	elapsed = time.Since(start)
	fmt.Printf("%d (ms): %v\n", elapsed.Milliseconds(), result)

	start = time.Now()
	result = graph.GeneticMethod(newG, 100, 500, 0.1, 1.0, 15, 10)
	// result = graph.ImprovedGreedyMIS(newG)
	elapsed = time.Since(start)
	fmt.Printf("%d (ms): %v\n", elapsed.Milliseconds(), result)
}

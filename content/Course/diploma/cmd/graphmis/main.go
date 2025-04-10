package main

import (
	"fmt"

	"graphmis/pkg/graph"
)

func main() {
	fmt.Println("Hello world!")

	graph := graph.NewGraph[int](graph.Directed)

	first := 1
	second := 2
	third := 3

	graph.AddVertex(&first)
	graph.AddVertex(&second)
	graph.AddVertex(&third)

	graph.AddEdge(&first, &second)
	graph.AddEdge(&second, &third)
	// graph.AddEdge(&third, &first)

	fmt.Println(graph.String())

	graph.ExportToFile("graph.dot", true)

	fmt.Println(graph.IsAcyclic())
}

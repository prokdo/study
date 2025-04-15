package utils

import (
	"math/rand"
	"time"

	"graphmis/graph"
)

func GenerateGraph(gt graph.GraphType, minVertices, maxVertices int) graph.Graph[int] {
	rand.New(rand.NewSource(time.Now().UnixNano()))

	if minVertices > maxVertices || minVertices < 0 {
		return nil
	}
	var n int
	if minVertices == maxVertices {
		n = minVertices
	} else {
		n = minVertices + rand.Intn(maxVertices-minVertices+1)
	}

	g := graph.NewGraph[int](gt)

	edgeProb := 0.5

	for i := range n {
		for j := range n {
			if i == j {
				continue
			}
			if rand.Float64() < edgeProb {
				from := i + 1
				to := j + 1
				g.AddEdge(&from, &to)
				if gt == graph.Undirected {
					g.AddEdge(&to, &from)
				}
			}
		}
	}

	return g
}

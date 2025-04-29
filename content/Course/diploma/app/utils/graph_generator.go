package utils

import (
	"fmt"
	"math/rand"
	"time"

	"graphmis/graph"
)

func GenerateGraph(gt graph.GraphType, minVertices, maxVertices int, edgeProb float64) graph.Graph[string] {
	rand.New(rand.NewSource(time.Now().UnixNano()))

	if minVertices > maxVertices || minVertices < 0 {
		return nil
	}

	if edgeProb < 0 || edgeProb > 1 {
		return nil
	}

	var n int
	if minVertices == maxVertices {
		n = minVertices
	} else {
		n = minVertices + rand.Intn(maxVertices-minVertices+1)
	}

	g := graph.NewGraph[string](gt)

	for i := range n {
		for j := range n {
			if i == j {
				continue
			}
			if rand.Float64() < edgeProb {
				from := fmt.Sprint(i + 1)
				to := fmt.Sprint(j + 1)
				g.AddEdge(&from, &to)
			}
		}
	}

	return g
}

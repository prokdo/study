package ru.prokdo.ksSolver.solution;

import java.util.ArrayList;

import ru.prokdo.ksSolver.knapsack.Item;
import ru.prokdo.ksSolver.knapsack.Knapsack;

public final class Solver {
    private Solver() {
    }

    public static Knapsack[][] invoke(int capacity, ArrayList<Item> items) {
        Knapsack[][] matrix = new Knapsack[items.size() + 1][capacity + 1];

        for (int i = 0; i < items.size() + 1; i++) {
            for (int j = 0; j < capacity + 1; j++)
                if (i == 0 || j == 0) matrix[i][j] = new Knapsack(capacity);
                else if (i == 1)
                    if (items.get(0).getWeight() <= j) {
                        matrix[i][j] = new Knapsack(capacity);
                        matrix[i][j].addItem(items.get(0));
                    }
                    else matrix[i][j] = new Knapsack(capacity);
                else
                    if (items.get(i - 1).getWeight() > j) matrix[i][j] = matrix[i - 1][j];
                    else {
                        int newPrice = items.get(i - 1).getPrice()
                                + matrix[i - 1][j - items.get(i - 1).getWeight()].getPrice();

                        if (matrix[i - 1][j].getPrice() > newPrice) matrix[i][j] = matrix[i - 1][j];
                        else {
                            matrix[i][j] = new Knapsack(capacity);

                            for (var item : matrix[i - 1][j - items.get(i - 1).getWeight()].getItems())
                                matrix[i][j].addItem(item);

                            matrix[i][j].addItem(items.get(i - 1));
                        }
                    }
        }

        return matrix;
    }
}

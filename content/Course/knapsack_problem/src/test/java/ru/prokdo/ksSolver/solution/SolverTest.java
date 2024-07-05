package ru.prokdo.ksSolver.solution;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ru.prokdo.ksSolver.knapsack.Item;
import ru.prokdo.ksSolver.knapsack.Knapsack;

public final class SolverTest {

    @Test
    public void testEmptyItems() {
        var capacity = 10;
        var items = new ArrayList<Item>(0);

        Knapsack[][] result = Solver.invoke(capacity, items);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(capacity + 1, result[0].length);
        for (var i = 0; i <= capacity; i++) {
            assertEquals(0, result[0][i].getWeight());
            assertEquals(0, result[0][i].getPrice());
            assertTrue(result[0][i].getItems().isEmpty());
        }
    }

    @Test
    public void testSingleItemFits() {
        var capacity = 10;
        var item = new Item("Item1", 5, 10);
        var items = new ArrayList<Item>(1);
        items.add(item);

        Knapsack[][] result = Solver.invoke(capacity, items);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(capacity + 1, result[1].length);

        for (var i = 0; i <= capacity; i++) {
            if (i >= item.getWeight()) {
                assertEquals(item.getWeight(), result[1][i].getWeight());
                assertEquals(item.getPrice(), result[1][i].getPrice());
                assertTrue(result[1][i].contains(item));
            }
            else {
                assertEquals(0, result[1][i].getWeight());
                assertEquals(0, result[1][i].getPrice());
                assertTrue(result[1][i].getItems().isEmpty());
            }
        }
    }

    @Test
    public void testSingleItemDoesNotFit() {
        var capacity = 4;
        var item = new Item("Item1", 5, 10);
        var items = new ArrayList<Item>(1);
        items.add(item);

        Knapsack[][] result = Solver.invoke(capacity, items);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(capacity + 1, result[1].length);

        for (var i = 0; i <= capacity; i++) {
            assertEquals(0, result[1][i].getWeight());
            assertEquals(0, result[1][i].getPrice());
            assertTrue(result[1][i].getItems().isEmpty());
        }
    }

    @Test
    public void testMultipleItems() {
        int capacity = 10;
        var item1 = new Item("Item1", 5, 10);
        var item2 = new Item("Item2", 3, 7);
        var item3 = new Item("Item3", 4, 5);
        var items = new ArrayList<Item>(3);
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Knapsack[][] result = Solver.invoke(capacity, items);

        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals(capacity + 1, result[3].length);

        Knapsack optimalKnapsack = result[3][capacity];
        assertTrue(optimalKnapsack.contains(item1));
        assertTrue(optimalKnapsack.contains(item2));
        assertEquals(8, optimalKnapsack.getWeight());
        assertEquals(17, optimalKnapsack.getPrice());
    }

    @Test
    public void testDuplicateItems() {
        var capacity = 10;
        var item = new Item("Item1", 5, 10);
        var items = new ArrayList<Item>(2);
        items.add(item);
        items.add(item);

        Knapsack[][] result = Solver.invoke(capacity, items);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(capacity + 1, result[2].length);

        Knapsack optimalKnapsack = result[2][capacity];
        assertTrue(optimalKnapsack.contains(item));
        assertEquals(2 * item.getWeight(), optimalKnapsack.getWeight());
        assertEquals(2 * item.getPrice(), optimalKnapsack.getPrice());
    }
}

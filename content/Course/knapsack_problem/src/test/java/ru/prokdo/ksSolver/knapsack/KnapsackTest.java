package ru.prokdo.ksSolver.knapsack;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class KnapsackTest {

    private Knapsack knapsack;

    @BeforeEach
    public void setUp() {
        knapsack = new Knapsack(50);
    }

    @Test
    public void testKnapsackConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Knapsack(0));
        assertThrows(IllegalArgumentException.class, () -> new Knapsack(-10));

        var validKnapsack = new Knapsack(10);
        assertEquals(10, validKnapsack.getCapacity());
        assertEquals(0, validKnapsack.getWeight());
        assertEquals(0, validKnapsack.getPrice());
        assertTrue(validKnapsack.getItems().isEmpty());
    }

    @Test
    public void testAddItem() {
        var mockItem = mock(Item.class);
        when(mockItem.getWeight()).thenReturn(10);
        when(mockItem.getPrice()).thenReturn(100);

        assertTrue(knapsack.addItem(mockItem));
        assertEquals(10, knapsack.getWeight());
        assertEquals(100, knapsack.getPrice());
        assertTrue(knapsack.contains(mockItem));

        var heavyItem = mock(Item.class);
        when(heavyItem.getWeight()).thenReturn(50);

        assertFalse(knapsack.addItem(heavyItem));
    }

    @Test
    public void testAddItems() {
        var mockItem1 = mock(Item.class);
        when(mockItem1.getWeight()).thenReturn(10);
        when(mockItem1.getPrice()).thenReturn(100);

        var mockItem2 = mock(Item.class);
        when(mockItem2.getWeight()).thenReturn(20);
        when(mockItem2.getPrice()).thenReturn(200);

        var items = new ArrayList<Item>();
        items.add(mockItem1);
        items.add(mockItem2);
        assertTrue(knapsack.addItems(items));
        assertEquals(30, knapsack.getWeight());
        assertEquals(300, knapsack.getPrice());

        var heavyItem = mock(Item.class);
        when(heavyItem.getWeight()).thenReturn(50);

        var heavyItems = new ArrayList<Item>();
        heavyItems.add(heavyItem);
        assertFalse(knapsack.addItems(heavyItems));
    }

    @Test
    public void testPutItems() {
        var mockItem1 = mock(Item.class);
        when(mockItem1.getWeight()).thenReturn(10);
        when(mockItem1.getPrice()).thenReturn(100);

        var mockItem2 = mock(Item.class);
        when(mockItem2.getWeight()).thenReturn(20);
        when(mockItem2.getPrice()).thenReturn(200);

        var items = new ArrayList<Item>();
        items.add(mockItem1);
        items.add(mockItem2);
        assertTrue(knapsack.putItems(items));
        assertEquals(30, knapsack.getWeight());
        assertEquals(300, knapsack.getPrice());

        var heavyItem = mock(Item.class);
        when(heavyItem.getWeight()).thenReturn(60);

        var heavyItems = new ArrayList<Item>();
        heavyItems.add(heavyItem);
        assertFalse(knapsack.putItems(heavyItems));
    }

    @Test
    public void testRemoveItem() {
        var mockItem = mock(Item.class);
        when(mockItem.getWeight()).thenReturn(10);
        when(mockItem.getPrice()).thenReturn(100);

        knapsack.addItem(mockItem);
        assertTrue(knapsack.removeItem(mockItem));
        assertEquals(0, knapsack.getWeight());
        assertEquals(0, knapsack.getPrice());
        assertFalse(knapsack.contains(mockItem));

        assertFalse(knapsack.removeItem(mockItem));
    }

    @Test
    public void testExtractItems() {
        var mockItem1 = mock(Item.class);
        when(mockItem1.getWeight()).thenReturn(10);
        when(mockItem1.getPrice()).thenReturn(100);

        var mockItem2 = mock(Item.class);
        when(mockItem2.getWeight()).thenReturn(20);
        when(mockItem2.getPrice()).thenReturn(200);

        var items = new ArrayList<Item>();
        items.add(mockItem1);
        items.add(mockItem2);
        knapsack.putItems(items);

        var extractedItems = knapsack.extractItems();
        assertEquals(2, extractedItems.size());
        assertTrue(knapsack.isEmpty());
    }

    @Test
    public void testExtractItem() {
        var mockItem = mock(Item.class);
        when(mockItem.getWeight()).thenReturn(10);
        when(mockItem.getPrice()).thenReturn(100);

        knapsack.addItem(mockItem);
        var extractedItem = knapsack.extractItem();
        assertEquals(mockItem, extractedItem);
        assertTrue(knapsack.isEmpty());
    }

    @Test
    public void testClear() {
        var mockItem = mock(Item.class);
        when(mockItem.getWeight()).thenReturn(10);
        when(mockItem.getPrice()).thenReturn(100);

        knapsack.addItem(mockItem);
        knapsack.clear();
        assertTrue(knapsack.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(knapsack.isEmpty());

        var mockItem = mock(Item.class);
        when(mockItem.getWeight()).thenReturn(10);
        knapsack.addItem(mockItem);

        assertFalse(knapsack.isEmpty());
    }
}

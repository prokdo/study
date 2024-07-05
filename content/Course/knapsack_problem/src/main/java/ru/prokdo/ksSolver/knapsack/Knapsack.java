package ru.prokdo.ksSolver.knapsack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Knapsack implements Iterable<Item> {
    private final int capacity;
    private int weight;
    private int price;
    private ArrayList<Item> items;

    public Knapsack(int capacity) {
        if (capacity <= 0 ) throw new IllegalArgumentException("Capacity cannot be zero or negative");

        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public ArrayList<Item> getItems() {
        var result = new ArrayList<Item>(items.size());
        for (var item : items) result.add(item.clone());

        return result;
    }

    public boolean addItems(Collection<Item> items) {
        int totalWeight = 0;
        for (var item : items) totalWeight += item.getWeight();

        if (weight + totalWeight > capacity) return false;

        for (var item: items) {
            this.items.add(item);
            weight += item.getWeight();
            price += item.getPrice();
        }

        return true;
    }

    public boolean putItems(Collection<Item> items) {
        int totalWeight = 0;
        for (var item : items) totalWeight += item.getWeight();

        if (totalWeight > capacity) return false;

        this.items = new ArrayList<>(items.size());
        for (var item: items) {
            this.items.add(item);
            weight += item.getWeight();
            price += item.getPrice();
        }

        return true;
    }

    public boolean addItem(Item item) {
        if (weight + item.getWeight() > capacity) return false;

        items.add(item);
        weight += item.getWeight();
        price += item.getPrice();

        return true;
    }

    public boolean removeItem(Item item) {
        if (!items.contains(item)) return false;

        items.remove(item);
        weight -= item.getWeight();
        price -= item.getPrice();

        return true;
    }

    public ArrayList<Item> extractItems() {
        var result = new ArrayList<>(items);

        items.clear();
        weight = 0;
        price = 0;

        return result;
    }

    public Item extractItem() {
        if (isEmpty()) return null;

        var result = items.removeLast();
        weight -= result.getWeight();
        price -= result.getPrice();
        return result;
    }

    public boolean contains(Item searchableItem) {
        for (var item : items)
            if (item.equals(searchableItem)) return true;

        return false;
    }

    public void clear() {
        items.clear();
        weight = 0;
        price = 0;
    }

    public boolean isEmpty() {
        return items.isEmpty() || weight == 0;
    }

    public String toString(boolean compactModeEnabled) {
        if (!compactModeEnabled) return toString();

        var result = new StringBuilder();

        int index = 0;
        for (var item : items) {
            result.append(item.getName());
            if (index != items.size() - 1) result.append("+");
            index++;
        }

        result.append(String.format("(%d)", price));

        return result.toString();
    }

    @Override
    public String toString() {
        var result = new StringBuilder();

        result.append(String.format("Грузоподъемность: %кг\n", capacity));
        result.append(String.format("Вес: %dкг\n", weight));
        result.append(String.format("Стоимость: %d\n", price));

        result.append("Предметы:\n");
        int index = 0;
        for (var item : items) {
            result.append(String.format("%s", item.toString()));
            if (index != items.size() - 1) result.append("\n\n");
            index++;
        }

        return result.toString();
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }
}

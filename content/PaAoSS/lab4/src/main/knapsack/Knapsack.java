package knapsack;

import java.util.ArrayList;

public final class Knapsack {
    private final int capacity;
    private int currentWeight;
    private int currentPrice;
    private ArrayList<Item> items;

    public Knapsack(int capacity) {
        this.capacity = capacity;
        this.currentWeight = 0;
        this.currentPrice = 0;
        this.items = new ArrayList<>();
    }

    private int calculateCurrentWeight() {
        int currentWeight = 0;
        for (Item item : this.items) {
            this.currentWeight += item.getWeight();
        }

        return currentWeight;
    }

    private int calculateCurrentPrice() {
        int currentPrice = 0;
        for (Item item : this.items) {
            this.currentPrice += item.getPrice();
        }

        return currentPrice;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getCurrentWeight() {
        return this.currentWeight;
    }

    public int getCurrentPrice() {
        return this.currentPrice;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public boolean addItems(ArrayList<Item> items) {
        int totalWeight = 0;
        for (Item item : items) {
            totalWeight += item.getWeight();
        }

        if (this.currentWeight + totalWeight > this.capacity) {
            return false;
        }

        this.items.addAll(items);
        this.currentWeight = this.calculateCurrentWeight();
        this.currentPrice = this.calculateCurrentPrice();

        return true;
    }

    public boolean putItems(ArrayList<Item> items) {
        int totalWeight = 0;
        for (Item item : items) {
            totalWeight += item.getWeight();
        }

        if (totalWeight > this.capacity) {
            return false;
        }

        this.items = items;
        this.currentWeight = this.calculateCurrentWeight();
        this.currentPrice = this.calculateCurrentPrice();

        return true;
    }

    public boolean addItem(Item item) {
        if (this.currentWeight + item.getWeight() > this.capacity) {
            return false;
        }

        this.items.add(item);
        this.currentWeight += item.getWeight();
        this.currentPrice += item.getPrice();

        return true;
    }

    public boolean removeItem(Item item) {
        if (!this.items.contains(item)) {
            return false;
        }

        this.items.remove(item);
        this.currentWeight -= item.getWeight();
        this.currentPrice -= item.getPrice();

        return true;
    }

    public ArrayList<Item> extractItems() {
        ArrayList<Item> result = new ArrayList<>(this.items);

        this.items.clear();
        this.currentWeight = 0;
        this.currentPrice = 0;

        return result;
    }

    public void empty() {
        this.items.clear();
        this.currentWeight = 0;
        this.currentPrice = 0;
    }

    public boolean isEmpty() {
        return this.items.isEmpty() || this.currentWeight == 0;
    }

    public String getDescription() {
        StringBuilder result = new StringBuilder();

        for (Item item : this.items) {
            result.append(item.getName());

            if (this.items.indexOf(item) != this.items.size() - 1)
                result.append("+");
        }

        result.append(String.format("(%d)", this.getCurrentPrice()));

        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Грузоподъемность: %кг\n", this.capacity));
        result.append(String.format("Вес: %dкг\n", this.currentWeight));
        result.append(String.format("Стоимость: %d\n", this.currentPrice));

        result.append("Предметы:\n");
        for (Item item : this.items) {
            result.append(String.format("%s", item.toString()));
            if (this.items.indexOf(item) != this.items.size() - 1) {
                result.append("\n\n");
            }
        }

        return result.toString();
    }
}

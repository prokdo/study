package ru.prokdo.ksSolver.knapsack;

public final class Item implements Cloneable {
    private final String name;
    private final int weight;
    private final int price;

    public Item(String name, int weight, int price) {
        if (name == null || name.isBlank() || name.isEmpty())
            throw new IllegalArgumentException("Item name cannot be null or blank");

        if (weight <= 0) throw new IllegalArgumentException("Item weight cannot be negative or zero");

        if (price < 0 ) throw new IllegalArgumentException("Item price cannot be negative");

        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        else if (object == null) return false;

        if (!(object instanceof Item)) return false;

        var other = (Item) object;

        return this.name.equals(other.name) && this.weight == other.weight && this.price == other.price;
    }

    @Override
    public String toString() {
        return String.format("Наименование: %s\nВес: %dкг\nСтоимость: %d", name, weight, price);
    }

    @Override
    public Item clone() {
        return new Item(name, weight, price);
    }
}

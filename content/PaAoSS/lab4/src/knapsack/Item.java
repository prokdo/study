package knapsack;

public final class Item {
    private final String name;
    private final int weight;
    private final int price;

    public Item(String name, int weight, int price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return String.format("Наименование: %s\nВес: %dкг\nСтоимость: %d", this.name, this.weight, this.price);
    }
}

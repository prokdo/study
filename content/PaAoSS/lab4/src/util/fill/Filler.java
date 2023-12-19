package util.fill;

import java.util.ArrayList;

import knapsack.Item;
import util.NumberGenerator;

public final class Filler {
    private Filler() {
    }

    private static ArrayList<Item> fillManual() {
        return new ArrayList<Item>();
    }

    private static ArrayList<Item> fillRandom() {
        ArrayList<Item> items = new ArrayList<Item>();

        int amount = (int) NumberGenerator.generate(1, 100, 0);

        for (int i = 0; i < amount; i++) {
            String name = "предмет" + (i + 1);
            int weight = (int) NumberGenerator.generate(1, 20, 0);
            int price = (int) NumberGenerator.generate(50, 5000, 0);

            items.add(new Item(name, weight, price));
        }

        return items;
    }

    public static ArrayList<Item> fill(FillType fillType) {
        switch (fillType) {
            case MANUAL -> {
                return Filler.fillManual();
            }
            case RANDOM -> {
                return Filler.fillRandom();
            }
            default -> {
                return null;
            }
        }
    }
}

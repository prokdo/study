package ru.prokdo.ksSolver.fill;

import java.util.ArrayList;

import ru.prokdo.ksSolver.knapsack.Item;
import ru.prokdo.ksSolver.util.math.NumberGenerator;

public final class Filler {
    private Filler() {
    }

    private static ArrayList<Item> fillManual() {
        return new ArrayList<Item>();
    }

    private static ArrayList<Item> fillRandom() {
        ArrayList<Item> items = new ArrayList<Item>();

        int amount = NumberGenerator.generateInt(1, 100);

        for (int i = 0; i < amount; i++) {
            String name = "предмет" + (i + 1);
            int weight = NumberGenerator.generateInt(1, 20);
            int price = NumberGenerator.generateInt(50, 5000);

            items.add(new Item(name, weight, price));
        }

        return items;
    }

    public static ArrayList<Item> fill(FillType fillType) {
        switch (fillType) {
            case MANUAL -> { return Filler.fillManual(); }
            case RANDOM -> { return Filler.fillRandom(); }
            default -> { return null; }
        }
    }
}

package ru.prokdo.sort;

import java.util.Random;

public enum SortType {
    ASCENDING,
    DESCENDING,
    SHAKE;

    private final static SortType[] VALUES = SortType.values();
    private final static int SIZE = VALUES.length;
    private final static Random RANDOM = new Random();

    public static SortType random() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}

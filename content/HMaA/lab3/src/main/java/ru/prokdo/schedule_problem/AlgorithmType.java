package ru.prokdo.schedule_problem;

import java.util.Random;

public enum AlgorithmType {
    CRON_METHOD,
    CRON_METHOD_MODIFICATION;

    private static final AlgorithmType[] VALUES = AlgorithmType.values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static AlgorithmType random() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
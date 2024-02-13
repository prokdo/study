package schedule_problem;

import java.util.Random;

public enum AlgorithmType {
    CRITICAL_METHOD_PATH, // Метод критического пути
    HALF_DIVISION_MULTITUDE_TASKS, // Метод половинного деления множества заданий
    ORDERED_FRAGMENTATION_MULTITUDE_TASKS; // Упорядоченное разбиение множества заданий

    private static final AlgorithmType[] VALUES = AlgorithmType.values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static AlgorithmType random() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
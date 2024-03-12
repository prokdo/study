package ru.prokdo.fill;

import java.util.Random;

public final class NumberGenerator {
    private NumberGenerator() {
    }

    private final static Random RANDOM = new Random();

    public static Double generate(int min, int max, int precision) {
        Double integer = (double) RANDOM.nextInt(min, max + 1);

        if (precision == 0) return integer;

        Integer power = (int) Math.pow(10, precision);

        Integer fraction = RANDOM.nextInt(power);

        Double number = integer.doubleValue() + fraction.doubleValue() / power;

        return Math.round(number * power) / power.doubleValue();
    }

    public static Double generate(int max, int precision) {
        Double integer = (double) RANDOM.nextInt(max + 1);

        if (precision == 0) return integer;

        Integer power = (int) Math.pow(10, precision);

        Integer fraction = RANDOM.nextInt(power);

        Double number = integer.doubleValue() + fraction.doubleValue() / power;

        return Math.round(number * power) / power.doubleValue();
    }
}

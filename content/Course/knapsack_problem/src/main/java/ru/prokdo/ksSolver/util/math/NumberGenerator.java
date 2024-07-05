package ru.prokdo.ksSolver.util.math;

import java.util.Random;

public final class NumberGenerator {
    private NumberGenerator() {
    }

    private final static Random RANDOM = new Random();

    public static int generateInt(int from, int until) {
        if (from < until) return RANDOM.nextInt(from, until + 1);
        if (from > until) return RANDOM.nextInt(until, from + 1);
        return from;
    }

    public static int generateInt(int until) {
        if (until <= 0) return -RANDOM.nextInt(-until + 1);
        return RANDOM.nextInt(until + 1);
    }

    public static double generateDouble(int from, int until, int precision) {
        if (precision <= 0) throw new IllegalArgumentException("Double generator precision cannot be zero or negative");

        int integer;
        if (from + 1 < until) integer = RANDOM.nextInt(from + 1, until);
        else if (from > until + 1) integer = RANDOM.nextInt(until + 1, from);
        else return from;

        int power = (int) Math.pow(10, precision);
        int fraction = RANDOM.nextInt(power);

        if (integer < 0) return integer - (double) fraction / power;
        return integer + (double) fraction / power;
    }

    public static double generateDouble(int until, int precision) {
        if (precision <= 0) throw new IllegalArgumentException("Double generator precision cannot be zero or negative");

        int power = (int) Math.pow(10, precision);
        int integer; int fraction;
        if (until <= 0) {
            integer = -RANDOM.nextInt(-until);
            fraction = -RANDOM.nextInt(power);
        }
        else {
            integer = RANDOM.nextInt(until);
            fraction = RANDOM.nextInt(power);
        }

        return integer + (double) fraction / power;
    }
}

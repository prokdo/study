package util;

import java.util.Random;

public final class NumberGenerator {
    private NumberGenerator() {
    }

    private final static Random RANDOM = new Random();

    public static Number generate(int min, int max, int precision) {
        Integer integer = RANDOM.nextInt(min, max + 1);

        if (precision == 0)
            return integer;

        Integer power = (int) Math.pow(10, precision);

        Integer fraction = RANDOM.nextInt(power);

        Float number = integer.floatValue() + fraction.floatValue() / power;

        return Math.round(number * power) / power.floatValue();
    }

    public static Number generate(int max, int precision) {
        Integer integer = RANDOM.nextInt(max + 1);

        if (precision == 0)
            return integer;

        Integer power = (int) Math.pow(10, precision);

        Integer fraction = RANDOM.nextInt(power);

        Float number = integer.floatValue() + fraction.floatValue() / power;

        return Math.round(number * power) / power.floatValue();
    }
}

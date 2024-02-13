package fill;

import java.util.Random;

public enum FillType {
    MANUAL,
    RANDOM;

    private final static FillType[] VALUES = FillType.values();
    private final static int SIZE = VALUES.length;
    private final static Random RAND = new Random();

    public static FillType random() {
        return VALUES[RAND.nextInt(SIZE)];
    }
}

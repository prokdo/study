package fill;

public final class Filler {
    private Filler() {
    }

    public static Double[] fill(int tasksCount, int lowerBound, int upperBound) {
        Double[] result = new Double[tasksCount];

        for (int i = 0; i < tasksCount; i++)
            result[i] = (Double) NumberGenerator.generate(lowerBound, upperBound, 0);

        return result;
    }
}
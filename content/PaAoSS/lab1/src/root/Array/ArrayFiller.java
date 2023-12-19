package root.Array;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import javafx.stage.FileChooser;

public final class ArrayFiller {
    private ArrayFiller() {}

    private static final ArrayList<Number> fillManual(Integer elementsAmount) {
        ArrayList<Number> array = new ArrayList<Number>(elementsAmount);

        return array;
    }

    private static final ArrayList<Number> fillRandom(Integer elementsAmount) {
        ArrayList<Number> array = new ArrayList<Number>();

        Random random = new Random();
        for (int i = 0; i < elementsAmount; i++) {
            Double rand = random.nextDouble(-10000, 10000);
            array.add(Math.floor(rand * 1000) / 1000);
        }

        return array;
    }

    private static final ArrayList<Number> fillFile(Integer elementsAmount) {
        ArrayList<Number> array = new ArrayList<Number>();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");
            
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(null);

        try {
                BufferedReader inputReader = new BufferedReader(new FileReader(file.toPath().toString()));

                String[] stringFile = (inputReader.readLine()).split(" ");

                for (String stringValue: stringFile)
                    array.add(Double.parseDouble(stringValue));
        } catch (Exception exception) {}

        return array;
    }

    public static final ArrayList<Number> fill(FillType fillType, Integer elementsAmount) {
        switch (fillType) {
            case MANUAL -> {return fillManual(elementsAmount);}
            case RANDOM -> {return fillRandom(elementsAmount);}
            case FILE -> {return fillFile(elementsAmount);}
            default -> {return null;}
        }
    }
}

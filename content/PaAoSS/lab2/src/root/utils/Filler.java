package root.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.stage.FileChooser;
import root.matrix.Matrix;

public final class Filler {
    private Filler() {}

    private final static ArrayList<Matrix> fillRandom() {
        Random generator = new Random();

        int elementsAmount = generator.nextInt(2, 100);

        ArrayList<Matrix> result = new ArrayList<>();

        int rowsAmount = generator.nextInt(1, 7);
        int columnsAmount = generator.nextInt(1, 7);

        Matrix currentMatrix = new Matrix(rowsAmount, columnsAmount);
        while (!currentMatrix.isFull())
            currentMatrix.addValue(generator.nextDouble() * 1000);

        result.add(currentMatrix);
        
        while (result.size() != elementsAmount) {
            Matrix newMatrix = new Matrix(currentMatrix.getLength(), generator.nextInt(1, 100));

            while (!newMatrix.isFull())
                newMatrix.addValue(generator.nextDouble() * 1000);

            result.add(newMatrix);
            currentMatrix = newMatrix;
        }

        return result;
    }

    private final static ArrayList<Matrix> fillFile() {
        ArrayList<Matrix> result = new ArrayList<>();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(null);

        try {
                Scanner scanner = new Scanner(file);

                String headerLine = scanner.nextLine();

                int currentMatrixRows = Integer.parseInt(headerLine.split(" ")[0]);
                int currentMatrixColumns = Integer.parseInt(headerLine.split(" ")[1]);;
                Matrix currentMatrix = new Matrix(currentMatrixRows, currentMatrixColumns);

                while (scanner.hasNextLine()) {
                    String currentLine = scanner.nextLine();

                    if (currentLine == "") {
                        result.add(currentMatrix);
                        
                        headerLine = scanner.nextLine();

                        currentMatrixRows = Integer.parseInt(headerLine.split(" ")[0]);
                        currentMatrixColumns = Integer.parseInt(headerLine.split(" ")[1]);

                        currentMatrix = new Matrix(currentMatrixRows, currentMatrixColumns);
                    }
                    else {
                        for (String strNumber: currentLine.split(" ")) {
                            currentMatrix.addValue(Double.parseDouble(strNumber));
                        }
                    }
                }

                result.add(currentMatrix);

                scanner.close();
        } catch (Exception exception) {exception.printStackTrace();}

        return result;
    }

    private final static ArrayList<Matrix> fillManual() {
        return new ArrayList<Matrix>();
    }

    public final static ArrayList<Matrix> fill(FillType fillType) {
        switch (fillType) {
            case RANDOM -> {return fillRandom();}
            case FILE -> {return fillFile();}
            case MANUAL -> {return fillManual();}
            default -> {return null;}
        }
    }
}

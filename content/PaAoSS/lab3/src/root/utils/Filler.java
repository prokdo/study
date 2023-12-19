package root.utils;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

import javafx.stage.FileChooser;

public final class Filler {
    private Filler() {}

    // private static String alphabet = "0123456789-+=/*;:#@'%$^()[]{}<>.,~|\"\\aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯ";

    // private static String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // private static String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // private static String alphabet = "abcdefghijklmnopqrstuvwxyz";
    
    private static String alphabet = "ABCDEFG";

    private final static String[] fillRandom() {
        String[] strings = new String[2];
    
        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            int length = random.nextInt(1000) + 1;
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < length; j++) {
                stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }

            strings[i] = stringBuilder.toString();
        }

        return strings;
    }

    private final static String[] fillFile() {
        String[] strings = new String[2];

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine()) {
                    strings[0] = scanner.nextLine();
                }

                if (scanner.hasNextLine()) {
                    strings[1] = scanner.nextLine();
                }

                scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return strings;
    }

    private final static String[] fillManual() {
        return new String[2];
    }

    public final static String[] fill(FillType fillType) {
        switch (fillType) {
            case RANDOM -> {return fillRandom();}
            case FILE -> {return fillFile();}
            case MANUAL -> {return fillManual();}
            default -> {return null;}
        }
    }
}

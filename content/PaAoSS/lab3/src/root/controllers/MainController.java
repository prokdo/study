package root.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import root.lcs.LCSFinder;
import root.utils.FillType;
import root.utils.Filler;

public class MainController {

    @FXML
    private Button buttonBegin;

    @FXML
    private FlowPane fpArrayFillTypeContainer;

    @FXML
    private MenuButton mbSourceType;

    @FXML
    private MenuItem miClose;

    @FXML
    private MenuItem miFileFill;

    @FXML
    private MenuItem miManualFill;

    @FXML
    private MenuItem miRandomFill;

    @FXML
    private TextArea taResult;

    @FXML
    private TextArea taString1;

    @FXML
    private TextArea taString2;

    private String[] strings;

    @FXML
    void onButtonBeginClicked(MouseEvent event) {
        buttonBegin.setVisible(false);
        taString1.setEditable(false);
        taString2.setEditable(false);

        if (strings[0] == null || strings[1] == null) {
            String string1 = taString1.getText();
            if (string1 != null && !string1.isEmpty()) {
                String[] string1Parts = string1.split("\n");
                strings[0] = string1Parts[0];
            }

            String string2 = taString2.getText();
            if (string2 != null && !string2.isEmpty()) {
                String[] string2Parts = string2.split("\n");
                strings[1] = string2Parts[0];
            }
        }

        LCSFinder lcsFinder = new LCSFinder(strings);
        
        String result = lcsFinder.find();
        if (result == null)
            taResult.setText("Не удалось найти общую подстроку");
        else
            taResult.setText(lcsFinder.find());
    }

    @FXML
    void onMiCloseClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onMiFileFillClicked(ActionEvent event) {
        taString1.setEditable(false);
        taString2.setEditable(false);
        
        mbSourceType.setText("Из файла");

        taResult.setText(null);
        strings = Filler.fill(FillType.FILE);

        taString1.setText(strings[0]);
        taString2.setText(strings[1]);

        buttonBegin.setVisible(true);
    }

    @FXML
    void onMiManualFillClicked(ActionEvent event) {
        taString1.setEditable(true);
        taString2.setEditable(true);
        
        mbSourceType.setText("Ручной ввод");

        taResult.setText(null);
        strings = Filler.fill(FillType.MANUAL);

        taString1.setText(null);
        taString2.setText(null);

        taString1.setPromptText("Введите первую строку");
        taString2.setPromptText("Введите вторую строку");

        buttonBegin.setVisible(true);
    }

    @FXML
    void onMiRandomFillClicked(ActionEvent event) {
        taString1.setEditable(false);
        taString2.setEditable(false);
        
        taResult.setText(null);
        mbSourceType.setText("Случайно");

        strings = Filler.fill(FillType.RANDOM);

        taString1.setText(strings[0]);
        taString2.setText(strings[1]);

        buttonBegin.setVisible(true);
    }
}

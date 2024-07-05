package ru.prokdo.ksSolver.controller;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import ru.prokdo.ksSolver.fill.FillType;
import ru.prokdo.ksSolver.fill.Filler;
import ru.prokdo.ksSolver.knapsack.Item;
import ru.prokdo.ksSolver.knapsack.Knapsack;
import ru.prokdo.ksSolver.solution.Solver;
import ru.prokdo.ksSolver.util.math.NumberGenerator;
import ru.prokdo.ksSolver.util.struct.Pair;

public class MainController extends Controller {

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonBegin;

    @FXML
    private Button buttonInputEnd;

    @FXML
    private Button buttonReset;

    @FXML
    private Button buttonStart;

    @FXML
    private MenuButton mbFillType;

    @FXML
    private MenuItem miExit;

    @FXML
    private MenuItem miManual;

    @FXML
    private MenuItem miRandom;

    @FXML
    private StackPane spControlContainer;

    @FXML
    private StackPane spFillTypeContainer;

    @FXML
    private StackPane spInputContainer;

    @FXML
    private TextArea taItems;

    @FXML
    private TextArea taItemsResult;

    @FXML
    private TextArea taMatrix;

    @FXML
    private Text textCapacity;

    @FXML
    private Text textCurrentPrice;

    @FXML
    private Text textCurrentWeight;

    @FXML
    private TextField tfCapacity;

    @FXML
    private TextField tfItemName;

    @FXML
    private TextField tfItemPrice;

    @FXML
    private TextField tfItemWeight;

    private ArrayList<Item> items;
    private int capacity;
    private FillType fillType;

    private void addItem(Item item) {
        if (item == null) {
            String name;
            int weight;
            int price;

            try {
                name = tfItemName.getText();
                weight = Integer.parseInt(tfItemWeight.getText());
                price = Integer.parseInt(tfItemPrice.getText());
            } 
            catch (Exception exception) { return; }

            if (name.isEmpty() || weight == 0 || price == 0) return;

            if (weight <= 0 || price <= 0) return;

            item = new Item(name, weight, price);
            items.add(item);

            if (items.size() != 1) taItems.appendText("\n\n");
            taItems.appendText(item.toString());

            tfItemName.setText("");
            tfItemWeight.setText("");
            tfItemPrice.setText("");
        } 
        else {
            taItems.appendText(item.toString());
            if (items.indexOf(item) != items.size() - 1) taItems.appendText("\n\n");
        }
    }

    private Pair<Knapsack, Integer> findMax(Knapsack[][] matrix) {
        Knapsack result = null;
        int maxPrice = -1;
        int maxSize = -1;
        Pair<Integer, Integer> maxSizeCoordinates = null;
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getPrice() > maxPrice) {
                    maxPrice = matrix[i][j].getPrice();
                    result = matrix[i][j];
                }

                if (matrix[i][j].toString(true).length() > maxSize) {
                    maxSize = matrix[i][j].toString(true).length();
                    maxSizeCoordinates = new Pair<>(i, j);
                }
            }
        int maxLength = matrix[maxSizeCoordinates.first][maxSizeCoordinates.second].toString(true).length();

        return new Pair<>(result, maxLength);
    }

    private String getResultInfo(Knapsack[][] matrix, int maxLength) {
        StringBuilder result = new StringBuilder();
        for (var matrixRow : matrix) {
            for (var knapsack : matrixRow) {
                result.append(knapsack.toString(true));
                result.append(" ".repeat(maxLength - knapsack.toString(true).length()));
                result.append("   ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    @FXML
    void onButtonAddClicked(MouseEvent event) {
        addItem(null);
    }

    @FXML
    void onButtonBeginClicked(MouseEvent event) {
        spFillTypeContainer.setVisible(false);

        items = Filler.fill(fillType);

        switch (fillType) {
            case FillType.MANUAL -> {
                spInputContainer.setVisible(true);
            }
            case FillType.RANDOM -> {
                capacity = NumberGenerator.generateInt(1, 100);

                textCapacity.setText(String.valueOf(capacity));

                for (Item item : items) addItem(item);

                spControlContainer.setVisible(true);
            }
            default -> {
                return;
            }
        }
    }

    @FXML
    void onButtonInputEndClicked(MouseEvent event) {
        if (items.isEmpty() || capacity == 0) return;

        spInputContainer.setVisible(false);
        spControlContainer.setVisible(true);
    }

    @FXML
    void onButtonResetClicked(MouseEvent event) {
        buttonBegin.setVisible(false);
        buttonStart.setVisible(true);
        spControlContainer.setVisible(false);
        spFillTypeContainer.setVisible(true);

        tfCapacity.setText("");
        mbFillType.setText("");
        tfItemName.setText("");
        tfItemPrice.setText("");
        taItems.setText("");
        taItemsResult.setText("");
        tfItemWeight.setText("");
        taMatrix.setText("");

        textCapacity.setText("0");
        textCurrentPrice.setText("0");
        textCurrentWeight.setText("0");

        capacity = 0;
        fillType = null;
        items = null;
    }

    @FXML
    void onButtonStartClicked(MouseEvent event) {
        buttonStart.setVisible(false);

        Knapsack[][] solverMatrix = Solver.invoke(capacity, items);

        Pair<Knapsack, Integer> maxPair = findMax(solverMatrix);

        String matrixString = getResultInfo(solverMatrix, maxPair.second);
        taMatrix.setText(matrixString.toString());

        textCurrentPrice.setText(String.valueOf(maxPair.first.getPrice()));
        textCurrentWeight.setText(String.valueOf(maxPair.first.getWeight()));

        for (var item : maxPair.first.getItems()) {
            taItemsResult.appendText(item.toString());
            if (maxPair.first.getItems().indexOf(item) != maxPair.first.getItems().size() - 1)
                taItemsResult.appendText("\n\n");
        }
    }

    @FXML
    void onMiExitClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onMiManualClicked(ActionEvent event) {
        fillType = FillType.MANUAL;
        mbFillType.setText("Ручной ввод");

        buttonBegin.setVisible(true);
    }

    @FXML
    void onMiRandomClicked(ActionEvent event) {
        fillType = FillType.RANDOM;
        mbFillType.setText("Случайно");

        buttonBegin.setVisible(true);
    }

    @FXML
    void onTfCapacityAction(ActionEvent event) {
        try { capacity = Integer.parseInt(tfCapacity.getText()); } 
        catch (Exception exception) { capacity = 0; }

        if (capacity <= 0) capacity = 0;

        textCapacity.setText(String.valueOf(capacity));
    }
}

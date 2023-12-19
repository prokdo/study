package controller;

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

import knapsack.Item;
import knapsack.Knapsack;
import knapsack.Solver;
import util.NumberGenerator;
import util.fill.Filler;
import util.fill.FillType;
import util.Pair;

public class MainController {

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
                name = this.tfItemName.getText();
                weight = Integer.parseInt(this.tfItemWeight.getText());
                price = Integer.parseInt(this.tfItemPrice.getText());
            } catch (Exception exception) {
                return;
            }

            if (name.isEmpty() || weight == 0 || price == 0) {
                return;
            }

            if (weight <= 0 || price <= 0)
                return;

            item = new Item(name, weight, price);
            this.items.add(item);

            if (this.items.size() != 1)
                this.taItems.appendText("\n\n");
            this.taItems.appendText(item.toString());

            this.tfItemName.setText("");
            this.tfItemWeight.setText("");
            this.tfItemPrice.setText("");
        } else {
            this.taItems.appendText(item.toString());
            if (this.items.indexOf(item) != this.items.size() - 1)
                this.taItems.appendText("\n\n");
        }
    }

    private Pair<Knapsack, Integer> findMax(Knapsack[][] matrix) {
        Knapsack result = null;
        int maxPrice = -1;
        int maxSize = -1;
        Pair<Integer, Integer> maxSizeCoordinates = null;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getCurrentPrice() > maxPrice) {
                    maxPrice = matrix[i][j].getCurrentPrice();
                    result = matrix[i][j];
                }

                if (matrix[i][j].getDescription().length() > maxSize) {
                    maxSize = matrix[i][j].getDescription().length();
                    maxSizeCoordinates = new Pair<>(i, j);
                }
            }
        }
        int maxLength = matrix[maxSizeCoordinates.first][maxSizeCoordinates.second].getDescription().length();

        return new Pair<>(result, maxLength);
    }

    private String getResultInfo(Knapsack[][] matrix, int maxLength) {
        StringBuilder result = new StringBuilder();
        for (Knapsack[] matrixRow : matrix) {
            for (Knapsack knapsack : matrixRow) {
                result.append(knapsack.getDescription());
                result.append(" ".repeat(maxLength - knapsack.getDescription().length()));
                result.append("   ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    @FXML
    void onButtonAddClicked(MouseEvent event) {
        this.addItem(null);
    }

    @FXML
    void onButtonBeginClicked(MouseEvent event) {
        this.spFillTypeContainer.setVisible(false);

        this.items = Filler.fill(this.fillType);

        switch (fillType) {
            case FillType.MANUAL -> {
                this.spInputContainer.setVisible(true);
            }
            case FillType.RANDOM -> {
                this.capacity = (int) NumberGenerator.generate(1, 100, 0);

                this.textCapacity.setText(String.valueOf(this.capacity));

                for (Item item : this.items)
                    this.addItem(item);

                this.spControlContainer.setVisible(true);
            }
            default -> {
                return;
            }
        }
    }

    @FXML
    void onButtonInputEndClicked(MouseEvent event) {
        if (this.items.isEmpty() || this.capacity == 0) {
            return;
        }

        this.spInputContainer.setVisible(false);
        this.spControlContainer.setVisible(true);
    }

    @FXML
    void onButtonResetClicked(MouseEvent event) {
        this.buttonBegin.setVisible(false);
        this.buttonStart.setVisible(true);
        this.spControlContainer.setVisible(false);
        this.spFillTypeContainer.setVisible(true);

        this.tfCapacity.setText("");
        this.mbFillType.setText("");
        this.tfItemName.setText("");
        this.tfItemPrice.setText("");
        this.taItems.setText("");
        this.taItemsResult.setText("");
        this.tfItemWeight.setText("");
        this.taMatrix.setText("");

        this.textCapacity.setText("0");
        this.textCurrentPrice.setText("0");
        this.textCurrentWeight.setText("0");

        this.capacity = 0;
        this.fillType = null;
        this.items = null;
    }

    @FXML
    void onButtonStartClicked(MouseEvent event) {
        this.buttonStart.setVisible(false);

        Knapsack[][] solverMatrix = Solver.solve(this.capacity, this.items);

        Pair<Knapsack, Integer> maxPair = this.findMax(solverMatrix);

        String matrixString = this.getResultInfo(solverMatrix, maxPair.second);
        this.taMatrix.setText(matrixString.toString());

        this.textCurrentPrice.setText(String.valueOf(maxPair.first.getCurrentPrice()));
        this.textCurrentWeight.setText(String.valueOf(maxPair.first.getCurrentWeight()));

        for (Item item : maxPair.first.getItems()) {
            this.taItemsResult.appendText(item.toString());
            if (maxPair.first.getItems().indexOf(item) != maxPair.first.getItems().size() - 1)
                this.taItemsResult.appendText("\n\n");
        }
    }

    @FXML
    void onMiExitClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onMiManualClicked(ActionEvent event) {
        this.fillType = FillType.MANUAL;
        this.mbFillType.setText("Ручной ввод");

        this.buttonBegin.setVisible(true);
    }

    @FXML
    void onMiRandomClicked(ActionEvent event) {
        this.fillType = FillType.RANDOM;
        this.mbFillType.setText("Случайно");

        this.buttonBegin.setVisible(true);
    }

    @FXML
    void onTfCapacityAction(ActionEvent event) {
        try {
            this.capacity = Integer.parseInt(this.tfCapacity.getText());
        } catch (Exception exception) {
            this.capacity = 0;
        }

        if (this.capacity <= 0)
            this.capacity = 0;

        this.textCapacity.setText(String.valueOf(this.capacity));
    }
}

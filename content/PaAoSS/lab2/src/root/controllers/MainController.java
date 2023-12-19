package root.controllers;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import root.matrix.Matrix;
import root.matrix.MatrixMultiplicator;
import root.matrix.MultiplicationRouter;
import root.utils.FillType;
import root.utils.Filler;

public final class MainController {

    @FXML
    private AnchorPane apManualFillContainer;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonBegin;


    @FXML
    private Button buttonLaunch;

    @FXML
    private Button buttonReset;

    @FXML
    private FlowPane fpAddNumber;

    @FXML
    private FlowPane fpArrayFillTypeContainer;

    @FXML
    private FlowPane fpElementsAmountContainer;

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
    private MenuItem miSave;

    @FXML
    private StackPane spControlContainer;

    @FXML
    private StackPane spDataSourceContainer;

    @FXML
    private TextArea taCurrentMatrix;

    @FXML
    private TextArea taResult;

    @FXML
    private TextArea taRoute;

    @FXML
    private TextArea taSourceData;

    @FXML
    private Text textCurrentMatrix;

    @FXML
    private Text textCurrentMatrixIndex;

    @FXML
    private Text textInputType;

    @FXML
    private Text textMatrixAmount;

    @FXML
    private Text textMatrixAmountInfo;

    @FXML
    private TextField tfAddNumber;

    @FXML
    private TextField tfColumnsAmount;

    @FXML
    private TextField tfElementsAmount;

    @FXML
    private TextField tfRowsAmount;

    private FillType fillType;

    private int matrixAmount;

    private ArrayList<Matrix> matrixList;

    private Integer currentMatrixRows;

    private Integer currentMatrixColumns;

    private int currentMatrixIndex;

    private Matrix currentMatrix;

    private void manualFill() {
        spDataSourceContainer.setVisible(false);

        matrixList = Filler.fill(fillType);

        textMatrixAmount.setText(matrixAmount + "");

        textMatrixAmountInfo.setText(matrixAmount + "");

        apManualFillContainer.setVisible(true);
    }

    private void randomFill() {
        spDataSourceContainer.setVisible(false);

        matrixList = Filler.fill(fillType); 

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < matrixList.size(); i++)
            stringBuilder.append("Матрица №" + (i + 1) + ":\n"  + matrixList.get(i).toString() + "\n");

        taSourceData.setText(stringBuilder.toString());

        spControlContainer.setVisible(true);

        textMatrixAmountInfo.setText(matrixList.size() + "");

        taRoute.setText(MultiplicationRouter.getRoute(matrixList).toString());
    }

    private void fileFill() {
        spDataSourceContainer.setVisible(false);

        matrixList = Filler.fill(fillType); 

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < matrixList.size(); i++)
            stringBuilder.append("Матрица №" + (i + 1) + ":\n"  + matrixList.get(i).toString() + "\n");

        taSourceData.setText(stringBuilder.toString());
        
        spControlContainer.setVisible(true);

        textMatrixAmountInfo.setText(matrixList.size() + "");

        MultiplicationRouter.getRoute(matrixList);
    }

    @FXML
    void onButtonAddClicked(MouseEvent event) {
        Number element;

        try {
            element = Double.parseDouble(tfAddNumber.getText());

            currentMatrix.addValue(element);

            if (currentMatrix.isFull()) {
                matrixList.add(currentMatrix);

                taSourceData.setText(taSourceData.getText() + "Матрица №" + (currentMatrixIndex + 1) + ":\n"  + currentMatrix.toString() + "\n");

                currentMatrixIndex += 1;

                if (currentMatrixIndex <= matrixAmount - 1)
                    textCurrentMatrixIndex.setText((currentMatrixIndex + 1) + "");

                taCurrentMatrix.setVisible(false);
                buttonAdd.setVisible(false);
                fpAddNumber.setVisible(false);
                textCurrentMatrix.setVisible(false);

                tfColumnsAmount.setText(null);
                tfColumnsAmount.setEditable(true);
                tfRowsAmount.setText(null);
                tfRowsAmount.setEditable(true);
                tfAddNumber.setText(null);
            } 
            else {
                taCurrentMatrix.setText(currentMatrix.toString());
            }

            if (currentMatrixIndex > matrixAmount - 1) {
                apManualFillContainer.setVisible(false);

                spControlContainer.setVisible(true);
            }
        } catch (Exception exception) {}
    }

    @FXML
    void onButtonBeginClicked(MouseEvent event) {
        switch (fillType) {
            case MANUAL -> {textInputType.setText("Ручной ввод"); manualFill();}
            case RANDOM -> {textInputType.setText("Случайно"); randomFill();}
            case FILE -> {textInputType.setText("Из файла"); fileFill();}
        }
    }

    @FXML
    void onButtonLaunchClicked(MouseEvent event) {
        MatrixMultiplicator matrixMultiplicator = new MatrixMultiplicator(matrixList);
        int result = matrixMultiplicator.calculate();

        if (result == 0) {
            taRoute.setText(matrixMultiplicator.getRoute().toString());
            taResult.setText(matrixMultiplicator.getResult().toString());
        }
        else {
            taRoute.setText("Невозможно выполнить полное умножение последовательности");
        }
    }

    @FXML
    void onButtonResetClicked(MouseEvent event) {

    }

    @FXML
    void onMiCloseClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onMiFileFillClicked(ActionEvent event) {
        fillType = FillType.FILE;

        mbSourceType.setText("Из файла");

        fpElementsAmountContainer.setVisible(false);
        buttonBegin.setVisible(true);
    }

    @FXML
    void onMiManualFillClicked(ActionEvent event) {
        fillType = FillType.MANUAL;

        mbSourceType.setText("Ручной ввод");

        fpElementsAmountContainer.setVisible(true);
        buttonBegin.setVisible(false);
    }

    @FXML
    void onMiRandomFillClicked(ActionEvent event) {
        fillType = FillType.RANDOM;

        mbSourceType.setText("Случайно");

        fpElementsAmountContainer.setVisible(false);
        buttonBegin.setVisible(true);
    }

    @FXML
    void onMiSaveClicked(ActionEvent event) {

    }

    @FXML
    void onTfColumnsAmountChanged(ActionEvent event) {
        try {
            currentMatrixColumns = Integer.parseInt(tfColumnsAmount.getText());
            currentMatrixRows = Integer.parseInt(tfRowsAmount.getText());

            if (currentMatrixColumns > 0 && currentMatrixRows > 0) {
                tfColumnsAmount.setEditable(false);
                tfRowsAmount.setEditable(false);

                fpAddNumber.setVisible(true);
                buttonAdd.setVisible(true);
                textCurrentMatrix.setVisible(true);
                taCurrentMatrix.setVisible(true);

                currentMatrix = new Matrix(currentMatrixRows, currentMatrixColumns);

                taCurrentMatrix.setText(currentMatrix.toString());
            }
        } catch (Exception exception) {}
    }

    @FXML
    void onTfElementsAmountChanged(ActionEvent event) {
        try {
            matrixAmount = Integer.parseInt(tfElementsAmount.getText());

            if (matrixAmount > 1)
                buttonBegin.setVisible(true);
        } catch (Exception exception) { 
            buttonBegin.setVisible(false); 
        }
    }

    @FXML
    void onTfRowsAmountChanged(ActionEvent event) {
        try {
            currentMatrixRows = Integer.parseInt(tfRowsAmount.getText());
            currentMatrixColumns = Integer.parseInt(tfColumnsAmount.getText());

            if (currentMatrixColumns > 0 && currentMatrixRows > 0) {
                tfColumnsAmount.setEditable(false);
                tfRowsAmount.setEditable(false);

                fpAddNumber.setVisible(true);
                buttonAdd.setVisible(true);
                textCurrentMatrix.setVisible(true);
                taCurrentMatrix.setVisible(true);

                currentMatrix = new Matrix(currentMatrixRows, currentMatrixColumns);

                taCurrentMatrix.setText(currentMatrix.toString());
            }
        } catch (Exception exception) {}
    }
}

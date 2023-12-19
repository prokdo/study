package root;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import root.Array.ArrayFiller;
import root.Array.ArrayInfo;
import root.Array.FillType;
import root.Sort.Sorter;

public final class Controller implements Initializable {

    @FXML
    private Button buttonAddNumber;

    @FXML
    private Button buttonReset;

    @FXML
    private MenuButton mbArrayFill;

    @FXML
    private MenuButton mbElementsAmount;

    @FXML
    private MenuButton mbSortType;

    @FXML
    private MenuItem mi10000;

    @FXML
    private MenuItem mi100000;

    @FXML
    private MenuItem mi150000;

    @FXML
    private MenuItem mi5000;

    @FXML
    private MenuItem miArrayFileFill;

    @FXML
    private MenuItem miArrayManualFill;

    @FXML
    private MenuItem miArrayRandomFill;

    @FXML
    private MenuItem miBubbleSort;

    @FXML
    private MenuItem miClose;

    @FXML
    private MenuItem miCustom;

    @FXML
    private MenuItem miHeapSort;

    @FXML
    private MenuItem miInsertionSort;

    @FXML
    private MenuItem miMergeSort;

    @FXML
    private MenuItem miQuickSort;

    @FXML
    private MenuItem miSave;

    @FXML
    private MenuItem miSelectionSort;

    @FXML
    private MenuItem miShellSort;

    @FXML
    private MenuItem miTreeSort;

    @FXML
    private TextArea taSortedArray;

    @FXML
    private TextArea taSourceArray;

    @FXML
    private Text textArraySize;

    @FXML
    private Text textSortTime;

    @FXML
    private TextField tfAddNumber;

    @FXML
    private TextField tfCustomAmount;

    @FXML
    private FlowPane fpCustomAmountContainer;

    @FXML
    private AnchorPane apManualFillContainer;

    @FXML
    private AnchorPane apArraySortContainer;
    
    @FXML
    private FlowPane fpElementsAmountContainer;

    private ArrayInfo arrayInfo = new ArrayInfo();

    @Override
    public final void initialize(URL arg0, ResourceBundle arg1) {
        fpCustomAmountContainer.setVisible(false);
        fpElementsAmountContainer.setVisible(false);

        apManualFillContainer.setVisible(false);
        apArraySortContainer.setVisible(false);
    }

    private final void hideMenuButtonItems(MenuButton menuButton) {
        for (MenuItem mi : menuButton.getItems())
            mi.setVisible(false);
    }

    private final void showMenuButtonItems(MenuButton menuButton) {
        for (MenuItem mi : menuButton.getItems())
            mi.setVisible(true);
    }

    @FXML
    public final void onMi5000Clicked(Event event) {
        arrayInfo.setElementsAmount(5000);
        mbElementsAmount.setText("5000");

        hideMenuButtonItems(mbElementsAmount);

        arrayInfo.setArray(ArrayFiller.fill(arrayInfo.getFillType(), arrayInfo.getElementsAmount()));

        taSourceArray.setText(arrayInfo.getArray().toString());

        if (arrayInfo.getFillType() == FillType.MANUAL)
            apManualFillContainer.setVisible(true);
        else
            apArraySortContainer.setVisible(true);
    }

    @FXML
    public final void onMi10000Clicked(Event event) {
        arrayInfo.setElementsAmount(10000);
        mbElementsAmount.setText("10000");

        hideMenuButtonItems(mbElementsAmount);

        arrayInfo.setArray(ArrayFiller.fill(arrayInfo.getFillType(), arrayInfo.getElementsAmount()));

        taSourceArray.setText(arrayInfo.getArray().toString());

        if (arrayInfo.getFillType() == FillType.MANUAL)
            apManualFillContainer.setVisible(true);
        else
            apArraySortContainer.setVisible(true);
    }

    @FXML
    public final void onMi100000Clicked(Event event) {
        arrayInfo.setElementsAmount(100000);
        mbElementsAmount.setText("100000");

        hideMenuButtonItems(mbElementsAmount);

        arrayInfo.setArray(ArrayFiller.fill(arrayInfo.getFillType(), arrayInfo.getElementsAmount()));

        taSourceArray.setText(arrayInfo.getArray().toString());

        if (arrayInfo.getFillType() == FillType.MANUAL)
            apManualFillContainer.setVisible(true);
        else
            apArraySortContainer.setVisible(true);
    }

    @FXML
    public final void onMi150000Clicked(Event event) {
        arrayInfo.setElementsAmount(150000);
        mbElementsAmount.setText("150000");

        hideMenuButtonItems(mbElementsAmount);

        arrayInfo.setArray(ArrayFiller.fill(arrayInfo.getFillType(), arrayInfo.getElementsAmount()));

        taSourceArray.setText(arrayInfo.getArray().toString());

        if (arrayInfo.getFillType() == FillType.MANUAL)
            apManualFillContainer.setVisible(true);
        else
            apArraySortContainer.setVisible(true);
    }

    @FXML
    public final void onMiCustomClicked(Event event) {
        mbElementsAmount.setText("Другое*");

        hideMenuButtonItems(mbElementsAmount);

        fpCustomAmountContainer.setVisible(true);
    }

    @FXML
    public final void onTfCustomAmountChanged(Event event) {
        try {
            arrayInfo.setElementsAmount(Integer.parseInt(tfCustomAmount.getText()));
        } catch (Exception exception) {}

        arrayInfo.setArray(ArrayFiller.fill(arrayInfo.getFillType(), arrayInfo.getElementsAmount()));

        if (arrayInfo.getFillType() == FillType.MANUAL)
            apManualFillContainer.setVisible(true);
        else {
            taSourceArray.setText(arrayInfo.getArray().toString());

            apArraySortContainer.setVisible(true);
        }

        tfCustomAmount.setEditable(false);
    }

    @FXML
    public final void onMiArrayManualFillClicked(Event event) {
        arrayInfo.setFillType(FillType.MANUAL);
        mbArrayFill.setText("Ручной ввод");

        hideMenuButtonItems(mbArrayFill);
        
        fpElementsAmountContainer.setVisible(true);
    }

    @FXML
    public final void onMiArrayRandomFillClicked(Event event) {
        arrayInfo.setFillType(FillType.RANDOM);
        mbArrayFill.setText("Случайно");

        hideMenuButtonItems(mbArrayFill);

        fpElementsAmountContainer.setVisible(true);
    }

    @FXML
    public final void onMiArrayFileFillClicked(Event event) {
        arrayInfo.setFillType(FillType.FILE);
        mbArrayFill.setText("Из файла");

        hideMenuButtonItems(mbArrayFill);

        arrayInfo.setArray(ArrayFiller.fill(arrayInfo.getFillType(), null));

        taSourceArray.setText(arrayInfo.getArray().toString());

        apArraySortContainer.setVisible(true);
    }

    @FXML
    public final void onButtonAddClicked(Event event) {
        try {
            arrayInfo.getArray().add(Double.parseDouble(tfAddNumber.getText()));
        }
        catch (Exception exception) {}

        taSourceArray.setText(arrayInfo.getArray().toString());

        textArraySize.setText(arrayInfo.getArray().size() + "");

        if (arrayInfo.getArray().size() == arrayInfo.getElementsAmount()){
            apArraySortContainer.setVisible(true);

            buttonAddNumber.setVisible(false);

            tfAddNumber.setEditable(false);
        }
    }

    @FXML
    public final void onMiBubbleSortClicked(Event event) {
        mbSortType.setText("Пузырьковая");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.bubbleSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    public final void onMiSelectionSortClicked(Event event) {
        mbSortType.setText("Выбором");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.selectionSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    public final void onMiInsertionSortClicked(Event event) {
         mbSortType.setText("Вставками");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.insertionSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    public final void onMiHeapSortClicked(Event event) {
        mbSortType.setText("Пирамидальная");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.heapSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    public final void onMiTreeSortClicked(Event event) {
        mbSortType.setText("Деревом");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.treeSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    public final void onMiMergeSortClicked(Event event) {
        mbSortType.setText("Слиянием");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.mergeSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    public final void onMiShellSortClicked(Event event) {
        mbSortType.setText("Шелла");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.shellSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    public final void onMiQuickSortClicked(Event event) {
        mbSortType.setText("Быстрая");
        arrayInfo.setSortedArray((ArrayList<Number>) arrayInfo.getArray().clone());
        textSortTime.setText(Sorter.quickSort(arrayInfo.getSortedArray()) + "");
        taSortedArray.setText(arrayInfo.getSortedArray().toString());
    }

    @FXML
    public final void onButtonResetClicked(Event event) {
        arrayInfo.reset();

        taSourceArray.setText(null);
        taSortedArray.setText(null);
        
        showMenuButtonItems(mbArrayFill);
        showMenuButtonItems(mbElementsAmount);
        
        mbElementsAmount.setText(null);
        mbArrayFill.setText(null);

        tfCustomAmount.setText(null);
        tfCustomAmount.setEditable(true);
        
        tfAddNumber.setText(null);
        tfAddNumber.setEditable(true);

        fpCustomAmountContainer.setVisible(false);
        fpElementsAmountContainer.setVisible(false);
        
        textArraySize.setText("0");
        textSortTime.setText("0");
        
        buttonAddNumber.setVisible(true);

        apManualFillContainer.setVisible(false);
        apArraySortContainer.setVisible(false);

    }

    @FXML
    public final void onMiSaveClicked(Event event) {
        if (arrayInfo.getSortedArray() != null) {
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extensionFilter);

            File file = fileChooser.showSaveDialog(null);

            try {
                if (!file.exists())
                    Files.createFile(file.toPath());
                
                BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file.toPath().toString()));
                for (Number value : arrayInfo.getSortedArray())
                    outputWriter.write(value + " ");

                outputWriter.flush();
                outputWriter.close();
            } catch (Exception exception) {}
        }
    }

    @FXML
    public final void onMiCloseClicked(Event event) {
        System.exit(0);
    }
}

package root.Array;

import java.util.ArrayList;

import root.Sort.SortAlgorithm;

public final class ArrayInfo {
    private ArrayList<Number> array;
    private ArrayList<Number> sortedArray;
    private int elementsAmount;
    private FillType fillType;
    private SortAlgorithm sortType;
    private long sortTime;

    public final void setArray(ArrayList<Number> array) {
        this.array = array;
    }

    public final ArrayList<Number> getArray() {
        return array;
    }

    public final void setElementsAmount(int elementsAmount) {
        this.elementsAmount = elementsAmount;
    }

    public final int getElementsAmount() {
        return this.elementsAmount;
    }

    public final void setFillType(FillType fillType) {
        this.fillType = fillType;
    }

    public final FillType getFillType() {
        return this.fillType;
    }

    public final void setSortType(SortAlgorithm sortType) {
        this.sortType = sortType;
    }

    public final SortAlgorithm getSortType() {
        return this.sortType;
    }

    public final void setSortTime(long sortTime) {
        this.sortTime = sortTime;
    }

    public final long getSortTime() {
        return this.sortTime;
    }

    public final void setSortedArray(ArrayList<Number> array) {
        this.sortedArray = array;
    }

    public final ArrayList<Number> getSortedArray() {
        return this.sortedArray;
    }

    public final void reset() {
        this.array = null;
        this.elementsAmount = 0;
        this.fillType = null;
        this.sortType = null;
        this.sortTime = 0;
    }
}

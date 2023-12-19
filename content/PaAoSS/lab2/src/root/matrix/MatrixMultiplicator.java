package root.matrix;

import java.util.ArrayList;

public final class MatrixMultiplicator {
    private ArrayList<Matrix> matrixList;

    private String route;

    private Matrix result;

    public MatrixMultiplicator() {}

    public MatrixMultiplicator(ArrayList<Matrix> matrixList) {
        this.matrixList = matrixList;
    }

    public ArrayList<Matrix> getMatrixList() {
        return this.matrixList;
    }

    public void setMatrixList(ArrayList<Matrix> matrixList) {
        this.matrixList = matrixList;

        this.route = null;
        this.result = null;
    }

    public String getRoute() {
        return this.route;
    }

    public Matrix getResult() {
        return this.result;
    }

    public final int calculate() {
        this.route = MultiplicationRouter.getRoute(this.matrixList);
        
        
        return 0;
    }
}

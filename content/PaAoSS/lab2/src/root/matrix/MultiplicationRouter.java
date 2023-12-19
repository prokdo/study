package root.matrix;

import java.util.ArrayList;

public final class MultiplicationRouter {
    private MultiplicationRouter() {}

    private static final ArrayList<Integer> getSizes(ArrayList<Matrix> matrixList) {
        ArrayList<Integer> result = new ArrayList<>();

        result.add(matrixList.get(0).getWidth());
        result.add(matrixList.get(0).getLength());

        for (int i = 1; i < matrixList.size(); i++) {
            result.add(matrixList.get(i).getLength());
        }

        return result;
    }

    private static final void getRouteImpl(Integer[][] pathMatrix, String route, int i, int j) {
        if (i == j) {
            route += "A" + i;
        } else {
            route += "(";
            getRouteImpl(pathMatrix, route, i, pathMatrix[i][j]);
            route += "*";
            getRouteImpl(pathMatrix, route, pathMatrix[i][j] + 1, j);
            route += ")";
        }
    }

    public static final String getRoute(ArrayList<Matrix> matrixList) {
        String route = new String();

        ArrayList<Integer> sizes = getSizes(matrixList);

        int n = sizes.size() - 1;
		Integer[][] costMatrix = new Integer[n][n];
        Integer[][] pathMatrix = new Integer[n][n];
		
		for (int i = 0; i < n; ++i) {
			costMatrix[i][i] = 0;
            pathMatrix[i][i] = 0;
		}
		
		for (int l = 1; l < n; ++l) {
			for (int i = 0; i < n - l; ++i) {
				int j = i + l;
				costMatrix[i][j] = Integer.MAX_VALUE;
				for (int k = i; k < j; ++k) {
					int q = costMatrix[i][k] + costMatrix[k + 1][j] + sizes.get(i) * sizes.get(k + 1) * sizes.get(j + 1);
                    if (q < costMatrix[i][j]) {
                        costMatrix[i][j] = q;
                        pathMatrix[i][j] = k;
                    }                   
				}
			}
		}

        route += costMatrix[0][n - 1] + ": ";

        getRouteImpl(pathMatrix, route, 0, n - 1);

        return route;
    }
}

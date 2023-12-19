package root.lcs;

public final class LCSFinder {
    private String[] strings;
    private int[][] LCSMatrix;
    
    public LCSFinder(String[] strings) {
        this.strings = strings;
    }

    private final int[][] createLCSMatrix() {
        int[][] matrix = new int[this.strings[0].length()][this.strings[1].length()];

        for (int i = 0; i < this.strings[0].length(); i++) {
            for (int j = 0; j < this.strings[1].length(); j++) {
                if (this.strings[0].charAt(i) == this.strings[1].charAt(j)) {
                    matrix[i][j] = 1;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrix;
    }

    public final String find() {
        this.LCSMatrix = createLCSMatrix();

        StringBuilder LCS = new StringBuilder();

        int string1Index = this.strings[0].length() - 1;
        int string2Index = this.strings[1].length() - 1;

        try {
            while (string1Index >= 0 && string2Index >= 0) {
                if (strings[0].charAt(string1Index) == strings[1].charAt(string2Index)) {
                    LCS.append(this.strings[0].charAt(string1Index));
                    string1Index--;
                    string2Index--;
                }
                else 
                    if (this.LCSMatrix[string1Index - 1][string2Index] > this.LCSMatrix[string1Index][string2Index - 1])
                        string1Index--; 
                    else 
                        string2Index--;
            }
        } catch (Exception exception) {return null;}

        return LCS.reverse().toString();
    }
}

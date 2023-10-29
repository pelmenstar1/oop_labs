package lab2;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        var random = new Random() {
            @Override
            public double nextDouble() {
                return super.nextDouble() * 5;
            }
        };

        var mat = MutableMatrix.createReal(
            new double[] { 0, 1, 1 },
            new double[] { 1, 0, -2 },
            new double[] { -2, -3, 2 }
        );
        var invMat = mat.inverse();

        prettyPrintMatrix(mat);
        System.out.println();
        prettyPrintMatrix(invMat);
    }

    private static void prettyPrintMatrix(MutableMatrix<RealNumber> matrix) {
        MatrixDimension dimen = matrix.getDimension();

        for (int i = 0; i < dimen.getRowCount(); i++) {
            System.out.print("| ");

            for (int j = 0; j < dimen.getColumnCount(); j++) {
                System.out.print(matrix.get(i, j));
                System.out.print(" ");
            }

            System.out.println("|");
        }
    }
}
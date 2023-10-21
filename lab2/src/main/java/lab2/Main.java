package lab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Operation: ");
        System.out.println("1. Add");
        System.out.println("2. Multiply by scalar");
        System.out.println("3. Multiply matrices");
        System.out.println("4. Transpose");
        System.out.println("5. Invert");

        var inReader = new BufferedReader(new InputStreamReader(System.in));

        int choice = Integer.parseInt(inReader.readLine());
        ImmutableMatrix<RealNumber> result;

        switch (choice) {
            case 1: { // Add
                var m1 = readMatrix(inReader);
                var m2 = readMatrix(inReader);
                result = m1.plus(m2);

                break;
            }
            case 2: { // Multiply by scalar
                var m = readMatrix(inReader);
                System.out.print("Scalar: ");
                double scalar = Double.parseDouble(inReader.readLine());

                result = m.multiplyBy(new RealNumber(scalar));
                break;
            }
            case 3: { // Multiply matrices
                var m1 = readMatrix(inReader);
                var m2 = readMatrix(inReader);
                result = m1.multiplyBy(m2);
                break;
            }
            case 4: { // Transpose
                var m = readMatrix(inReader);
                result = m.transposed();
                break;
            }
            case 5: { // Invert
                var m = readMatrix(inReader);
                result = m.inverse();

                break;
            }
            default:
                throw new RuntimeException("Invalid choice");
        }

        System.out.println("Result:");
        prettyPrintMatrix(result);
    }

    private static ImmutableMatrix<RealNumber> readMatrix(BufferedReader reader) throws IOException {
        MatrixDimension dimen = readMatrixDimension(reader);
        System.out.println("Way to fill a matrix: ");
        System.out.println("1. With numbers");
        System.out.println("2. Identity");
        System.out.println("3. Diagonal");
        System.out.println("4. Row-random");
        System.out.println("5. Column-random");

        int choice = Integer.parseInt(reader.readLine());
        var random = new Random();

        ImmutableMatrix<RealNumber> result;

        switch (choice) {
            case 1: // With numbers
                System.out.println("Matrix: ");
                return readExactMatrix(dimen, reader);
            case 2: // Identity
            case 3: // Diagonal
                if (!dimen.hasSameComponents()) {
                    throw new RuntimeException("The matrix is not square");
                }
                int size = dimen.getRowCount();

                if (choice == 2) {
                    result = ImmutableMatrix.createIdentity(RealNumber.support(), size);
                } else {
                    var numbers = readRealNumberRow(reader);

                    result = ImmutableMatrix.createDiagonal(RealNumber.support(), numbers);
                }
                break;
            case 4: { // Row-random
                if (dimen.getRowCount() != 1) {
                    throw new RuntimeException("The matrix is not row-like");
                }

                result = ImmutableMatrix.createRandomRowMatrix(RealNumber.support(), dimen.getColumnCount(), random);
                break;
            }
            case 5: { // Column-random
                if (dimen.getColumnCount() != 1) {
                    throw new RuntimeException("The matrix is not column-like");
                }

                result = ImmutableMatrix.createRandomColumnMatrix(RealNumber.support(), dimen.getRowCount(), random);
                break;
            }
            default:
                throw new RuntimeException("Invalid choice");
        }

        System.out.println("Matrix: ");
        prettyPrintMatrix(result);

        return result;
    }

    private static MatrixDimension readMatrixDimension(BufferedReader reader) throws IOException {
        System.out.print("Rows: ");
        int rows = Integer.parseInt(reader.readLine());

        System.out.print("Columns: ");
        int columns = Integer.parseInt(reader.readLine());

        return new MatrixDimension(rows, columns);
    }

    private static ImmutableMatrix<RealNumber> readExactMatrix(
        MatrixDimension dimen,
        BufferedReader reader
    ) throws IOException {
        var data = new RealNumber[dimen.getRowCount()][];

        for (int i = 0; i < dimen.getRowCount(); i++) {
            try {
                var rows = readRealNumberRow(reader);

                if (rows.length != dimen.getColumnCount()) {
                    throwInvalidMatrixFormat();
                }

                data[i] = rows;
            } catch (NumberFormatException e) {
                throwInvalidMatrixFormat();
            }
        }

        return new ImmutableMatrix<>(RealNumber.support(), data);
    }

    private static RealNumber[] readRealNumberRow(BufferedReader reader) throws IOException {
        return Arrays.stream(reader.readLine().split(" "))
            .map(x -> new RealNumber(Double.parseDouble(x)))
            .toArray(RealNumber[]::new);
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

    private static void throwInvalidMatrixFormat() {
        throw new RuntimeException("Invalid matrix format");
    }
}
package lab2;

class Preconditions {
    private Preconditions() {
    }

    public static void ensureValidDimension(int value, String argName) {
        if (value <= 0) {
            throw new IllegalArgumentException(argName + " cannot be non-positive");
        }
    }

    public static void ensureValidIndexComponent(int value, int max, String argName) {
        if (value < 0 || value >= max) {
            throw new IndexOutOfBoundsException(argName + " value is out of bounds");
        }
    }

    public static void ensureSameDimensions(MatrixDimension a, MatrixDimension b) {
        if (!a.equals(b)) {
            throw new IllegalArgumentException("Expected same dimensions");
        }
    }
}

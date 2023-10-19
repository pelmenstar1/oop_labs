package lab2;

class Preconditions {
    private Preconditions() {
    }

    public static void ensureValidDimension(int value, String argName) {
        if (value <= 0) {
            throw new IllegalArgumentException(argName + " cannot be non-positive");
        }
    }

    public static void ensureNonNull(Object value, String argName) {
        if (value == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
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

    public static void ensureSquareDimension(MatrixDimension dimen) {
        if (!dimen.hasSameComponents()) {
            throw new IllegalStateException("Expected square dimensions");
        }
    }

    public static void ensureValidLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length cannot be negative");
        }
    }
}

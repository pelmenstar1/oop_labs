package lab2;

class Preconditions {
    private Preconditions() {
    }

    public static void ensureValidDimension(int value, String argName) {
        if (value <= 0) {
            throw new IllegalArgumentException(argName + " cannot be non-positive");
        }
    }
}

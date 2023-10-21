package lab2;

public class DoubleUtils {
    private static double normalizeZero(double value) {
        return value == -0.0 ? 0.0 : value;
    }

    public static boolean normalizedEquals(double a, double b) {
        return Double.doubleToLongBits(normalizeZero(a)) == Double.doubleToLongBits(normalizeZero(b));
    }

    public static int normalizedHashCode(double value) {
        return Double.hashCode(normalizeZero(value));
    }
}

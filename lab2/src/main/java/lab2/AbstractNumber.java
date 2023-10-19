package lab2;

public interface AbstractNumber<T extends AbstractNumber<T>> {
    T plus(T other);
    T multiply(T other);

    T inverse();
    T negated();

    default T minus(T other) {
        return plus(other.negated());
    }

    boolean equalsApproximately(T other, T precision);
}

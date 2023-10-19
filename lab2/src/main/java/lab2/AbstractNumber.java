package lab2;

public interface AbstractNumber<T extends AbstractNumber<T>> extends Comparable<T> {
    T plus(T other);
    T minus(T other);
    T multiply(T other);
    T inverse();
    T absolute();
}

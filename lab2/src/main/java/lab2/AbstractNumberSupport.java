package lab2;

import java.util.Random;

public interface AbstractNumberSupport<T extends AbstractNumber<? super T>> {
    T getZero();
    T getOne();
    T getEpsilon();
    T[] newArray(int size);
    T randomNumber(Random random);
    Class<T> getNumberClass();
}

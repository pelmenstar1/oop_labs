package lab2;

import java.util.Arrays;
import java.util.Random;

public interface AbstractNumberSupport<T extends AbstractNumber<? super T>> {
    T getZero();
    T getOne();
    T getPrecision();
    T[] newArray(int size);
    T randomNumber(Random random);
    Class<T> getNumberClass();

    default T[] newZeroArray(int size) {
        T[] array = newArray(size);
        Arrays.fill(array, getZero());

        return array;
    }
}

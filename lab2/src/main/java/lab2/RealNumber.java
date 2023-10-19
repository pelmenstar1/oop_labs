package lab2;

import java.util.Random;

public class RealNumber implements AbstractNumber<RealNumber> {
    private static final class Support implements AbstractNumberSupport<RealNumber> {
        @Override
        public RealNumber getZero() {
            return new RealNumber(0);
        }

        @Override
        public RealNumber getOne() {
            return new RealNumber(1);
        }

        @Override
        public RealNumber getEpsilon() {
            return new RealNumber(1e-7);
        }

        @Override
        public RealNumber[] newArray(int size) {
            return new RealNumber[size];
        }

        @Override
        public RealNumber randomNumber(Random random) {
            return new RealNumber(random.nextDouble());
        }

        @Override
        public Class<RealNumber> getNumberClass() {
            return RealNumber.class;
        }
    }

    private static final Support SUPPORT = new Support();

    private final double value;

    public RealNumber(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static AbstractNumberSupport<RealNumber> support() {
        return SUPPORT;
    }

    @Override
    public RealNumber plus(RealNumber other) {
        return new RealNumber(value + other.value);
    }

    @Override
    public RealNumber minus(RealNumber other) {
        return new RealNumber(value - other.value);
    }

    @Override
    public RealNumber multiply(RealNumber other) {
        return new RealNumber(value * other.value);
    }

    @Override
    public RealNumber inverse() {
        return new RealNumber(1.0 / value);
    }

    @Override
    public RealNumber absolute() {
        return new RealNumber(Math.abs(value));
    }

    @Override
    public int compareTo(RealNumber o) {
        return Double.compare(value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RealNumber number && value == number.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
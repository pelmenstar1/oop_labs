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
        public RealNumber getPrecision() {
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
    public RealNumber multiply(RealNumber other) {
        return new RealNumber(value * other.value);
    }

    @Override
    public RealNumber inverse() {
        return new RealNumber(1.0 / value);
    }

    @Override
    public RealNumber negated() {
        return new RealNumber(-value);
    }

    @Override
    public boolean equalsApproximately(RealNumber other, RealNumber precision) {
        return Math.abs(value - other.value) < precision.value;
    }

    @Override
    public int compareMagnitude(RealNumber other) {
        return Double.compare(Math.abs(value), Math.abs(other.value));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RealNumber other && DoubleUtils.normalizedEquals(value, other.value);
    }

    @Override
    public int hashCode() {
        return DoubleUtils.normalizedHashCode(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}

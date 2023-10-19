package lab2;

import java.util.Random;

public class ComplexNumber implements AbstractNumber<ComplexNumber> {
    private static final class Support implements AbstractNumberSupport<ComplexNumber> {
        @Override
        public ComplexNumber getZero() {
            return new ComplexNumber(0.0);
        }

        @Override
        public ComplexNumber getOne() {
            return new ComplexNumber(1.0);
        }

        @Override
        public ComplexNumber getEpsilon() {
            return new ComplexNumber(1e-7, 1e-7);
        }

        @Override
        public ComplexNumber[] newArray(int size) {
            return new ComplexNumber[size];
        }

        @Override
        public ComplexNumber randomNumber(Random random) {
            return new ComplexNumber(random.nextDouble(), random.nextDouble());
        }

        @Override
        public Class<ComplexNumber> getNumberClass() {
            return ComplexNumber.class;
        }
    }

    private static final Support SUPPORT = new Support();

    private final double real;
    private final double imaginary;

    public ComplexNumber(double real) {
        this(real, 0);
    }

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static AbstractNumberSupport<ComplexNumber> support() {
        return SUPPORT;
    }

    public double getRealValue() {
        return real;
    }

    public double getImaginaryValue() {
        return imaginary;
    }

    @Override
    public ComplexNumber plus(ComplexNumber other) {
        return new ComplexNumber(real + other.real, imaginary + other.imaginary);
    }

    @Override
    public ComplexNumber multiply(ComplexNumber other) {
        double x = real;
        double y = imaginary;
        double u = other.real;
        double v = other.imaginary;

        return new ComplexNumber(
            x * u - y * v,
            x * v + y * u
        );
    }

    @Override
    public ComplexNumber inverse() {
        double x = real;
        double y = imaginary;

        double mag = x * x + y * y;
        double newReal = x / mag;
        double newIm = -y / mag;

        return new ComplexNumber(newReal, newIm);
    }

    @Override
    public ComplexNumber negated() {
        return new ComplexNumber(-real, -imaginary);
    }

    @Override
    public boolean equalsApproximately(ComplexNumber other, ComplexNumber precision) {
        return Math.abs(real - other.real) < precision.real &&
            Math.abs(imaginary - other.imaginary) < precision.imaginary;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComplexNumber number) {
            return real == number.real && imaginary == number.imaginary;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(real) * 31 + Double.hashCode(imaginary);
    }

    @Override
    public String toString() {
        return "(" + real + ";" + imaginary + ")";
    }
}

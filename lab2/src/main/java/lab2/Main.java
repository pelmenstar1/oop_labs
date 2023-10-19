package lab2;

public class Main {
    public static void main(String[] args) {
        ImmutableMatrix.createIdentity(ComplexNumber.support(), 4);
        var mat = new ImmutableMatrix<>(ComplexNumber.support(), 1, 2);
        mat.multiplyBy(new ComplexNumber(2.0));
    }
}
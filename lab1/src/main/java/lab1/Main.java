package lab1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        System.out.print("Numbers: ");

        try {
            String numbers = new BufferedReader(new InputStreamReader(System.in)).readLine();

            int result = new StringCalculator().add(numbers);

            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
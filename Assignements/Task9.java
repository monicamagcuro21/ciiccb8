package Assignements;

import static java.lang.Math.*;   // Static import of Math class methods
import java.util.Scanner;

public class Task9 {

    public static int add(int a, int b) {
        return addExact(a, b);  // Using Math.addExact()
    }

    public static int subtract(int a, int b) {
        return subtractExact(a, b);  // Using Math.subtractExact()
    }

    public static int multiply(int a, int b) {
        return multiplyExact(a, b);  // Using Math.multiplyExact()
    }

    public static float divide(int a, int b) {
        return (float) floorDiv(a, b);  // Using Math.floorDiv()
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Enter 1st integer:");
        int a = in.nextInt();
        System.out.println("Enter 2nd integer:");
        int b = in.nextInt();
    

        System.out.println("Addition: " + add(a, b));
        System.out.println("Subtraction: " + subtract(a, b));
        System.out.println("Multiplication: " + multiply(a, b));
        System.out.println("Division (floor): " + divide(a, b));
    }
}


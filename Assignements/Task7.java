package Assignements;

import java.util.Scanner;

public class Task7 {


     public static void toSum(double a, double b){
            System.out.println(a+b);
        };
    public static void toDiff(double a, double b){
         System.out.println(a-b);
    };
    public static void toDiv(double a, double b){
         System.out.println(a/b);
    };
    public static void toMul(double a, double b){
         System.out.println(a*b);
    };

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);


        System.out.print("Enter 1st int: ");
        double a = input.nextDouble();
        System.out.print("Enter 2nd int: ");
        double b = input.nextDouble();

        toSum(a, b);
        toDiff(a, b);
        toMul(a, b);
        toDiv(a, b);
    }
}

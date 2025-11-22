package Assignements;

import java.util.Scanner;

public class Task5 {

    public static void main(String[] args) {

        try {
            double one, two, three;
            Scanner input = new Scanner(System.in);
            System.out.println("Lets check the highest number:");
            System.out.print("Enter 1st num:");
            one = input.nextDouble();
            System.out.print("Enter 2nd num:");
            two = input.nextDouble();
            System.out.print("Enter 3rd num:");
            three = input.nextDouble();

            if (one > two && one > three) {
                System.out.println("The highest number is:" + one);
            } else if (two > one && two > three) {
                System.out.println("The highest number is:" + two);
            } else if (three > one && three > two) {
                System.out.println("The highest number is:" + three);
            } else if (one == two && one == three && one == three) {
                System.out.println("All numbers are equal!");
            }

        } catch (Exception e) {
            System.out.println("Please enter a valid number!!");
        }
    }
}

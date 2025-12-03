package Challenge;
import java.util.Scanner;

public class dayofweek {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a number 1 to 7:");
        int week = in.nextInt();

        switch (week) {
            case 1:
            System.out.println("Monday");
                break;
            case 2:
            System.out.println("Tuesday");
                break;
            case 3:
            System.out.println("Wednesday");
                break;
            case 4:
            System.out.println("Thursday");
                break;
            case 5:
            System.out.println("Friday");
                break;
            case 6:
            System.out.println("Saturday");
                break;
            case 7:
            System.out.println("Sunday");
                break;
            default:
                System.out.println("Please enter a number from 1 to 7!");
        }
    }
}

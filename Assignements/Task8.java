package Assignements;

import java.util.Scanner;

public class Task8 {
    
    static void numbers(int... nums){
        int totalSum = 0;

        for(int n : nums){
        int cnum = n*(n+1)/2;
        System.out.println(n + " >>> "+ cnum);
        totalSum +=n;
    }
System.out.println("Total sum : "+ totalSum);
    }

    

    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Number: ");
        int i = in.nextInt();
        System.out.println("Enter Number: ");
        int e = in.nextInt();
        System.out.println("Enter Number: ");
        int o = in.nextInt();

        numbers(i,e,o);
        
    }
}

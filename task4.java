import java.util.Scanner;


public class task4 {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter a word: ");
        String words = in.nextLine();

        StringBuilder sb = new StringBuilder(words);
        String rev = sb.reverse().toString().toLowerCase();

      

       if(words.equals(rev) == true){
        System.out.println("This word is a palindrome");
       }else{
        System.out.println("This is not a palindrome");
       }

      
        

        
        


        
    }
}

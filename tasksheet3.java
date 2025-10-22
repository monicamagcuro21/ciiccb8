public class tasksheet3 {
public static void main(String[] args) {
    int i = 1;
    do {

        String message = (i%2==0? i+ " is even number": i+ " is odd number");
        i++;
        System.out.println(message);
     }while(i<11);
}
}

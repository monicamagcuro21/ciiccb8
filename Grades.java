import java.util.Scanner;
public class Grades{
    private String subject;
    private double grade;

    public Grades(String subject,double grade ){
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject(){
        return subject;
    }

    public double getGrade(){
        return grade;
    }
    @Override
    public String toString(){
        return subject + ": "+ grade;
    }
public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    double eng, math, scie, fil, java, ave;

    System.out.print("English: ");
     eng = in.nextDouble();
    Grades english = new Grades("English", eng);
    if(eng> 100.00){
        System.out.println("kindly input a valid grades!");
        System.exit(0);
    }
    System.out.print("Math: ");
     math = in.nextDouble();
    Grades mat = new Grades("Math", math);
    if(math> 100.00){
        System.out.println("kindly input a valid grades!");
        System.exit(0);
    }
    System.out.print("Science: ");
     scie = in.nextDouble();
    Grades science = new Grades("Science", scie);
    if(scie> 100.00){
        System.out.println("kindly input a valid grades!");
        System.exit(0);
    }
    System.out.print("Filipino: ");
     fil = in.nextDouble();
    Grades filipino = new Grades("Filipino", fil);
    if(fil> 100.00){
        System.out.println("kindly input a valid grades!");
        System.exit(0);
    }
    System.out.print("Java: ");
     java = in.nextDouble();
    Grades jav = new Grades("Java", java);
    if(java > 100.00){
        System.out.println("kindly input a valid grades!");
        System.exit(0);
    }
    ave = (((eng+math+scie+fil+java)/5.0)*1.0)/1.0;
    
    System.out.println(" - - - - - - - - - - - - -");

   System.out.println(english.toString());
   System.out.println(mat.toString());
   System.out.println(science.toString());
   System.out.println(filipino.toString());
   System.out.println(jav.toString());
     System.out.println(" - - - - - - - - - - - - -");
   System.out.println("Average: " + ave);
   System.out.println(" - - - - - - - - - - - - -");
   

   if(ave >= 90.00){
    System.out.println("You received an A");
   }else if(ave>=80 && ave >= 89 ){
    System.out.println("You received a B");
   }else if(ave>=70 && ave >= 79 ){
    System.out.println("You received a C");
   }else if(ave>=60 && ave >= 69 ){
    System.out.println("You received a D");
   }
   else if(ave>=60 && ave >= 69 ){
    System.out.println("You received a D");
   }else {
    System.out.println("you failed");
   }
   System.out.println(" - - - - - - - - - - - - -");
   
}
  
}

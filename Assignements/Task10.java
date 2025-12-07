package Assignements;

class Student{
    private String firstName;
    private String lastName;
    public Student(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void printFullName(){
        System.out.println(firstName+ " "+lastName);
    }
}
public class Task10 {
   public static void main(String[] args) {
    Student[] students = new Student[]{
        new Student("Monica", "Magcuro"),
        new Student("Brad", "Pitt"),
        new Student("Mon","Mon"),

    };
for (Student s:students){
    s.printFullName();
}
   }
}

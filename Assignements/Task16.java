package Assignements;


interface Animal {
    boolean feed(boolean timeToEat);
    void groom();
    void pet();
}


class Gorilla implements Animal {

    @Override
    public boolean feed(boolean timeToEat) {
        if (timeToEat) {
            
            System.out.println("// put gorilla food into cage");
            return true;
        } else {
            System.out.println("It's not time to eat.");
            return false;
        }
    }

    @Override
    public void groom() {
      
        System.out.println("// lather, rinse, repeat");
    }

   
    @Override
    public void pet() {
       
        System.out.println("// pet at your own risk");
    }
}


public class Task16 {
    public static void main(String[] args) {
   
        Animal myAnimal = new Gorilla();

        System.out.println("--- Gorilla Actions ---");
        
        myAnimal.feed(true);
        myAnimal.groom();
        myAnimal.pet();
    }
}
package Assignements;
class Vehicle {

    private String make;
    private String model;
    private int year;

   
    public Vehicle(String make, String model, int year) { 
        this.make = make;
        this.model = model;
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }
}

class Car extends Vehicle {
    private int numberOfDoors;

    public Car(String make, String model, int year, int numberOfDoors) {
        
        super(make, model, year); 
        this.numberOfDoors = numberOfDoors;
    }

    public void displayDetails() {
        System.out.println("Car Details:");
        System.out.println("Make: " + getMake());
        System.out.println("Model: " + getModel());
        System.out.println("Year: " + getYear());
        System.out.println("Number of Doors: " + numberOfDoors);
    }
}


public class Task13 {
    public static void main(String[] args) {
        Car myCar = new Car("Lexus", "GX", 2025, 4);
        
     
        myCar.displayDetails(); 
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Certification{
	private String name;
		private double price;
	
	public Certification(String s,double d){
			name = s;
			price = d;
		}
	
	public double getPrice(){
			return price;
		}
	
	public String getName(){
			return name;
		}
	}
	
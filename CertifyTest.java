
	//Assume necessary imports have done

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CertifyTest {
	
		public static void main(String[] args) {
	
			List<Certification> certs = new ArrayList<Certification>();

			certs.add(new Certification("1Z0-803",120));
			certs.add(new Certification("1Z0-804",250));
		certs.add(new Certification("1Z0-805",175));
		certs.add(new Certification("1Z0-808",150));
			certs.add(new Certification("1Z0-810",225));

			Predicate <Certification>  filter = (c) -> {return c.getPrice() > 180;};	for(Certification c : certs){
				if(filter.test(c)){
					System.out.println(c.getName());
				}
			}
		}

	}
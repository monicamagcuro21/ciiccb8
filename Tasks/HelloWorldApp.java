public class Prog {
	class Test{
	    public static void main(String args[]){
			try{
		   		new Test().meth();
		}catch(ArithmeticException e){
				System.out.print("Arithmetic");
			}finally{
				System.out.print("final 1");
			}catch(Exception e){
				System.out.print("Exception");
			}finally{
				System.out.print("final 2");
		}
		}
		
		public void meth()throws  ArithmeticException{
		for(int x=0;x<5;x++){
			int y = (int)5/x;
				System.out.print(x);
			}
		}
	}
}

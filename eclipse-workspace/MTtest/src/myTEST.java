public class myTEST implements Runnable {

//public class myTEST {
	Runnable r = new Runnable() {
		  public void run() {
		    
		  }
		};
	
		Runnable r1 = new Runnable() {
			  public void run() {
			    try {
			      while (true) {
			        System.out.println("Hello, world!");
			        Thread.sleep(1000L);
			      }
			    } catch (InterruptedException iex) {}
			  }
			};
			Runnable r2 = new Runnable() {
			  public void run() {
			    try {
			      while (true) {
			        System.out.println("Goodbye, " +
					"cruel world!");
			        Thread.sleep(2000L);
			      }
			    } catch (InterruptedException iex) {}
			  }
			};
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}	
}

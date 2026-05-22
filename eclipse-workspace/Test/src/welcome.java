public class welcome implements Runnable {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
//		Thread thr1 = new Thread(r1);
//		thr1.start();

		Thread [] threads = new Thread[/*2*/10];
	    for ( int i = 0; i < threads.length; i++ ) {
	      // Make each thread.
	      threads[i] = new Thread(new welcome());
	      // Start it.
	      threads[i].start();
	    }
	    // Wait for them to finish.
	    for ( int i = 0; i < threads.length; i++ ) {
	      // Wait for completion.
	      threads[i].join();
	    }	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
			    try {
			      while (true) {
			        System.out.println("Hello, world!");
			        Thread.sleep(1000L);
			      }
			    } catch (InterruptedException iex) {}
			  }		
	}



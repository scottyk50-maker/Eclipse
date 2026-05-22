import java.util.concurrent.atomic.AtomicInteger;

public class HelloWorld implements Runnable {
	  // The words.
	  private final String[] words;
	  // Which word to print next.
	  private final AtomicInteger whichWord;
	  // Cycles remaining.
	  private final AtomicInteger cycles;

	  private HelloWorld(String[] words, AtomicInteger whichWord, AtomicInteger cycles) {
	    // The words to print.
	    this.words = words;
	    // The Atomic holding the next word to print.
	    this.whichWord = whichWord;
	    // How many times around we've gone.
	    this.cycles = cycles;
	  }

	  @Override
	  public void run() {
	    // Until cycles are complete.
	    while ( cycles.get() > 0 ) {
	      // Must transit from this word
	      int thisWord = whichWord.get();
	      // to the next word.
	      int nextWord = thisWord + 1;
	      // Are we cycling?
	      boolean cycled = false;
	      if ( nextWord >= words.length ) {
	        // We cycled!
	        cycled = true;
	        // Back to zero.
	        nextWord = 0;
	      }
	      // Grab hold of System.out to ensure no race there either.
	      synchronized ( System.out ) {
	        // Atomically step the word number - must still be at thisWord for the step calculations to still be correct.
	        if ( whichWord.compareAndSet(thisWord, nextWord)) {
	          // Success!! We are the priveliged one!
	          System.out.print(words[thisWord]);
	          // Count the cycles.
	          if ( cycled ) {
	            // Just step it down.
	            cycles.decrementAndGet();
	            System.out.println(cycles);
	          }
	        }
	      }
	    }
	  }

	  public static void test() throws InterruptedException {
	    // The words to print.
	    String [] words = {"Hello ", "world. "};
	    // Which word to print next (start at 0 obviously).
	    AtomicInteger whichWord = new AtomicInteger(0);
	    // How many cycles to print - 6 as specified.
	    AtomicInteger cycles = new AtomicInteger(50);
	    // My threads - as many as I like.
	    Thread [] threads = new Thread[/*2*/42];
	    for ( int i = 0; i < threads.length; i++ ) {
	      // Make each thread.
	      threads[i] = new Thread(new HelloWorld(words, whichWord, cycles));
	      // Start it.
	      threads[i].start();
	    }
	    // Wait for them to finish.
	    for ( int i = 0; i < threads.length; i++ ) {
	      // Wait for completion.
	      threads[i].join();
	    }
	  }

	  public static void main(String args[]) throws InterruptedException {
	    System.out.println("HelloWorld:Test");
	    test();
	  }

	}
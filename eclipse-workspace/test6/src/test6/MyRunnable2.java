package test6;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MyRunnable will count the sum of the number from 1 to the parameter
 * countUntil and then write the result to the console.
 * <p>
 * MyRunnable is the task which will be performed
 *
 * @author Lars Vogel
 *
 */
public class MyRunnable2 implements Runnable {
    private final long countUntil;
 //   private final String sit;

    MyRunnable2(long countUntil) {
        this.countUntil = countUntil;
//        this.sit = sit;
    }

    @Override
    public void run() {
        long sum = 0;
        for (long i = 1; i < countUntil; i++) {
            sum += i;
        }
        System.out.println(countUntil);
    }
    
 
    public class Main {
        private static final int NTHREDS = 10;

        public void main(String[] args) throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
            for (int i = 0; i < 500; i++) {
                Runnable worker = new MyRunnable2(10000000L + i);
                executor.execute(worker);
            }
            // This will make the executor accept no new threads
            // and finish all existing threads in the queue
            executor.shutdown();
            // Wait until all threads are finish
            executor.awaitTermination(countUntil, null);
            System.out.println("Finished all threads");
        }
    }    
    
    
}
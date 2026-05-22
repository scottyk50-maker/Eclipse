import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FileCopy {

  // The blocking queue that will store the file paths to be processed
  private final BlockingQueue<String> queue;
  // The number of consumer threads to create
  private final int numConsumers;
  // The executor service that will manage the consumer threads
  private final ExecutorService executor;

  public FileCopy(int numConsumers) {
    this.queue = new LinkedBlockingQueue<>();
    this.numConsumers = numConsumers;
    this.executor = Executors.newFixedThreadPool(numConsumers);
  }

  // This method adds a file path to the blocking queue
  public void addFile(String filePath) {
    queue.add(filePath);
  }

  // This method starts the consumer threads and waits for them to finish
  public void start() throws InterruptedException {
    // Create and start the consumer threads
    for (int i = 0; i < numConsumers; i++) {
      executor.submit(new Consumer(queue));
    }

    // Wait for the consumer threads to finish
    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.HOURS);
  }

  // This is the Callable class that will be executed by the consumer threads
  private static class Consumer implements Callable<Void> {

    // The blocking queue that stores the file paths to be processed
    private final BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
      this.queue = queue;
    }

    @Override
    public Void call() throws Exception {
      while (true) {
        // Take a file path from the blocking queue
        String filePath = queue.take();
        // If the file path is null, it means that the producer has finished adding file paths
        if (filePath == null) {
          break;
        }
        // Copy the file
        copyFile(filePath);
      }
      return null;
    }

    // This method copies the file with the given file path
    private void copyFile(String filePath) throws IOException {
      FileInputStream in = new FileInputStream(filePath);
      FileOutputStream out = new FileOutputStream(filePath + ".copy");

      byte[] buffer = new byte[1024];
      int len;
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }

      in.close();
      out.close();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    // Create a FileCopier with 2 consumer threads

  }
}
	  
	  
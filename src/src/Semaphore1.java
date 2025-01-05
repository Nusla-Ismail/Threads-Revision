import java.util.concurrent.Semaphore;

public class Semaphore1 {
    // Semaphore initialized with 3 permits, allowing up to 3 threads to access the resource concurrently
    private final Semaphore semaphore = new Semaphore(3);

    // Simulate access to a shared resource
    public void accessResource(String threadName) {
        try {
            // Acquire a permit before accessing the shared resource
            semaphore.acquire();
            System.out.println(threadName + " is accessing the resource.");

            // Simulate resource access with a sleep
            Thread.sleep(2000);

            System.out.println(threadName + " has finished accessing the resource.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Release the permit after the resource is done
            semaphore.release();
        }
    }
}

class SemaphoreExample {
    public static void main(String[] args) {
        Semaphore1 resource = new Semaphore1();

        // Create multiple threads that will access the shared resource
        Thread t1 = new Thread(() -> resource.accessResource("Thread 1"));
        Thread t2 = new Thread(() -> resource.accessResource("Thread 2"));
        Thread t3 = new Thread(() -> resource.accessResource("Thread 3"));
        Thread t4 = new Thread(() -> resource.accessResource("Thread 4"));
        Thread t5 = new Thread(() -> resource.accessResource("Thread 5"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
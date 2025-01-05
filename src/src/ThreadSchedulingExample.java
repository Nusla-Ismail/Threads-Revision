public class ThreadSchedulingExample {

    public static void main(String[] args) {
        // Create two threads with different priorities
        Thread highPriorityThread = new Thread(new Task(), "HighPriorityThread");
        Thread lowPriorityThread = new Thread(new Task(), "LowPriorityThread");

        // Create the Tracker thread (extends Thread)
        Tracker trackerThread = new Tracker();

        // Set priorities: high-priority thread will finish faster
        highPriorityThread.setPriority(Thread.NORM_PRIORITY); // Highest priority
        lowPriorityThread.setPriority(Thread.NORM_PRIORITY); // Lowest priority
        trackerThread.setPriority(Thread.MAX_PRIORITY);

        // Start all threads
        highPriorityThread.start();
        lowPriorityThread.start();
        trackerThread.start();
    }

    // Task for the threads to run
    static class Task implements Runnable {
        @Override
        public void run() {
            synchronized (ThreadSchedulingExample.class) {
                // Each thread will perform a task that involves "doing work" for a while
                for (int i = 1; i <= 10; i++) {
                    System.out.println(Thread.currentThread().getName() + " - Step " + i);

                    try {
                        // Simulate work by making the thread sleep for a short period
                        Thread.sleep(100);  // Each step takes 100ms to complete
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " has finished!");
            }
        }
    }

    // Tracker thread that displays a message whenever it's selected
    static class Tracker extends Thread {
        @Override
        public void run() {


                    // Display a message that the tracker thread has been selected
                    System.out.println("Tracker thread has been selected!");


        }
    }
}

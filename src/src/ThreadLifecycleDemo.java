public class ThreadLifecycleDemo {
    public static void main(String[] args) {
        // NEW state
        Thread newThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getState());
            // RUNNABLE state
            System.out.println("Thread is in RUNNABLE state.");
            System.out.println(Thread.currentThread().getState());
            // Simulating some task
            for (int i = 0; i < 3; i++) {
                System.out.println("Executing task in RUNNABLE state: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // BLOCKED state (synchronized block)
            synchronized (ThreadLifecycleDemo.class) {
                System.out.println("Thread is in BLOCKED state (synchronized block).");
            }
            System.out.println(Thread.currentThread().getState());
            // WAITING state (waiting for a notification)
            synchronized (ThreadLifecycleDemo.class) {
                try {
                    System.out.println("Thread is in WAITING state (waiting for a notification).");
                    ThreadLifecycleDemo.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getState());
            // TIMED_WAITING state (sleeping for a specific time)
            try {
                System.out.println("Thread is in TIMED_WAITING state (sleeping for 3 seconds).");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getState());
            // TERMINATED state
            System.out.println("Thread is in TERMINATED state.");
        });
        // Starting the thread
        newThread.start();
        // Main thread
        for (int i = 0; i < 5; i++) {
            System.out.println("Main thread executing: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // NOTIFY the waiting thread to move from WAITING to RUNNABLE
        synchronized (ThreadLifecycleDemo.class) {
            System.out.println("Notifying the waiting thread.");
            ThreadLifecycleDemo.class.notify();
        }
        // Waiting for the newThread to complete
        try {
            newThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread is in TERMINATED state.");
    }
}
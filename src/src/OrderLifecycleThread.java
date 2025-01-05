class OrderLifecycleThread extends Thread {
    private static final Object inventoryLock = new Object();

    // Method to simulate receiving an order (NEW state)
    public void receiveOrder() {
        System.out.println(Thread.currentThread().getName() + " - Receiving order...");
    }

    // Method to simulate processing an order (RUNNABLE and BLOCKED states)
    public void processOrder() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " - Processing order...");
        Thread.sleep(1000); // Simulate time taken for processing (RUNNABLE state)
        System.out.println(Thread.currentThread().getName() + " - Order processed. Waiting for inventory update...");

        // Simulate BLOCKED state while waiting for inventory update
        // Here, we release the lock before calling wait()
        synchronized (inventoryLock) {
            System.out.println(Thread.currentThread().getName() + " - Blocked while waiting for inventory update.");
            // Now release the lock before calling wait
            inventoryLock.wait();  // The thread will wait here until inventory is updated
        }
    }

    // Method to simulate waiting for an inventory update (WAITING state)
    public void waitForInventoryUpdate() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " - Waiting for inventory update...");
        synchronized (inventoryLock) {
            inventoryLock.wait(); // Thread enters WAITING state
        }
    }

    // Method to simulate timed waiting for shipping (TIMED_WAITING state)
    public void waitForShipping() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " - Waiting for shipping confirmation...");
        Thread.sleep(2000);  // Thread enters TIMED_WAITING state for 2 seconds
    }

    // Method to complete the order and reach the TERMINATED state
    public void completeOrder() {
        System.out.println(Thread.currentThread().getName() + " - Order completed. Terminating thread.");
    }

    @Override
    public void run() {
        try {
            // 1. Receiving orders (NEW state)
            receiveOrder();

            // 2. Processing orders (RUNNABLE and BLOCKED states)
            processOrder();

            // 3. Waiting for inventory update (WAITING state)
            waitForInventoryUpdate();

            // 4. Timed waiting for shipping (TIMED_WAITING state)
            waitForShipping();

            // 5. Completing the order (TERMINATED state)
            completeOrder();

        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " - Thread interrupted.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Create a thread to simulate the lifecycle of order processing
        OrderLifecycleThread orderThread = new OrderLifecycleThread();

        // 1. Start the thread (NEW state)
        System.out.println("Main thread - Starting Order Thread...");
        orderThread.start();

        // Simulating inventory update from another thread to unblock the waiting thread
        Thread.sleep(3000);  // Wait for some time before updating inventory
        synchronized (inventoryLock) {
            System.out.println("Main thread - Updating inventory...");
            inventoryLock.notify();  // Notify the waiting thread that inventory is updated
        }

        // Wait for the order thread to complete (TERMINATED state)
        orderThread.join();
        System.out.println("Main thread - Order processing completed.");
    }
}

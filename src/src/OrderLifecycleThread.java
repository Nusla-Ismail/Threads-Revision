public class OrderLifecycleThread extends Thread {
    // Shared resource to simulate inventory update
    private static boolean inventoryUpdated = false;

    // Simulate receiving an order (NEW state)
    public void receiveOrder() {
        System.out.println("Order received. Transitioning to RUNNABLE state.");
    }

    // Simulate processing an order (RUNNABLE and BLOCKED states)
    public synchronized void processOrder() throws InterruptedException {
        // RUNNABLE state
        System.out.println("Processing order. Transitioning to RUNNABLE state.");
        Thread.sleep(1000); // Simulate work being done in the RUNNABLE state

        // Simulate a scenario where the thread is BLOCKED by another thread
        System.out.println("Order processing is blocked due to inventory check.");
        while (!inventoryUpdated) {
            wait(); // Waiting for the inventory update (BLOCKED state)
        }

        // After receiving the inventory update, processing continues
        System.out.println("Order processed successfully. Transitioning to TERMINATED state.");
    }

    // Simulate waiting for an inventory update (WAITING state)
    public synchronized void waitForInventoryUpdate() throws InterruptedException {
        System.out.println("Waiting for inventory update. Transitioning to WAITING state.");
        while (!inventoryUpdated) {
            wait(); // The thread will wait until inventory is updated
        }
    }

    // Simulate timed waiting for shipping confirmation (TIMED_WAITING state)
    public void waitForShipping() throws InterruptedException {
        System.out.println("Waiting for shipping confirmation. Transitioning to TIMED_WAITING state.");
        Thread.sleep(2000); // Simulate waiting for a shipping confirmation
    }

    // Simulate the completion of the order processing (TERMINATED state)
    public void completeOrder() {
        System.out.println("Order completed. Transitioning to TERMINATED state.");
    }

    // Main method to demonstrate the thread's life cycle
    public static void main(String[] args) throws InterruptedException {
        OrderLifecycleThread orderThread = new OrderLifecycleThread();

        // Simulating the life cycle of the thread
        orderThread.start(); // NEW state -> RUNNABLE state

        // Simulating receiving an order
        orderThread.receiveOrder();

        // Simulating order processing (RUNNABLE and BLOCKED)
        orderThread.processOrder();

        // Simulating waiting for inventory update
        orderThread.waitForInventoryUpdate();

        // Simulating waiting for shipping confirmation (TIMED_WAITING)
        orderThread.waitForShipping();

        // Simulating the completion of the order processing (TERMINATED)
        orderThread.completeOrder();
    }

    @Override
    public void run() {
        // This thread simulates processing the order and handles state transitions
        try {
            // Receiving and processing orders
            receiveOrder();
            processOrder();

            // Waiting for inventory update
            waitForInventoryUpdate();

            // Waiting for shipping confirmation
            waitForShipping();

            // Completing the order
            completeOrder();
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        }
    }

    // Method to simulate an inventory update (used by another thread)
    public static synchronized void updateInventory() {
        inventoryUpdated = true;
        System.out.println("Inventory updated. Notifying waiting threads.");
        // Notify all threads waiting for the inventory update
        synchronized (OrderLifecycleThread.class) {
            OrderLifecycleThread.class.notifyAll(); // Notify the thread waiting for inventory
        }
    }
}

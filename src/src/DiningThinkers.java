import java.util.concurrent.Semaphore;

// ResourceMonitor class manages the shared resources (utensils)
class ResourceMonitor {
    final int NUM_PHILOSOPHERS = 5; // Number of thinkers
    private final Semaphore[] utensils = new Semaphore[NUM_PHILOSOPHERS]; // Semaphores for each utensil

    // Constructor: Initialize each utensil semaphore to 1 (available)
    public ResourceMonitor() {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            utensils[i] = new Semaphore(1); // 1 means the utensil is available
        }
    }

    // Method to pick up utensils (both left and right)
    public void acquireUtensils(int thinkerId) {
        try {
            // Wait to acquire the left and right utensils
            utensils[thinkerId].acquire(); // Left utensil
            utensils[(thinkerId + 1) % NUM_PHILOSOPHERS].acquire(); // Right utensil
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle interruption
        }
    }

    // Method to put down utensils (both left and right)
    public void releaseUtensils(int thinkerId) {
        // Release the left and right utensils
        utensils[thinkerId].release(); // Left utensil
        utensils[(thinkerId + 1) % NUM_PHILOSOPHERS].release(); // Right utensil
    }
}

// Thinker class represents each thinker's behavior
class Thinker implements Runnable {
    private final int thinkerId;
    private final ResourceMonitor resourceMonitor;

    public Thinker(int thinkerId, ResourceMonitor resourceMonitor) {
        this.thinkerId = thinkerId;
        this.resourceMonitor = resourceMonitor;
    }

    @Override
    public void run() {
        while (true) {
            contemplate();
            resourceMonitor.acquireUtensils(thinkerId); // Pick up utensils
            dine();
            resourceMonitor.releaseUtensils(thinkerId); // Put down utensils
        }
    }

    private void contemplate() {
        System.out.println("Thinker " + thinkerId + " is contemplating.");
        try {
            Thread.sleep((long) (Math.random() * 1000)); // Simulate contemplation time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void dine() {
        System.out.println("Thinker " + thinkerId + " is dining.");
        try {
            Thread.sleep((long) (Math.random() * 1000)); // Simulate dining time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main program to simulate the dining philosophers
public class DiningThinkers {
    public static void main(String[] args) {
        ResourceMonitor resourceMonitor = new ResourceMonitor();
        Thread[] thinkers = new Thread[5]; // Create 5 thinkers

        // Create and start thinker threads
        for (int i = 0; i < 5; i++) {
            thinkers[i] = new Thread(new Thinker(i, resourceMonitor));
            thinkers[i].start();
        }

        // Wait for all thinkers to finish (they run indefinitely in this simulation)
        for (int i = 0; i < 5; i++) {
            try {
                thinkers[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

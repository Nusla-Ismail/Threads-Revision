// Interface for the Mailbox
interface Mailbox {
    int take();
    void put(int value);
}

// Implementation of the Mailbox using a Monitor
class MailboxMonitor implements Mailbox {
    // Shared Resource
    private int contents = -1; // Encapsulated resource
    private boolean available = false; // Flag indicating availability

    // Monitor method to take an item
    public synchronized int take() {
        while (!available) { // Wait while the resource is unavailable
            try {
                wait(); // Add calling thread to the wait-set
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted while waiting to take an item.");
            }
        }
        available = false; // Change the state of the monitor
        notifyAll(); // Signal state change to waiting threads
        return contents;
    }

    // Monitor method to put an item
    public synchronized void put(int value) {
        while (available) { // Wait while the resource is already available
            try {
                wait(); // Add calling thread to the wait-set
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted while waiting to put an item.");
            }
        }
        contents = value; // Update the shared resource
        available = true; // Change the state of the monitor
        notifyAll(); // Signal state change to waiting threads
    }
}

// Producer class
class Producer extends Thread {
    private final Mailbox mailbox;
    private final int id;
    private final int itemCount;

    public Producer(Mailbox mailbox, int id, int itemCount) {
        this.mailbox = mailbox;
        this.id = id;
        this.itemCount = itemCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < itemCount; i++) {
            mailbox.put(i); // Produce an item
            System.out.println("Producer-" + id + " produced item: " + i);
        }
    }
}

// Consumer class
class Consumer extends Thread {
    private final Mailbox mailbox;
    private final int id;
    private final int itemCount;

    public Consumer(Mailbox mailbox, int id, int itemCount) {
        this.mailbox = mailbox;
        this.id = id;
        this.itemCount = itemCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < itemCount; i++) {
            int item = mailbox.take(); // Consume an item
            System.out.println("Consumer-" + id + " consumed item: " + item);
        }
    }
}

// Main program to demonstrate producer-consumer using a mailbox monitor
class ProdConMailboxMonitor {
    public static void main(String[] args) {
        final int NUM_ITEMS = 10;

        // Create a MailboxMonitor, Producer, and Consumer
        Mailbox mailbox = new MailboxMonitor();
        Producer producer = new Producer(mailbox, 1, NUM_ITEMS);
        Consumer consumer = new Consumer(mailbox, 1, NUM_ITEMS);

        // Start Producer and Consumer
        producer.start();
        consumer.start();
    }
}

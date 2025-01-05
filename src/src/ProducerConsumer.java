import java.util.ArrayList;

public class ProducerConsumer {
    private static volatile boolean running = true; // Volatile terminator flag
    private static final ArrayList<String> product = new ArrayList<>();
    public static void main(String[] args) {

        Thread producer1 = new Producer(product, 1);
        Thread producer2 = new Producer(product, 2);
        Consumer[] consumers = new Consumer[4];

        for (int i = 0; i < 4; i++) {
            consumers[i] = new Consumer(product, i);
        }

        producer1.start();
        producer2.start();

        for (Consumer consumer : consumers) {
            consumer.start();
        }

        // Stop the threads after 5 seconds
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        running = false; // Signal all threads to stop
        synchronized (product) {
            product.notifyAll(); // Notify all waiting threads to exit
        }

        System.out.println("Main thread has signaled termination.");
    }

    static class Producer extends Thread {
        private final ArrayList<String> product;

        public Producer(ArrayList<String> product, int id) {
            super("Producer-" + id);
            this.product = product;
        }

        public void produce() {
            while (running) {
                synchronized (product) {
                    while (!product.isEmpty() && running) {
                        try {
                            product.wait(); // Wait if the product list is not empty
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (!running) break; // Check termination flag before producing

                    // Produce items
                    for (int i = 0; i < 3; i++) {
                        product.add("product-" + i);
                    }
                    System.out.println(Thread.currentThread().getName() + " has added items.");
                    product.notifyAll(); // Notify all waiting threads
                }

                try {
                    Thread.sleep(100); // Simulate production time
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + " is terminating.");
        }

        @Override
        public void run() {
            produce();
        }
    }

    static class Consumer extends Thread {
        private final ArrayList<String> product;

        public Consumer(ArrayList<String> product, int id) {
            super("Consumer-" + id);
            this.product = product;
        }

        public void consume() {
            while (running) {
                synchronized (product) {
                    while (product.isEmpty() && running) {
                        try {
                            product.wait(); // Wait if the product list is empty
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (!running) break; // Check termination flag before consuming

                    // Consume items
                    for (String item : product) {
                        System.out.println(Thread.currentThread().getName() + " consumed " + item);
                    }
                    product.clear(); // Clear the product list after consumption
                    System.out.println(Thread.currentThread().getName() + " consumed all items.");
                    product.notifyAll(); // Notify all waiting threads
                }

                try {
                    Thread.sleep(100); // Simulate consumption time
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + " is terminating.");
        }

        @Override
        public void run() {
            consume();
        }
    }
}

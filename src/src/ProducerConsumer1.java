import java.util.LinkedList;
import java.util.Queue;

class ProducerConsumer1 {

    public static void main(String[] args) {
        Buffer buffer = new Buffer(5); // Buffer size is 5

        Thread producerThread = new Thread(new Producer1(buffer));
        Thread consumerThread = new Thread(new Consumer1(buffer));

        producerThread.start();
        consumerThread.start();
    }
}

class Buffer {
    private final Queue<Integer> queue;
    private final int maxSize;

    public Buffer(int maxSize) {
        this.queue = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public synchronized void produce(int value) throws InterruptedException {
        while (queue.size() == maxSize) {
            System.out.println("Buffer is full, producer is waiting...");
            wait();
        }
        queue.add(value);
        System.out.println("Produced: " + value);
        notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("Buffer is empty, consumer is waiting...");
            wait();
        }
        int value = queue.poll();
        System.out.println("Consumed: " + value);
        notifyAll();
    }
}

class Producer1 implements Runnable {
    private final Buffer buffer;

    public Producer1(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int value = 0;
        try {
            while (true) {
                buffer.produce(value++);
                Thread.sleep(500); // Simulate time taken to produce
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer interrupted.");
        }
    }
}

class Consumer1 implements Runnable {
    private Buffer buffer;

    public Consumer1(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consume();
                Thread.sleep(1000); // Simulate time taken to consume
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer interrupted.");
        }
    }
}

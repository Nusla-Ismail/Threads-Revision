import java.util.LinkedList;
import java.util.Queue;

public class Q2 {
    public static void main(String[] args) {
       Queue<Integer> numbers = new LinkedList<>();
       Thread t1 = new Processor(numbers);
        System.out.println(t1.getState()); //NEW state
        t1.start();

        try {
            System.out.println(t1.getState());
            Thread.sleep(200);
            t1.join();
            System.out.println(t1.getState()); //TERMINATED state
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread t2 = new Updater(numbers);
        Thread t3 = new Updater(numbers);
        t2.start(); //RUNNABLE or BLOCKED
        t3.start();
        System.out.println(t2.getState());
        System.out.println(t3.getState());
        Thread t4 = new Notifier();
        t4.start();
        System.out.println(t4.getState());

    }
}

class Processor extends Thread {
    final Queue<Integer> sharedResource;

    public Processor(Queue<Integer> sharedResource) {
        this.sharedResource = sharedResource;
    }

    public void put() {
        synchronized (sharedResource) {
            for (int i = 0; i < 3; i++) {
                sharedResource.add(i);
                System.out.println("Processor added: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void run() {
        put();
    }
}

class Updater extends Thread {
    final Queue<Integer> sharedResource;
    private boolean update = false;

    public Updater(Queue<Integer> sharedResource) {
        this.sharedResource = sharedResource;
    }

    public synchronized void take() {
        while (!update) {
            try {
                wait(); // WAITING state
                System.out.println(Thread.currentThread().getName() + " is waiting for resource..");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (!sharedResource.isEmpty()) {
            sharedResource.poll();
            System.out.println("Thread has updated");
        }
        notifyAll();
    }

    @Override
    public void run() {
        take();
    }
}

class Notifier extends Thread {
    private boolean notify = false;

    public synchronized void notifier() {
        while (!notify) {
            try {
                wait(1000); // TIMED_WAITING state
                System.out.println(Thread.currentThread().getName() + " is waiting for resource to notify..");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(Thread.currentThread().getName() + " has notified user");
        notifyAll();
    }

    @Override
    public void run() {
        notifier();
    }
}

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosopher {
    public static void main(String[] args) {
        int num = 5;
        Forks forks = new Forks();
        Thread[] philosophers = new Thread[num];

        for(int i=0; i<num;i++){
            philosophers[i] = new Thread(new Philosopher(i,forks));
        }
        for (Thread philosopher:philosophers){
            philosopher.start();
            try {
                philosopher.join(); // ensuring that philosophers uses
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
class Forks{
    final int num_phil = 5;
    private final int[] forks = new int[num_phil];
    private final Lock lock = new ReentrantLock();
    private final Condition[] useFork = new Condition[num_phil]; // Separate condition for each philosopher


    public Forks(){
        for (int i =0 ; i<num_phil;i++){ //1- available , 0- not available
            forks[i] = 1; // making all forks available initially
            useFork[i]= lock.newCondition(); // creating unique condition for each philosopher
        }
    }
    public void pickUpFork(int philID) {
        lock.lock();
        try {
            while (forks[philID] == 0 || forks[(philID + 1) % num_phil] == 0) { // left or right fork is in use
                useFork[philID].await();
                System.out.println("Philosopher-" + philID + " is waiting for the fork");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Philosopher-" + philID + " has acquired the fork");
        forks[philID] = 0; // assigning left fork as in use
        forks[(philID + 1) % num_phil] = 0; // assigning right fork as in use
        lock.unlock();
    }

    public void putDownFork(int philID){
        lock.lock();
        try {
            forks[philID] = 1; // assigning left fork as not in use
            forks[(philID + 1) % num_phil] = 1; // assigning right fork as not in use
            System.out.println("Philosopher-" + philID + " has finished using the fork");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}

class Philosopher implements Runnable{
    private final int philosopherId;
    private final Forks forks;

    public Philosopher(int philosopherId, Forks forks){
        this.philosopherId=philosopherId;
        this.forks=forks;
    }

    @Override
    public void run() {
        think();
        forks.pickUpFork(philosopherId);
        eat();
        forks.putDownFork(philosopherId);


    }
    private void think() {
        System.out.println("Philosopher " + philosopherId + " is thinking.");
        try {
            Thread.sleep((long) (Math.random() * 1000)); // Simulate thinking time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void eat() {
        System.out.println("Philosopher " + philosopherId + " is eating.");
        try {
            Thread.sleep((long) (Math.random() * 1000)); // Simulate eating time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
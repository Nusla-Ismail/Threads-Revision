public class Q1 {
    public static void main(String[] args) {
        Runnables run = new Runnables();
        Thread t1 = new Thread1();
        Thread t2= new Thread(run);

        t1.start();
        t2.start();
    }
}

class Thread1 extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName()+" is reading "+i);

        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Runnables implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName()+" is reading "+i);

        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
public class Deadlock extends Thread{
    private final static Object ball = new Object();
    private final static Object rope = new Object();

    public static void person(){
        synchronized (ball){
            System.out.println(Thread.currentThread().getName()+" has the ball");
            synchronized (rope){
                System.out.println(Thread.currentThread().getName()+" has the rope");
            }
        }


    }

    public static void dog(){
        synchronized (rope){
            System.out.println(Thread.currentThread().getName()+" has the rope");
            synchronized (ball){
                System.out.println(Thread.currentThread().getName()+" has the ball");
            }
        }


    }

}

class Simulator{
    public static void main(String[] args) {
        Thread t1 = new Thread(Deadlock::dog);
        Thread t2 = new Thread(Deadlock::person);

        t1.start();
        t2.start();
    }
}

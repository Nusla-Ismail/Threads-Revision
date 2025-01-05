import java.util.ArrayList;

public class OrderThreadLifecycle extends Thread{
    ArrayList<String> orders = new ArrayList<>();
    ArrayList<String> inventory = new ArrayList<>();
    private static final Object lock = new Object();
    private static boolean access = true;


    public void receiveOrder(){
        for (int i = 0; i < 3; i++) {
            orders.add("Order-"+i);
            System.out.println(orders.get(i)+ " has been received");
        }
        System.out.println("All the orders have been received by : "+ Thread.currentThread().getName());
        try {
            Thread.sleep(100); // simulating order receive time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void processOrder(){
        synchronized (lock){
            for(String  order:orders){
                System.out.println(order+ "has been processed");
            }
            try {
                Thread.sleep(100); // simulating order processing time
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            }
            System.out.println(Thread.currentThread().getName()+" has processed all the orders");
    }

    public void  waitForInventory(){
        synchronized (lock){
            while (!access){
            try {
                lock.wait();
                System.out.println(Thread.currentThread().getName()+" is waiting for the access");
                access=true;
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }

            }

            for (int i = 0; i < 5; i++) {
                inventory.add("Item-"+i);
                System.out.println("Inventory updated with item No. " + i);
                try {
                    Thread.sleep(1000); // Simulate processing time
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            access= false;
            lock.notifyAll();
            System.out.println("Inventory successfully updated");

        }
    }
    public synchronized void waitForShipping(){
        while (!access){
            try {
                System.out.println(Thread.currentThread().getName()+" is waiting for shipping");
                wait(1000);
                System.out.println(Thread.currentThread().getName()+" is out of the waiting state");
                access=true;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        access=false;
        notifyAll();

    }

    public synchronized void completeOrder(){
        System.out.println(Thread.currentThread().getName()+ " has finished its execution");

    }

    @Override
    public void run() {

    }
}

class  WareHouseSimulator {
    public static void main(String[] args) {
        OrderThreadLifecycle t1 =new OrderThreadLifecycle();
        OrderThreadLifecycle t2 =new OrderThreadLifecycle();

    }
}



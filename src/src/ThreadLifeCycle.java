import java.util.ArrayList;

public class ThreadLifeCycle {
    public static void main(String[] args) {
        ArrayList<String> orders= new ArrayList<>();
        ReceiveOrders receiveOrders = new ReceiveOrders(orders);
        ProcessOrders processOrders =new ProcessOrders(orders);
        ProcessOrders processOrders1 =new ProcessOrders(orders);
        System.out.println("The state of receiveOrders: "+ receiveOrders.getState()); //NEW state
        receiveOrders.start();
        try {
            receiveOrders.join();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        processOrders.start();//RUNNABLE state
        processOrders1.start(); //BLOCKED STATE
        System.out.println("The state of processOrders: "+ processOrders.getState());
        System.out.println("The state of processOrders1: "+ processOrders1.getState());

    }
}

class ReceiveOrders extends Thread{
    ArrayList<String> orders;
    public ReceiveOrders(ArrayList<String> orders){
        this.orders= orders;
    }
    public synchronized void receiveOrder(){
        for (int i = 0; i < 5; i++) {
            orders.add("Order no. "+ i);
        }
        System.out.println(Thread.currentThread().getName()+ " has received all the orders");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500); // simulating receive time
            receiveOrder();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ProcessOrders extends Thread{
    ArrayList<String> orders;
    public ProcessOrders (ArrayList<String> orders){
        this.orders= orders;
    }

    public synchronized void process(){
        System.out.println(Thread.currentThread().getName()+ " has started processing orders");
        for (String order:orders){
            System.out.println(order+ " is currently being processed");
        }
        System.out.println(Thread.currentThread().getName()+ " has processed all orders");
        try{

            Thread.sleep(1000000000); //simulating processing time

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
         process();
    }
}


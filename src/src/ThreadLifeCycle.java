import java.util.ArrayList;


public class ThreadLifeCycle {
    public static void main(String[] args) {
        ArrayList<String> orders = new ArrayList<>();
        ReceiveOrder receiveOrder = new ReceiveOrder(orders,"Order Receiver Thread");
        System.out.println("Order receiver thread is in the state: "+ receiveOrder.getState()); // this will print new state
        ProcessingOrders processingOrder = new ProcessingOrders("Order processing thread 1", orders);
        ProcessingOrders processingOrder1 = new ProcessingOrders("Order processing thread 2", orders);
        receiveOrder.start();

        try {
            receiveOrder.join(); // main thread waits for ReceiveOrder thread to finish
            processingOrder.start();
            processingOrder1.start();


        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        System.out.println("Order processing thread 1 is in the state: "+ processingOrder.getState()); // runnable
        System.out.println("Order processing thread 2 is in the state: "+ processingOrder1.getState()); // blocked


        try{
            Thread.sleep(1000); // Allow time for the first thread to lock and the second to get blocked
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}

class ReceiveOrder extends Thread {
    ArrayList<String> orders;
    public ReceiveOrder( ArrayList<String> orders,String name){
        super(name);
        this.orders=orders;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            orders.add("Order No. "+i);
            System.out.println(Thread.currentThread().getName()+ " has received the order no. "+ orders.get(i));
        }

        try{
            Thread.sleep(100); // Simulate receiving time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+ " has received all the orders");
    }
}

class ProcessingOrders extends Thread{
    ArrayList<String> orders;

    public ProcessingOrders(String name, ArrayList<String> orders){
        super(name);
        this.orders = orders;

    }

    public synchronized void processOrders(){

        while (orders.isEmpty()){
            try {
                wait(); // waiting for orders
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " is waiting for the orders");

        }

        notify();
        for(String order:orders){
            try{
                Thread.sleep(1000); // simulating processing time
                System.out.println(order+ " has been processed");
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        processOrders();
    }
}



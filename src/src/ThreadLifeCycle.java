import java.util.ArrayList;

public class ThreadLifeCycle {
    public static void main(String[] args) {
        ArrayList<String> orders = new ArrayList<>();
        ReceiveOrder receiveOrder = new ReceiveOrder("Order Receiver Thread");
        System.out.println("The state of the receiveOrder thread is: "+ receiveOrder.getState()); // this will print new state
        ProcessingOrders processingOrders = new ProcessingOrders(orders, "Order Processor Thread 1");
        ProcessingOrders processingOrders1 = new ProcessingOrders(orders, "Order Processor Thread 2");
        processingOrders1.start(); // Runnable State
        System.out.println(processingOrders1.getName()+ " is in the state: "+ processingOrders1.getState()); // Runnable state
        processingOrders.start(); // Blocked State ( because of main thread timeout)

        try{
            Thread.sleep(100); // making main thread sleep to catch the blocked state
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(processingOrders.getName()+ " is in the state: "+ processingOrders.getState());


    }
}

class ReceiveOrder extends Thread{
    public ReceiveOrder(String name){
        super(name);
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+ " has received the orders");
        try{
            Thread.sleep(100);//simulating receiving time

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ProcessingOrders extends Thread {
    ArrayList<String> orders;

    public ProcessingOrders(ArrayList<String> orders, String name){
        super(name);
        this.orders = orders;
    }

    public synchronized void addOrder(String order){
        orders.add(order);
        try {
            wait(500); //orderProcessing time simulation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notify();
        System.out.println(Thread.currentThread().getName()+ " has added the order: "+ order);
    }

    @Override
    public void run() {
        String[] orderValues = new String[10];

        for (int i = 0; i < 10; i++) {
           orderValues[i] = "Order No." + i;
           addOrder(orderValues[i]);
        }
        try {
            Thread.sleep(500); //orderProcessing time simulation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+ " has processed all the orders");


    }
}

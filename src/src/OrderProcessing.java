import java.util.ArrayList;

public class OrderProcessing {
    public static void main(String[] args) {
        ArrayList<String > order = new ArrayList<>();
        ArrayList<String > inventory = new ArrayList<>();
        ArrayList<String > user = new ArrayList<>();

        for (int i = 0; i < 10 ; i++) {
            order.add("order No. "+i);
            inventory.add("Inventory No. "+ i);
            user.add("User ID. "+ i);
        }

        OrderProcessingThread t1 = new OrderProcessingThread(order);
        UserNotificationThread t2 = new UserNotificationThread(user);
        InventoryUpdateRunnable inv = new InventoryUpdateRunnable(inventory);
        Thread t3 = new Thread(inv);

        t1.start();
        t2.start();
        t3.start();

        // Comparison:
// 1. Extending Thread:
//    - Advantages: Easier to implement for a single-threaded task as it requires overriding the run() method.
//    - Disadvantages: Java does not support multiple inheritance, so extending Thread limits the ability to extend other classes.
// 2. Implementing Runnable:
//    - Advantages: Allows the class to extend other classes while also supporting multithreading.
//    - Disadvantages: Slightly more lengthy as it requires passing a Runnable instance to a Thread.



    }
}

class OrderProcessingThread extends Thread{
    ArrayList<String> orders;

    public OrderProcessingThread(ArrayList<String> orders){
        this.orders = orders;
    }
    @Override
    public void run() {
        for( String order:orders){
            System.out.println(order + " is being processed now");
        try{
            Thread.sleep(400);// simulating order processing time
        } catch (InterruptedException e) {
            System.out.println(order+ " Order processing has been interrupted");
            throw new RuntimeException(e);
        }

        }

        System.out.println(Thread.currentThread().getName() + " has processed all the orders");

    }
}

class InventoryUpdateRunnable implements Runnable{
    ArrayList<String> inventory;

    public InventoryUpdateRunnable(ArrayList<String> inventory){
        this.inventory = inventory;
    }

    @Override
    public void run() {

        for (String item:inventory){
            System.out.println(item + " has been added to the inventory");
            try{
                Thread.sleep(400); // simulating inventory update time
            }catch (InterruptedException e){
                System.out.println(item+ " adding has been interrupted");
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName()+" has updated all available inventory");

    }
}

class UserNotificationThread extends Thread{
    ArrayList<String> user;
    public UserNotificationThread(ArrayList<String> user){
        this.user = user;
    }
    @Override
    public void run() {
     for (String name:user){
         System.out.println(name+ " has been notified");
         try{
             Thread.sleep(200); // simulating notification time lag
         } catch (InterruptedException e) {
             System.out.println("notifying "+name+" has been interrupted");
             throw new RuntimeException(e);
         }
     }
        System.out.println(Thread.currentThread().getName()+" has notified all the users");
    }
}
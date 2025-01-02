public class Main {
    public static void main(String[] args) {
        Thread[] myThread = new Thread[10];
        for (int i = 0; i < 10 ; i++) {
            myThread[i]= new MyThread(i+1);
        }

        for (Thread thread:myThread) {
            thread.start();
        }

        MyRunnable mr= new MyRunnable();

        Thread r1 = new Thread(mr);

        r1.start();
    }
}

//Creating a thread using Thread class

class MyThread extends Thread{

    public MyThread(int id){
        super("Thread No. "+ id);
    }
    @Override
    public  void  run() {
        try{
           Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+ " is running now");
    }
}


class MyRunnable implements Runnable{


    @Override
    public void run() {
        try{
           Thread.sleep(100);
                   } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName()+ " runnable thread is running now");
    }
}
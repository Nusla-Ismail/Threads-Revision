public class ConsumerProducer {
    public static void main(String[] args) {
       Buffers buffer = new Buffers();
       Thread t1= new Producer3(buffer);
       Thread t2 = new Consumer3(buffer);
       t1.start();
       t2.start();
    }
}

class Buffers {
    private final int numberOfItems = 10;
    private final Object[] buffer = new Object[numberOfItems];
    private int in = 0;
    private int out=0;
    private int bufferSize=0;

    public synchronized void put(Object value){
        while(buffer.length==numberOfItems){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        notifyAll();
        buffer[in]=value;
        in = (in+1)%10;
        bufferSize++;
    }

    public synchronized Object take(){
        while (buffer.length==0){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        notifyAll();
        Object item = buffer[out];
        out = (out+1)%10;
        bufferSize--;
        return item;
    }
}

class Consumer3 extends Thread{
    Buffers buffer;

    public Consumer3(Buffers buffer){
        this.buffer=buffer;
    }

    @Override
    public void run() {
        while(true){
            Object item = buffer.take();
            System.out.println(item+" has been consumed");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
       }
    }
}

class Producer3 extends Thread{
    Buffers buffer;

    public Producer3(Buffers buffer){
        this.buffer=buffer;
    }

    @Override
    public void run() {
        int i =0;
        while(true){
            Object item = "item"+i++;
            buffer.put(item);
            System.out.println(item+ " has been produced");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
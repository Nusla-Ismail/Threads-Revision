import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReaderWriter{
    private int num_readers;
    private boolean writing;
    private final Condition OK_to_read;
    private final Condition OK_to_write;
    private int writersWaiting;
    private final Lock lock;

    public ReaderWriter(){
        num_readers = 0;
        writing = false;
        lock = new ReentrantLock();
        writersWaiting = 0;
        OK_to_read = lock.newCondition();
        OK_to_write = lock.newCondition();
    }

    public void start_read(){
        lock.lock(); // acquires lock
        try {
            // Wait if there's a writer currently writing or no writers are waiting
            while (writing || writersWaiting > 0) {
                OK_to_read.await();
            }
            num_readers++; // Reader starts reading
            OK_to_read.signalAll(); // Wake up other readers
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void end_read(){
        lock.lock();
        try {
            num_readers--; // One less reader
            if (num_readers == 0) {
                OK_to_write.signalAll(); // Wake up writers if no more readers
            }else {
                OK_to_read.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void start_write(){
        lock.lock();
        try {
            writersWaiting++;// Increment the writers waiting
            while (writing || num_readers > 0) { // Wait if there's an active writer or readers
                OK_to_write.await();

            }
            writersWaiting--; // Decrement after starting writing
            writing = true; // Writer is writing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void end_write(){
        lock.lock();
        try {
            writing = false; // Writer finishes writing
            if (writersWaiting > 0) {
                OK_to_write.signalAll(); // Wake up the next waiting writer
            } else {
                OK_to_read.signalAll(); // Wake up waiting readers
            }
        } finally {
            lock.unlock();
        }
    }
}

class Reader extends Thread{
    private final ReaderWriter readerWriter;

    public Reader(ReaderWriter readerWriter){
        this.readerWriter = readerWriter;
    }

    @Override
    public void run() {
        readerWriter.start_read();
        System.out.println("Reading from the database...");
        try {
            Thread.sleep(1000);  // Simulate time taken for reading
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        readerWriter.end_read();
    }
}

class Writer extends Thread{
    private final ReaderWriter readerWriter;

    public Writer(ReaderWriter readerWriter){
        this.readerWriter = readerWriter;
    }

    @Override
    public void run() {
        readerWriter.start_write();
        System.out.println("Writing to the database...");
        try {
            Thread.sleep(1000);  // Simulate time taken for writing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        readerWriter.end_write();
    }
}

public class ReaderWriterSimulation{
    public static void main(String[] args) {
        ReaderWriter readerWriter = new ReaderWriter();
        Reader[] readers = new Reader[10];
        for(int i = 0; i < 10; i++){
            readers[i] = new Reader(readerWriter);
        }
        Writer writer1 = new Writer(readerWriter);
        Writer writer2 = new Writer(readerWriter);

        writer1.start();
        writer2.start();
        try {
            writer1.join(); // ensuring at least one writer has written before reading
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for(Reader reader : readers){
            reader.start();
        }
    }
}

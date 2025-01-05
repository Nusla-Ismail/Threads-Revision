class SelfishThread extends Thread {
    public int tick = 1;

    public SelfishThread(int id) {
        super("SelfishThread-" + id);
    }

    public void run() {
        while (tick < 100) {
            tick++;
            if (tick % 10 == 0) {
                System.out.println(getName() + " tick = " + tick);
                try {
                    // Corrected: Access yield() via the Thread class directly
                    Thread.yield();
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        SelfishThread thread1 = new SelfishThread(1);
        SelfishThread thread2 = new SelfishThread(2);

        // Create a daemon thread
        Thread daemonThread = new Thread(() -> { // lowest priority thread
            while (true) {
                System.out.println("Daemon thread is running...");

            }
        });

        // Mark the daemon thread as a daemon thread
        daemonThread.setDaemon(true);



        thread1.start();
        thread2.start();
    }
}

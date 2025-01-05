public class ThreadGroupExample {
    public static void main(String[] args) {
        // Number of groups and threads
        final int NUM_GROUPS = 6;
        final int NUM_THREADS = 7;

        // Arrays to hold the thread groups and threads
        ThreadGroup[] groups = new ThreadGroup[NUM_GROUPS];
        Thread[] threads = new Thread[NUM_THREADS];

        // Get the "main" system group
        groups[0] = Thread.currentThread().getThreadGroup();

        // Create the thread group hierarchy
        groups[1] = new ThreadGroup(groups[0], "ThreadGroup A");
        groups[2] = new ThreadGroup(groups[0], "ThreadGroup B");
        groups[3] = new ThreadGroup(groups[0], "ThreadGroup C");
        groups[4] = new ThreadGroup(groups[2], "ThreadGroup B1");
        groups[5] = new ThreadGroup(groups[2], "ThreadGroup B2");

        // Create the threads and assign them to groups
        threads[0] = new Thread(groups[0], "Thread 1");
        threads[1] = new Thread(groups[1], "Thread 2");
        threads[2] = new Thread(groups[1], "Thread 3");
        threads[3] = new Thread(groups[3], "Thread 4");
        threads[4] = new Thread(groups[4], "Thread 5");
        threads[5] = new Thread(groups[4], "Thread 6");
        threads[6] = new Thread(groups[5], "Thread 7");

        // Start the threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Print thread group hierarchy using enumerate() and activeCount()
        printThreadGroupHierarchy(groups);
    }

    // Method to print thread group hierarchy
    private static void printThreadGroupHierarchy(ThreadGroup[] groups) {
        // Iterate through the thread groups
        for (ThreadGroup group : groups) {
            if (group != null) {
                System.out.println("Thread Group: " + group.getName());

                // Get the number of active threads in the group (including subgroups)
                int activeThreads = group.activeCount();
                System.out.println("Number of active threads: " + activeThreads);

                // Create an array to store the threads in this group
                Thread[] activeThreadsArray = new Thread[activeThreads];

                // Enumerate all threads in the group
                int enumeratedThreads = group.enumerate(activeThreadsArray);
                System.out.println("Enumerated " + enumeratedThreads + " threads:");

                // Print each thread in the group
                for (Thread t : activeThreadsArray) {
                    if (t != null) {
                        System.out.println("  - " + t.getName());
                    }
                }
                System.out.println();
            }
        }
    }
}

package synchronous_asynchronous_blocking_nonblocking;

public class SynchronousBlockingExample {

    public static void main(String[] args) throws InterruptedException {
        printAfterSeconds("Task A", 3000);
        printAfterSeconds("Task B", 2000);
        printAfterSeconds("Task C", 1000);
    }

    private static void printAfterSeconds(String text, long millis) throws InterruptedException {
        Thread.sleep(millis);
        System.out.println(text);
    }
}

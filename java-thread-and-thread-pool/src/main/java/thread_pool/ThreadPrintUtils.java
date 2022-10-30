package thread_pool;

public class ThreadPrintUtils {

    public static String getCurrentThreadName() {
        return "[" + Thread.currentThread().getName() + "]";
    }
}

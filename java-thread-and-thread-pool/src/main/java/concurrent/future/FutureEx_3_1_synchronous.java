package concurrent.future;

import thread_pool.ThreadPrintUtils;

public class FutureEx_3_1_synchronous {

    public static void main(String[] args) throws InterruptedException {
        String start = returnValueAfterSeconds("Start", 3000);
        String process = returnValueAfterSeconds("Process", 2000);
        String end = returnValueAfterSeconds("End", 1000);

        System.out.println(ThreadPrintUtils.getCurrentThreadName() +  start);
        System.out.println(ThreadPrintUtils.getCurrentThreadName() +  process);
        System.out.println(ThreadPrintUtils.getCurrentThreadName() +  end);
        System.out.println(ThreadPrintUtils.getCurrentThreadName() +  start + " " + process + " " + end);
        System.out.println(ThreadPrintUtils.getCurrentThreadName() + "Exit");
    }

    private static String returnValueAfterSeconds(String text, long millis) throws InterruptedException {
        Thread.sleep(millis);
        return text;
    }
}

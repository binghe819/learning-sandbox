package thread.threadgroup;

import java.util.Map;

public class PrintAllThreadGroup {

    public static void main(String[] args) {
        Thread th1 = new Thread(new DoSomething());
        th1.start();
        printAllThread();
    }

    public static void printAllThread() {
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        for (Thread thread : allStackTraces.keySet()) {
            System.out.println("-----------");
            System.out.println("Thread Name : " + thread.getName());
            System.out.println("Thread Group : " + thread.getThreadGroup());
            System.out.println("-----------");
        }
    }

    static class DoSomething implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {}
            System.out.println("Do Something~~~ thread");
        }
    }
}

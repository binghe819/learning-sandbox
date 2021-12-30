package thread.yield;

public class YieldThread {

    static class PrintAndYield implements Runnable {
        @Override
        public void run() {
            // 한번 스레드 이름을 출력하고 yield하고 또 다시 출력하는 로직.

            System.out.println(Thread.currentThread().getName());

            Thread.yield();

            System.out.println(Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        Thread th1 = new Thread(new PrintAndYield(), "Thread 1");
        Thread th2 = new Thread(new PrintAndYield(), "Thread 2");

        th1.start();
        th2.start();
    }
}

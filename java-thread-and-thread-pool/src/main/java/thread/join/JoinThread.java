package thread.join;

public class JoinThread {

    static class PrintName implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                System.out.print(Thread.currentThread().getName());
            }
        }
    }

    public static void main(String[] args) {
        Thread th1 = new Thread(new PrintName(), "-");
        Thread th2 = new Thread(new PrintName(), "|");

        long startTime = System.currentTimeMillis();

        th1.start();
        th2.start();

        try {
            th1.join(); // Main 스레드가 th1 스레드의 작업이 끝날 때까지 기다린다.
            th2.join(); // Main 스레드가 th2 스레드의 작업이 끝날 때까지 기다린다.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long executionTime = System.currentTimeMillis() - startTime;
        System.out.print("소요시간: " + executionTime);
    }
}

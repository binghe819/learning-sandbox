package thread.sleep;

// Sleep 테스트 - sleep은 일정시간동안 스레드를 멈추게 한다. (Custom Thread에 Sleep 발생시키는 예시)
public class CustomThread {

    public static void main(String[] args) {
        Thread customThread = new Thread(new SleepRunnable());

        customThread.start();
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }

    static class SleepRunnable implements Runnable {
        @Override
        public void run() {
            try {
                String threadName = Thread.currentThread().getName() + "(sleep)";
                for (int i = 0; i < 100; i++) {
                    System.out.println(threadName + " " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {}
        }
    }
}

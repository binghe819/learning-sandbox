package thread.sleep;

// Sleep 테스트 - sleep은 일정시간동안 스레드를 멈추게 한다. (Main Thread에 Sleep 발생시키는 예시)
public class MainThread {

    public static void main(String[] args) {
        Thread th = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
        });
        th.start();

        // Main Thread가 Sleep 발동.
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {}
    }
}

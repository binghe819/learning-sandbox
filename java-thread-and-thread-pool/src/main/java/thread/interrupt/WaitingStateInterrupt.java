package thread.interrupt;

// Waiting 상태의 스레드 (sleep, wait, join)에 인터럽트를 발생시켜 InterruptException이 발생하는 예시
public class WaitingStateInterrupt {

    static class CountThread extends Thread {
        @Override
        public void run() {
            int i = 10;

            while (i != 0 && !isInterrupted()) {
                System.out.println(getName() + " " + i--);
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    // interrupt()가 발생되면 실행되는 로직
                    System.out.println("InterruptedException 발생");
                    break;
                }
            }
            System.out.println("카운트가 종료되었습니다.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread countTh = new CountThread();
        countTh.start();

        System.out.println("interrupt 전 - isInterrupted() : " + countTh.isInterrupted());

        Thread.sleep(2_000); // 2초간 main 스레드 sleep

        countTh.interrupt();
        System.out.println("interrupt 후 - isInterrupted() : " + countTh.isInterrupted());
    }
}

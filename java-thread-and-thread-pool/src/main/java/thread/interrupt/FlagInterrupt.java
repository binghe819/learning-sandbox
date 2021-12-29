package thread.interrupt;

// Thread의 interrupt 상태를 이용한 스레드 인터럽트 제어 예시
public class FlagInterrupt {
    static class CountThread extends Thread {
        @Override
        public void run() {
            int i = 10;

            while (!isInterrupted()) {
                System.out.println(getName() + " " + i--);
                for(long x = 0; x < 2500000000L; x++); // 시간 지연
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

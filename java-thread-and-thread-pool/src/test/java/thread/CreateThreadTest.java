package thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("스레드의 구현 및 실행 테스트")
public class CreateThreadTest {

    @Test
    void Thread_생성_및_실행_테스트() {
        // given, then
        Thread thread1 = new ThreadByExtends(); // 1
        Thread thread2 = new Thread(new ThreadByRunnable()); // 2

        // then
        thread1.start();
        thread2.start();
    }

    // 1. Thread 클래스를 상속받는 방법
    static class ThreadByExtends extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(getName()); // 부모인 Thread의 getName() 호출
            }
        }
    }

    // 2. Runnable 인터페이스를 구현하는 방법
    static class ThreadByRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName());
            }
        }
    }
}

package thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sleep 테스트")
public class SleepTest {

    @Test
    void sleep은_일정시간동안_스레드를_멈추게_한다() {
        // given
        Thread th1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Sleep 시작");
                Thread.sleep(1_000);
            } catch (InterruptedException e) {}
        });

        // when
        th1.start(); // 1초 동안 Sleep
        for (int i = 0; i < 30; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }
}

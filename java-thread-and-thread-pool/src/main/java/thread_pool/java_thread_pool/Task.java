package thread_pool.java_thread_pool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Runnable {

    private String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

                if (i == 0) {
                    System.out.println(this.name + "  시작 - " + dateFormat.format(now));
                } else {
                    System.out.println(this.name + " 실행 - " + dateFormat.format(now));
                }
                Thread.sleep(1_000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + " Task 완료");
    }
}

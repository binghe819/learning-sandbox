package iterable_and_observer_pattern;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Observable: source (producer). 이벤트를 던지는 역할.
 * Observable (Source, Producer) -> Event/Data -> Observer (Target, Consumer)
 */
@SuppressWarnings("deprecation")
public class ObservableTestMain {

    public static void main(String[] args) {
        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + " " + arg);
            }
        };

        IntObservable io = new IntObservable();
        io.addObserver(ob);

        ExecutorService es = Executors.newSingleThreadExecutor();

        es.execute(io);

        System.out.println(Thread.currentThread().getName() + " EXIT");
        es.shutdown();
    }

    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                setChanged(); // 변경된 것을 알려줌
                // push
                notifyObservers(i); // Observer에게 이벤트 알림. (데이터 전송)
            }
        }
    }
}

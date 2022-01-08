package thread_pool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@DisplayName("Executor 테스트")
public class ExecutorTest {

    @Test
    void executor_singleThreadPool() {
        // given
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            System.out.println("Hello Executor");
        });
    }
}

package caffeine.population;

import com.binghe.DataObject;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 캐시 추가 테스트 - Async (동기화 로딩)
 * - 이 전략은 Loading과 동일하게 작동하지만 비동기적으로 작업을 수행하고 실제 값을 보유하는 CompletableFuture를 반환합니다.
 * -
 */
public class CachePopulationAsyncLoadingTest {

    @Test
    void AsyncLoading_저장_및_조회() {
        AsyncLoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(k -> DataObject.create("Data for " + k));

        String key = "test";
        cache.get(key) // CF
                .thenAccept(dataObject -> {
                    assertThat(dataObject).isNotNull();
                    assertThat(dataObject.getData()).isEqualTo("Data for " + key);
                });

        cache.getAll(Arrays.asList("A", "B", "C"))
                .thenAccept(dataObjectMap -> assertEquals(3, dataObjectMap.size()));
    }
}

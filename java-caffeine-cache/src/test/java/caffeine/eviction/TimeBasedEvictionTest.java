package caffeine.eviction;

import com.binghe.DataObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Time-Based eviction
 * - 설정된 만료 시간을 기준으로 하며 세 가지 유형으로 데이터를 evict하는 전략.
 *   - expireAfterAccess (마지막 읽기 또는 쓰기가 발생한 이후 기간이 지나면 항목이 만료됩니다.)
 *   - expireAfterWrite  (마지막 쓰기가 발생한 이후 기간이 지나면 항목이 만료됩니다.)
 *   - custom (Expiry 구현) (만료 시간은 Expiry 구현에 의해 각 항목별로 개별적으로 계산됩니다.)
 */
public class TimeBasedEvictionTest {

    @Test
    void time_based_expireAfterAccess() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .build(k -> DataObject.create("Data for " + k));

        // 데이터 삽입
        cache.get("A");
        assertThat(cache.estimatedSize()).isEqualTo(1L);

        // 6초간 TimeSleep
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        cache.cleanUp();
        assertThat(cache.estimatedSize()).isEqualTo(0L);
    }

    @Test
    void time_based_expireAfterWrite() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .build(k -> DataObject.create("Data for " + k));

        // 데이터 삽입
        cache.get("A");
        assertThat(cache.estimatedSize()).isEqualTo(1L);
        cache.get("A");

        // 6초간 TimeSleep
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        cache.cleanUp();
        assertThat(cache.estimatedSize()).isEqualTo(0L);
    }

    @Test
    void time_based_custom() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder().expireAfter(new Expiry<String, DataObject>() {
            @Override
            public long expireAfterCreate(
                    String key, DataObject value, long currentTime) {
                return value.getData().length() * 1000;
            }

            @Override
            public long expireAfterUpdate(
                    String key, DataObject value, long currentTime, long currentDuration) {
                return currentDuration;
            }

            @Override
            public long expireAfterRead(
                    String key, DataObject value, long currentTime, long currentDuration) {
                return currentDuration;
            }
        }).build(k -> DataObject.create("Data for " + k));
    }
}

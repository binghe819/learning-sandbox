package caffeine.eviction;

import com.binghe.DataObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Size-Based Eviction
 * - 설정된 크기 제한을 초과할 때 evict가 발생하는 전략.
 * - 크기를 얻는 방법에는 두 가지가 있습니다. 캐시에 있는 객체의 개수는 세는 것, 또는 가중치를 얻는 것입니다.
 *  - 개수
 *  - 가중치
 *
 * 그렇다면, 어떤 값을 제거할까요?
 * -> Window TinyLfu를 적용하여 가장 최근에 사용되지 않았거나, 자주 사용되어지지 않은 것을 제거합니다.
 */
public class SizeBasedEvictionTest {

    @DisplayName("count 기반은 최대 크기를 초과할 때 evict가 발생한다. 단, 비동기로 동작하므로 바로 지워지진않고 추후 작업에 따라 제거된다.")
    @Test
    void size_based_eviction_count() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumSize(1)
                .build(k -> DataObject.create("Data for " + k));

        assertThat(cache.estimatedSize()).isEqualTo(0L);

        cache.get("A");
        assertThat(cache.estimatedSize()).isEqualTo(1L);

        // 최대 크기보다 초과된 데이터 삽입
        cache.get("B");
        // Performs any pending maintenance operations needed by the cache. Exactly which activities are performed.
        cache.cleanUp(); // cache에 남겨진 작업을 수행하라고 요청. (eviction)

        assertThat(cache.estimatedSize()).isEqualTo(1L);
    }

    @Test
    void size_based_eviction_weight() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumWeight(10)
                .weigher((k ,v) -> 5) // 매 key, value마다 가중치 5로 설정. (만약 데이터에 따라 가중치를 커스텀하고 싶다면 여기에 하면 된다.)
                .build(k -> DataObject.create("Data for " + k));

        assertThat(cache.estimatedSize()).isEqualTo(0L);
        cache.get("A");
        cache.get("B");
        assertThat(cache.estimatedSize()).isEqualTo(2L);

        // 최대 가중치보다 초과된 데이터 삽입
        cache.get("C");
        cache.cleanUp();

        assertThat(cache.estimatedSize()).isEqualTo(2L);
    }
}

package caffeine.eviction;

import com.binghe.DataObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.Test;

/**
 * Reference-Based Eviction
 * - 키나 값에 대한 약한 참조와 값에 대한 소프트 참조를 사용하여 캐시를 설정하여 항목에 대해서 CG가 수집할 수 있도록 허용할 수 있다.
 * - 이렇게하면 이를 통해 키나 값에 대한 다른 강력한 참조가 없는 경우 항목을 GC가 수집할 수 있습니다. 가비지 수집은 ID 동등성에만 의존하므로, 이로 인해 전체 캐시가 키를 비교하기 위해 equals() 대신 ID(==) 동등성을 사용하게 됩니다.
 *
 */
public class ReferenceBasedEvictionTest {

    @Test
    void reference_based_eviction() {
        // Evict when neither the key nor value are strongly reachable
        LoadingCache<String, DataObject> cache1 = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                .build(key -> DataObject.create(key));

        // Evict when the garbage collector needs to free memory
        LoadingCache<String, DataObject> cache2 = Caffeine.newBuilder()
                .softValues()
                .build(key -> DataObject.create(key));
    }
}

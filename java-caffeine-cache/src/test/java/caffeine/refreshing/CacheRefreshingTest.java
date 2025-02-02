package caffeine.refreshing;

import com.binghe.DataObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Refreshing
 * - Refreshing is not quite the same as eviction.
 *   As specified in LoadingCache.refresh(K), refreshing a key loads a new value for the key asynchronously.
 *   The old value (if any) is still returned while the key is being refreshed, in contrast to eviction,
 *   which forces retrievals to wait until the value is loaded anew. (백그라운드에서 refresh를 진행함)
 *
 * - refreshAfterWrite will make a key eligible for refresh after the specified duration,
 *   but a refresh will only be actually initiated when the entry is queried (지정된 시간이 지나 해당 키에 대한 요청이 왔을 때 refresh를 수행한다.)
 *
 * - you can specify both refreshAfterWrite and expireAfterWrite on the same cache
 *   so that the expiration timer on an entry isn't blindly reset whenever an entry becomes eligible for a refresh.
 *   If an entry isn't queried after it comes eligible for refreshing, it is allowed to expire. (같이 사용하면 refresh 될 때마다 expire 시간이 초기화된다. write 혹은 refresh되고 expire동안 한번도 조회가 안들어오면 expire)
 *
 * - Refresh operations are executed asynchronously using an Executor.
 *   The default executor is ForkJoinPool.commonPool() and can be overridden via Caffeine.executor(Executor). (스레드 풀 설정)
 *
 * 참고: https://github.com/ben-manes/caffeine/wiki/Refresh
 */
public class CacheRefreshingTest {

    @DisplayName("")
    @Test
    void cache_refreshing_test() {
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .refreshAfterWrite(Duration.ofMinutes(1))
                .build(key -> key); // 수정 -> refresh될 때마다 카운트 1씩 올려서 value에 저장하도록.
    }
}

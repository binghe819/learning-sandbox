package caffeine.population;

import com.binghe.DataObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 캐시 추가 테스트 - Manual
 * - 수동으로 캐시 생성을 할대는 cache 에 로딩하는 코드가 필요없다. 단지 해당 키가 없는 지를 아래와 같이 get..으로 확인후 put으로 넣으면 된다.
 * - 캐시 인터페이스를 사용하면 항목 검색, 업데이트, 무효화를 명시적으로 제어할 수 있습니다.
 *
 * (문서에 Cache.asMap()으로 반환된 ConcurrentMap을 이용해서도 데이터 수정이 된다는 것으로 보아, 그저 Map이다)
 *
 * 참고: https://github.com/ben-manes/caffeine/wiki/Population
 */
public class CachePopulationManualTest {

    @Test
    void Manual_저장_및_조회_put() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();

        // 없는 데이터 조회
        String key = "test";
        DataObject dataObject = cache.getIfPresent(key);
        assertThat(dataObject).isNull();

        // 데이터 Put
        cache.put(key, DataObject.create("value"));

        // 데이터 조회
        DataObject result = cache.getIfPresent(key);
        assertThat(result).isNotNull();
        assertThat(result.getData()).isEqualTo("value");
    }

    @Test
    void Manual_저장_및_조회_get() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();

        String key = "test";
        DataObject dataObject = cache.get(key, k -> DataObject.create("value"));

        assertThat(dataObject).isNotNull();
        assertThat(dataObject.getData()).isEqualTo("value");
    }

    @Test
    void Manual_캐시_제거() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();

        String key = "test";
        DataObject dataObject = cache.get(key, k -> DataObject.create("value"));

        assertThat(dataObject).isNotNull();

        // 캐시 제거
        cache.invalidate(key);
        assertThat(cache.getIfPresent(key)).isNull();
    }
}

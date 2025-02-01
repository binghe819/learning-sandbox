package caffeine.population;

import com.binghe.DataObject;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 캐시 추가 테스트 - Loading (동기화 로딩)
 * - LoadingCache는 CacheLoader가 첨부된 캐시입니다. CacheLoader란 cache.get으로 데이터 조회시 키가 없으면 CacheLoader에 정의한 함수에 등록한 코드가 동작하여 키를 등록한다.
 */
public class CachePopulationLoadingTest {

    Logger log = LoggerFactory.getLogger(CachePopulationLoadingTest.class);

    @Test
    void Loading_저장_및_조회() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build(key -> DataObject.create("Data for " + key));

        // 없는 데이터 조회시 CacheLodaer에 의해 자동으로 저장된다.
        String key = "test";
        DataObject dataObject = cache.get(key);
        assertThat(dataObject).isNotNull();
        assertThat(dataObject.getData()).isEqualTo("Data for test");
    }

    /**
     * - By default, getAll will issue a separate call to CacheLoader.load for each key which is absent from the cache. (n번 호출됨)
     * - When bulk retrieval is more efficient than many individual lookups, you can override CacheLoader.loadAll to exploit this. (loadAll 활용시 1번 호출됨)
     */
    @Test
    void Loading_getAll() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build(this::findByKey);

        // getAll로 데이터 조회시에도 없는 데이터의 경우 하나하나 CacheLoader가 동작하여 데이터를 채운다.
        Map<String, DataObject> dataObjectMap = cache.getAll(Arrays.asList("A", "B", "C"));

        assertThat(dataObjectMap.size()).isEqualTo(3);
        assertThat(dataObjectMap.get("A").getData()).isEqualTo("Data for A");
        assertThat(dataObjectMap.get("B").getData()).isEqualTo("Data for B");
        assertThat(dataObjectMap.get("C").getData()).isEqualTo("Data for C");
    }

    @Test
    void Loading_getAll_plus_loadAll() {
        CacheLoader<String, DataObject> cacheLoader = new CacheLoader<String, DataObject>() {
            @Override
            public DataObject load(String key) throws Exception {
                // find
                return findByKey(key);
            }

            @Override
            public Map<? extends String, ? extends @NonNull DataObject> loadAll(Set<? extends String> keys) throws Exception {
                // findAll 구현
                List<DataObject> result = findByAll(keys);
                // key에 맞는 value를 찾는 KeyFindFunc 구현이 중요할 것으로 보임.
                return result.stream()
                        .collect(toMap(it -> it.getData().replace("Data for ", ""), Function.identity(), (a1, a2) -> a1));
            }
        };

        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(100)
                .build(cacheLoader);

        cache.get("C"); // C는 미리 저장.
        // loadAll을 구현했으므로 1회 호출만으로 A, B에 대한 데이터 생성.
        Map<String, DataObject> dataObjectMap = cache.getAll(Arrays.asList("A", "B", "C"));

        assertThat(dataObjectMap.size()).isEqualTo(3);
        assertThat(dataObjectMap.get("A").getData()).isEqualTo("Data for A");
        assertThat(dataObjectMap.get("B").getData()).isEqualTo("Data for B");
        assertThat(dataObjectMap.get("C").getData()).isEqualTo("Data for C");
    }

    // 이름만 이렇고 그냥 생성해서 응답하는 메서드 (테스트용)
    private DataObject findByKey(String key) {
        log.info("findByKey 호출됨. key - " + key);
        return DataObject.create("Data for " + key);
    }

    private List<DataObject> findByAll(Set<? extends String> keys) {
        log.info("findByAll 호출됨. key - " + keys);
        return keys.stream()
                .map(it -> DataObject.create("Data for " + it))
                .collect(Collectors.toList());
    }
}

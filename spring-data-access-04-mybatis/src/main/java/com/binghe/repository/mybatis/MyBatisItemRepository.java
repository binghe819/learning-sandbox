package com.binghe.repository.mybatis;

import com.binghe.domain.Item;
import com.binghe.repository.ItemRepository;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisItemRepository implements ItemRepository {

    private static final Logger log = LoggerFactory.getLogger(MyBatisItemRepository.class);

    private final ItemMapper itemMapper;

    public MyBatisItemRepository(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    /**
     * [MyBatis에서 INSERT 후 생성된 id를 얻는 방법]
     *
     * MyBatis의 <insert useGeneratedKeys="true" keyProperty="id"> 설정을 하면
     * DB가 AUTO_INCREMENT로 생성한 PK를 파라미터 객체(item)의 id 필드에 자동으로 주입해준다.
     *
     * 단, MyBatis <insert>의 반환값은 "영향받은 행 수(int)"로 고정되어 있다.
     * 즉, Mapper 인터페이스에서 save()의 반환 타입을 Long으로 선언해도
     * 그 값은 생성된 id가 아니라 rows affected(1)가 된다.
     *
     * 따라서 생성된 id를 얻으려면 itemMapper.save(item) 호출 후
     * keyProperty로 주입된 item.getId()를 직접 꺼내야 한다.
     *
     * 흐름 요약:
     *   1. itemMapper.save(item) 호출
     *   2. MyBatis가 INSERT 실행 후 JDBC getGeneratedKeys()로 생성된 PK를 가져옴
     *   3. keyProperty="id" 설정에 따라 item.setId(generatedKey)를 자동 호출
     *   4. item.getId()로 생성된 id를 반환
     */
    @Override
    public Long save(Item item) {
        log.info("itemMapper class={}", itemMapper.getClass());
        itemMapper.save(item);
        return item.getId();
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemMapper.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCondition cond) {
        return itemMapper.findAll(cond);
    }
}

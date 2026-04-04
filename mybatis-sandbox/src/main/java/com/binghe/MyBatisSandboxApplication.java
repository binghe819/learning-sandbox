package com.binghe;

import com.binghe.domain.Item;
import com.binghe.mybatis.MyBatisConfig;
import com.binghe.repository.ItemMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class MyBatisSandboxApplication {

    private static final Logger log = LoggerFactory.getLogger(MyBatisSandboxApplication.class);

    public static void main(String[] args) {
        MyBatisConfig myBatisConfig = new MyBatisConfig();

        SqlSessionFactory sqlSessionFactory = myBatisConfig.sqlSessionFactory();

        Item item1 = new Item(null, "itemA", 1000, 10);
        Item item2 = new Item(null, "itemB", 2000, 20);

        // openSession()은 auto-commit=false로 세션을 연다.
        // 따라서 commit()을 명시적으로 호출해야 DB에 반영된다.
        // 에러 발생 시 rollback()으로 해당 세션에서 실행된 SQL을 모두 되돌린다.
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            try {
                ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
                mapper.save(item1);
                mapper.save(item2);

                sqlSession.commit();

                // useGeneratedKeys="true" keyProperty="id" 설정으로 인해
                // INSERT 후 채번된 PK는 반환값이 아니라 파라미터 객체(item)의 id 필드에 주입된다.
                // <insert>의 반환값은 항상 영향받은 행 수(int)이기 때문에 Long으로 id를 받을 수 없다.
                log.info("save item1 id: {}", item1.getId());
                log.info("save item2 id: {}", item2.getId());
            } catch (Exception e) {
                sqlSession.rollback();
                log.error("save 실패, rollback 완료: {}", e.getMessage());
            }
        }

        // openSession(true) -> auto-commit: true
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
            Optional<Item> maybeItem1 = mapper.findById(item1.getId());
            Optional<Item> maybeItem2 = mapper.findById(item2.getId());

            System.out.println(maybeItem1.get());
            System.out.println(maybeItem2.get());
        }
    }
}

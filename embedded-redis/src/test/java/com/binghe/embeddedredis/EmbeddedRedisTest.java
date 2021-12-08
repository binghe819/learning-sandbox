package com.binghe.embeddedredis;

import static org.assertj.core.api.Assertions.assertThat;

import com.binghe.embeddedredis.domain.Point;
import com.binghe.embeddedredis.domain.PointRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmbeddedRedisTest {

    @Autowired
    private PointRepository pointRepository;

    @Test
    void 기본_등록_조회기능() {
        // given
        String id = "binghe819";
        LocalDateTime refreshTime = LocalDateTime.now();
        Point point = new Point(id, 1000L, refreshTime);

        // when
        pointRepository.save(point);

        // then
        Point savedPoint = pointRepository.findById(id).get();
        assertThat(savedPoint.getAmount()).isEqualTo(1000L);
        assertThat(savedPoint.getRefreshTime()).isEqualTo(refreshTime);
    }
}

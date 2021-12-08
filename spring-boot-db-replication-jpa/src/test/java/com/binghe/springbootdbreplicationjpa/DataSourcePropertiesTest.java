package com.binghe.springbootdbreplicationjpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.binghe.springbootdbreplicationjpa.config.DataSourceProperties;
import com.binghe.springbootdbreplicationjpa.config.DataSourceProperties.DatasourceNode;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("Profile에 명시된 DataSource 정보를 잘 가져오는지 확인하는 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@EnableConfigurationProperties(DataSourceProperties.class)
class DataSourcePropertiesTest {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Test
    void dependency() {
        assertThat(dataSourceProperties).isNotNull();
        assertThat(dataSourceProperties.getMaster()).isNotNull();
        assertThat(dataSourceProperties.getSlave()).isNotNull();
    }

    @DisplayName("Profile에 정의된 Master의 값을 바인딩한다.")
    @Test
    void bindDataSource_Master() {
        // given
        DatasourceNode expected = new DatasourceNode();
        expected.setName("master");
        expected.setUrl("{DB IP주소, 포트 및 스키마 명}");
        expected.setUsername("{DB 계정}");
        expected.setPassword("{비밀번호}");

        // when
        DatasourceNode master = dataSourceProperties.getMaster();

        // then
        assertThat(master)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("Profile에 정의된 Slave의 값을 바인딩한다.")
    @Test
    void bindDataSource_Slaves() {
        // given
        DatasourceNode expected1 = new DatasourceNode();
        expected1.setName("slave1");
        expected1.setUrl("{DB IP주소, 포트 및 스키마 명}");
        expected1.setUsername("{DB 계정}");
        expected1.setPassword("{비밀번호}");

        DatasourceNode expected2 = new DatasourceNode();
        expected2.setName("slave2");
        expected2.setUrl("{DB IP주소, 포트 및 스키마 명}");
        expected2.setUsername("{DB 계정}");
        expected2.setPassword("{비밀번호}");

        // when
        Map<String, DatasourceNode> slaves = dataSourceProperties.getSlave();

        // then
        assertThat(slaves.get(expected1.getName()))
            .usingRecursiveComparison()
            .isEqualTo(expected1);
        assertThat(slaves.get(expected2.getName()))
            .usingRecursiveComparison()
            .isEqualTo(expected2);
    }
}

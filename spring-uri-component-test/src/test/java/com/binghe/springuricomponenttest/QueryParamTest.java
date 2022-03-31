package com.binghe.springuricomponenttest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Query Param 관련 테스트")
public class QueryParamTest {

    @Test
    void 이미쿼리파람이존재하는경우_새로운값을넣는테스트() {
        // given
        String url = "https://binghe-test.com?f=jpg";

        // when
        String actual = UriComponentsBuilder.fromUriString(url)
                .queryParam("new_param", "new_value")
                .toUriString();

        // then
        System.out.println(actual);
        String expected = url + "&new_param=new_value";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 이미쿼리파람이존재하지않는경우_새로운값을넣는테스트() {
        // given
        String url = "https://binghe-test.com";

        // when
        String actual = UriComponentsBuilder.fromUriString(url)
                .queryParam("new_param", "new_value")
                .toUriString();

        // then
        System.out.printf(actual);
        String expected = url + "?new_param=new_value";
        assertThat(actual).isEqualTo(expected);
    }
}

package com.binghe.springuricomponenttest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("URI 관련 테스트")
public class UriTest {

    @Test
    void URI로부터_파일확장자_구하기() {
        // given
        URI uri = URI.create("https://binghe-test.com/filename.png");
        String path = uri.getPath();

        // when
        String fileExtension = path.substring(path.lastIndexOf(".") + 1);

        // then
        assertThat(fileExtension).isEqualTo("png");
    }

    @Test
    void 파일확장자가없는URI로부터_파일확장자_구하기_예외던져지나() {
        // given
        URI uri = URI.create("https://binghe-test.com");
        String path = uri.getPath();

        // when
        String fileExtension = path.substring(path.lastIndexOf(".") + 1);

        // then
        assertThat(fileExtension).isEqualTo("");
        assertThat(fileExtension).isNotNull();
    }
}

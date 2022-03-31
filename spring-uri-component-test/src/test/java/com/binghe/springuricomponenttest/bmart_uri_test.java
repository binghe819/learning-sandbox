package com.binghe.springuricomponenttest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class bmart_uri_test {

    @Test
    void 이미쿼리파람이존재하는경우_파일명() {
        // given
        String url = "https://binghe-test.com/thumbnail.jpg?f=jpg";
        double discountPercent = 50.0;

        // when
        String actual = determineTestImageUrl(url, discountPercent);

        // then
        System.out.println(actual);
        String expected = url + "&b=finishSale_" + (int)discountPercent;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 쿼리파람이존재하지않는경우() {
        // given
        String url = "https://binghe-test.com/thumbnail.jpg";
        double discountPercent = 50.0;

        // when
        String actual = determineTestImageUrl(url, discountPercent);

        // then
        System.out.println(actual);
        String expected = url + "?f=jpg" + "&b=finishSale_" + (int) discountPercent;
        assertThat(actual).isEqualTo(expected);
    }

    private String determineTestImageUrl(String originImageUrl, double discountPercent) {
        String filePath = URI.create(originImageUrl).getPath();
        String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);

        return UriComponentsBuilder.fromUriString(originImageUrl)
                .replaceQueryParam("f", fileExtension)
                .replaceQueryParam("b", String.format("finishSale_%d", (int) discountPercent))
                .toUriString();
    }
}

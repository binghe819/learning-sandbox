package com.binghe.gradlemultimodule;

import com.binghe.gradlemultimodule.presentation.UserInfoResponse;
import com.binghe.gradlemultimodule.presentation.UserRegisterRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class UserApiAcceptanceTest extends AcceptanceTest {

    @Test
    void 유저_생성() {
        // given, when
        ExtractableResponse<Response> response = 유저_생성_요청("binghe", 27);

        // then
        Assertions.assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    void 유저_조회() {
        // given
        ExtractableResponse<Response> createUserResponse = 유저_생성_요청("binghe", 27);
        String[] splittedRedirectUrl = createUserResponse.header("Location").split("/");
        String userId = splittedRedirectUrl[splittedRedirectUrl.length-1];

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/users/" + userId)
                .then().log().all()
                .extract();

        // then
        UserInfoResponse actual = new UserInfoResponse(Long.valueOf(userId), "binghe", 27);
        UserInfoResponse responseBody = response.body().as(UserInfoResponse.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(responseBody);
    }

    private ExtractableResponse<Response> 유저_생성_요청(String name, int age) {
        UserRegisterRequest requestBody = new UserRegisterRequest(name, age);

        return RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/users")
                .then().log().all()
                .extract();
    }
}

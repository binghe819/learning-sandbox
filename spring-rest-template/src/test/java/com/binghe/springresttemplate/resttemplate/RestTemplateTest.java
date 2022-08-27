package com.binghe.springresttemplate.resttemplate;


import com.binghe.springresttemplate.user.application.UserDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class RestTemplateTest {
    private static final String LOCAL_URL = "http://localhost:8080/users";

    @Test
    void Get_getForEntity() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        Long id = 1L;
        String url = LOCAL_URL + "/" + id;

        // when
        ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(id);
    }

    @Test
    void Get_getForObject() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        Long id = 1L;
        String url = LOCAL_URL + "/" + id;

        // when
        UserDto response = restTemplate.getForObject(url, UserDto.class);

        // then
        assertThat(response.getId()).isEqualTo(id);
    }

    @Test
    void Get_exchange() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        Long id = 1L;
        String url = LOCAL_URL + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(headers);

        // when
        ResponseEntity<UserDto> response = restTemplate.exchange(url, HttpMethod.GET, request, UserDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(id);
    }

    @Test
    void Head_headForHeaders() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        String url = LOCAL_URL;

        // when
        HttpHeaders response = restTemplate.headForHeaders(url);

        // then
        assertThat(response.getContentType().includes(MediaType.APPLICATION_JSON)).isTrue();
    }

    @Test
    void Post_postForEntity() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        String url = LOCAL_URL;
        String name = "test";
        int age = 27;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, name, age), headers);

        // when
        ResponseEntity<Long> response = restTemplate.postForEntity(url, request, Long.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isOfAnyClassIn(Long.class);
        System.out.println(response.getBody()); // 새로 만들어진 User의 Entity ID
    }

    @Test
    void Post_postForObject() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        String url = LOCAL_URL;
        String name = "test";
        int age = 27;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, name, age), headers);

        // when
        Long response = restTemplate.postForObject(url, request, Long.class);

        // then
        assertThat(response).isNotNull();
        System.out.println(response); // 새로 만들어진 User의 Entity ID
    }

    @Disabled
    @Test
    void Post_postForLocation() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        String url = LOCAL_URL;
        String name = "test";
        int age = 27;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, name, age), headers);

        // when
        URI uri = restTemplate.postForLocation(url, request);

        // then
        System.out.println(uri.toString());
    }

    @Test
    void Post_exchange() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        String url = LOCAL_URL;
        String name = "test";
        int age = 27;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, name, age), headers);

        // when
        ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.POST, request, Long.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response); // 새로 만들어진 User의 Entity ID
    }

    @Test
    void Put_put() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        String url = LOCAL_URL + "/" + 1L;
        String updatedName = "updated_user_name";
        int updatedAge = 27;
        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, updatedName, updatedAge), headers);

        // when
        restTemplate.put(url, request);
    }

    @Test
    void Put_exchange() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        String url = LOCAL_URL + "/" + 1L;
        String updatedName = "updated_user_name";
        int updatedAge = 27;
        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, updatedName, updatedAge), headers);

        // when
        ResponseEntity<UserDto> response = restTemplate.exchange(url, HttpMethod.PUT, request, UserDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(updatedName);
        assertThat(response.getBody().getAge()).isEqualTo(updatedAge);
    }

    @Test
    void DELETE() {
        // given
        RestTemplate restTemplate = new RestTemplate();

        String url = LOCAL_URL + "/" + 1L;

        restTemplate.delete(url);
    }
}

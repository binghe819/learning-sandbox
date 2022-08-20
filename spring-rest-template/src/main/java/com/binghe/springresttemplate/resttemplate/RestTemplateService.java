package com.binghe.springresttemplate.resttemplate;


import com.binghe.springresttemplate.user.application.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RestTemplateService {

    private static final String LOCAL_URL = "http://localhost:8080";

    /**
     * Get 요청에 대한 두 가지 처리 방식
     * 1. String으로 받아서 직접 Json 형식으로 변환
     * 2. RestTemplate.getForEntity를 이용하여 자동으로 Json 변환하도록.
     * 아래 코드는 2번째 방법.
     */
    public UserDto getUser(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = LOCAL_URL + "/users/" + id;
        ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("에러 발생");
        }
        return response.getBody();
    }

    /**
     * 배열 직렬화 방법
     * 1. 배열 사용
     * 2. 일급 컬렉션 객체 사용
     */
    public List<UserDto> getAllUser() {
        RestTemplate restTemplate = new RestTemplate();
        String url = LOCAL_URL + "/users";
        ResponseEntity<UserDto[]> response = restTemplate.getForEntity(url, UserDto[].class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("에러 발생");
        }
        return Arrays.asList(response.getBody());
    }

    /**
     * Post 요청에 대한 처리 방식
     * 1. postForObject (Post 요청후 응답을 특정 객체로 변환후 응답)
     * 2. postForEntity
     * 3. exchange
     */
    public Long saveByPostForObject(String name, int age) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, name, age), headers);

        Long response = restTemplate.postForObject(LOCAL_URL + "/users", request, Long.class);
        return response;
    }

    public Long saveByPostForEntity(String name, int age) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, name, age), headers);

        ResponseEntity<Long> response = restTemplate.postForEntity(LOCAL_URL + "/users", request, Long.class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("에러 발생");
        }
        return response.getBody();
    }

    public Long saveByExchange(String name, int age) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, name, age), headers);

        ResponseEntity<Long> response = restTemplate.exchange(LOCAL_URL + "/users", HttpMethod.POST, request, Long.class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("에러 발생");
        }

        return response.getBody();
    }

    public UserDto update(Long id, UserDto userDto) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<UserDto> request =
                new HttpEntity<>(new UserDto(null, userDto.getName(), userDto.getAge()), headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(LOCAL_URL + "/users/" + id, HttpMethod.PUT, request, UserDto.class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("에러 발생");
        }
        return response.getBody();
    }

    public void delete(Long id) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.delete(LOCAL_URL + "/users/" + id);
    }
}

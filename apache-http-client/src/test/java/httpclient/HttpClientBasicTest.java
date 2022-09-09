package httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class HttpClientBasicTest {

    @Test
    void GET_Quick_Start() throws IOException {
        // HttpClient 인스턴스 생성
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 요청할 HTTP Method에 맞는 인스턴스 생성.
        HttpGet httpGet = new HttpGet("http://localhost:8080/users");
        // HTTP 요청에 담을 내용 정의 (헤더와 바디)
        httpGet.addHeader("User-Agent", "Java Client; Mac OS");
        httpGet.addHeader("Accept", "*/*");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Keep-Alive", "timeout=5, max=100");

        // HTTP 요청 실행 (TCP 커넥션 + HTTP 요청)
        CloseableHttpResponse response = httpclient.execute(httpGet);

        // 응답 처리
        try {
            System.out.println(response.getStatusLine());                         // 응답의 시작줄
            Arrays.stream(response.getAllHeaders()).forEach(System.out::println); // 응답 헤더

            HttpEntity entity = response.getEntity();

            // 응답 바디 처리
            InputStream inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder responseBody = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            System.out.println(responseBody);

            // 응답 콘텐츠를 완전히 소비. (완전히 소비해줘야 다음 연결이 안전하게 사용될 수 있다.)
            EntityUtils.consume(entity);
        } finally {
            // 리소스 반환.
            response.close();
        }
    }

    @Test
    void POST_Quick_Start() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://localhost:8080/users");

        // 요청 헤더
        httpPost.addHeader("User-Agent", "Java Client; Mac OS");
        httpPost.addHeader("Accept", "*/*");
        httpPost.addHeader("Connection", "keep-alive");
        httpPost.addHeader("Keep-Alive", "timeout=5, max=100");

        // 요청 바디
        String requestBody = "{\"name\":\"testtest1\",\"age\":27}";
        httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

        // HTTP 요청 실행 (TCP 커넥션 + HTTP 요청)
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 응답 처리
        try {
            System.out.println(response.getStatusLine());                         // 응답의 시작줄
            Arrays.stream(response.getAllHeaders()).forEach(System.out::println); // 응답 헤더

            HttpEntity entity = response.getEntity();

            // 응답 바디 처리
            InputStream inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder responseBody = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            System.out.println(responseBody);

            // 응답 콘텐츠를 완전히 소비. (완전히 소비해줘야 다음 연결이 안전하게 사용될 수 있다.)
            EntityUtils.consume(entity);
        } finally {
            // 리소스 반환.
            response.close();
        }
    }
}

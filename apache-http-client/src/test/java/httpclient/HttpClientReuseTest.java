package httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class HttpClientReuseTest {

    @DisplayName("HttpClient 재사용이 가능한지 테스트 - 결론적으론 가능함.")
    @Test
    void GET_HttpClient_reuse() throws IOException {
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
        CloseableHttpResponse response2 = httpclient.execute(httpGet);

        // 응답 처리
        System.out.println(responseToString(response));
        System.out.println("----------------------");
        System.out.println(responseToString(response2));
    }

    private String responseToString(CloseableHttpResponse response) throws IOException {
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

            // 응답 콘텐츠를 완전히 소비. (완전히 소비해줘야 다음 연결이 안전하게 사용될 수 있다.)
            EntityUtils.consume(entity);

            return responseBody.toString();
        } finally {
            // 리소스 반환.
            response.close();
        }
    }
}

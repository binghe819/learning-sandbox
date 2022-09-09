package httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpClientInterceptorTest {

    @Test
    void HttpClient_Interceptor_test_Consequence() throws IOException {
        CloseableHttpClient httpclient = HttpClients.custom()
                .addInterceptorLast(new HttpRequestInterceptor() {

                    public void process(
                            final HttpRequest request,
                            final HttpContext context) throws HttpException, IOException {
                        AtomicInteger count = (AtomicInteger) context.getAttribute("count");
                        request.addHeader("Count", Integer.toString(count.getAndIncrement()));
                        System.out.println("#### 요청 수 : " + count);
                    }

                })
                .build();

        AtomicInteger count = new AtomicInteger(0);
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAttribute("count", count);

        HttpGet httpget = new HttpGet("http://localhost:8080/users");
        for (int i = 0; i < 10; i++) {
            CloseableHttpResponse response = httpclient.execute(httpget, localContext);
            try {
                System.out.println(responseToString(response));
            } finally {
                response.close();
            }
        }
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

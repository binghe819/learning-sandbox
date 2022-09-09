package httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;

public class ETCTest {

    @Test
    void ConfigTest() {
        // given
        RequestConfig requestConfig = RequestConfig.custom()
                .setMaxRedirects(1_00)
                .setRedirectsEnabled(false)
                .build();

        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(5, true);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionTimeToLive()
                .build();
    }
}

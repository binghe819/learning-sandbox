package com.binghe.client;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

import java.net.InetSocketAddress;

public class HttpHelloWorldClient {

    public static void main(String[] args) {
        // HttpClient (ClientTransport)
        HttpClient httpClient = HttpClient.create()
//                .bindAddress(() -> new InetSocketAddress("127.0.0.1", 8080))
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
                .host("127.0.0.1")
                .port(8080);

        String getHelloWorldResponse = getHelloWorld(httpClient);
        System.out.println(getHelloWorldResponse);
    }

    private static String getHelloWorld(HttpClient httpClient) {
//        return httpClient.get()
//                .uri("/helloworld")
//                .responseContent()
//                .aggregate()
//                .asString()
//                .block();

        return httpClient.get()
                .uri("/helloworld")
                .responseSingle((resp, bytes) -> {
                    System.out.printf("Status: " + resp.status());
                    System.out.printf("Header: " + resp.responseHeaders());
                    System.out.println("Content Body: " + bytes.asString());
                    return bytes.asString();
                })
                .block();
    }
}

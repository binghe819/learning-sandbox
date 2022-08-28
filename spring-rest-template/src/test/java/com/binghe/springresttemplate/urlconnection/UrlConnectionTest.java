package com.binghe.springresttemplate.urlconnection;

import com.binghe.springresttemplate.user.application.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class UrlConnectionTest {

    @Test
    void Get_read_responseHeader() {
        String url = "http://localhost:8080/users/1";

        try {
            URL urlObj = new URL(url);
            URLConnection urlConnection = urlObj.openConnection();

            // 요청 Header
            urlConnection.setRequestProperty("User-Agent", "Java Client; Mac OS");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestProperty("Connection", "keep-alive");

            // 응답 Header 가져오기
            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();

            headerFields.forEach((key, value) -> {
                System.out.println(key + " : " + value);
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void Get_read_responseBody() {
        String url = "http://localhost:8080/users/1";

        try {
            URL urlObj = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

            // 요청 Header
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("User-Agent", "Java Client; Mac OS");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestProperty("Connection", "keep-alive");

            // 응답 가져오기
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder responseBody = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null) {
                responseBody.append(line);
            }

            // 연결 종료
            reader.close();

            // 출력
            System.out.println(responseBody);
        } catch (MalformedURLException e) {
            System.out.println("URL 객체에 URL 주소를 잘못주면 발생하는 예외");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("openConnection는 입력 혹은 출력 스트림에서 예외가 발생하면 IOException을 던진다.");
            e.printStackTrace();
        }
    }

    @Test
    void Post_send_requestBody_and_read_responseBody() {
        String url = "http://localhost:8080/users";

        try {
            URL urlObj = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

            // 요청 Header
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("User-Agent", "Java Client; Mac OS");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestProperty("Connection", "keep-alive");

            // 요청 Body
            String requestBody = "{\"name\":\"testtest15\",\"age\":27}";
            try (OutputStream os = urlConnection.getOutputStream()){
                byte request_data[] = requestBody.getBytes("utf-8");
                os.write(request_data);
            } catch(Exception e) {
                e.printStackTrace();
            }

            // 연결 (HTTP 연결 실시)
            urlConnection.connect();

            // 응답 헤더 출력
            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();

            System.out.println("상태 코드: " + urlConnection.getResponseCode());
            headerFields.forEach((key, value) -> {
                System.out.println(key + " : " + value);
            });

            // 응답 바디 출력
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            StringBuilder responseBody = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            reader.close();

            // 응답 바디 출력
            System.out.println(responseBody);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

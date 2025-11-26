package com.binghe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 테스트용 TCP 서버입니다.
 * 수신한 문자열을 대문자로 변환하여 반환합니다.
 */
public class TestStringUpperServer {
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private ExecutorService clientHandlerExecutor;
    private volatile boolean running;

    public int start() throws IOException {
        serverSocket = new ServerSocket(0); // 임의의 사용 가능한 포트
        int port = serverSocket.getLocalPort();
        running = true;
        executor = Executors.newSingleThreadExecutor();
        clientHandlerExecutor = Executors.newCachedThreadPool();

        executor.submit(() -> {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientHandlerExecutor.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return port;
    }

    private void handleClient(Socket client) {
        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                // 받은 데이터를 대문자로 변환하여 전송
                String received = new String(buffer, 0, bytesRead, "UTF-8");
                String upperCased = received.toUpperCase();
                out.write(upperCased.getBytes("UTF-8"));
                out.flush();
            }
        } catch (IOException e) {
            // 클라이언트 연결이 끊어짐
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (clientHandlerExecutor != null) {
                clientHandlerExecutor.shutdownNow();
                clientHandlerExecutor.awaitTermination(1, TimeUnit.SECONDS);
            }
            if (executor != null) {
                executor.shutdownNow();
                executor.awaitTermination(1, TimeUnit.SECONDS);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

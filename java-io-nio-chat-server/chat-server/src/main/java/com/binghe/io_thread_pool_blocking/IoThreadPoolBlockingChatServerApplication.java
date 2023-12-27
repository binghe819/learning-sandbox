package com.binghe.io_thread_pool_blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IoThreadPoolBlockingChatServerApplication {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        List<Socket> sockets = new ArrayList<>();

        while (true) {
            // blocking call (새로운 클라이언트가 접속할 때까지 blocking 된다.)
            Socket socket = serverSocket.accept();

            sockets.add(socket);

            // 스레드 풀 개수인 3개이상 소켓 연결이 안된다.
            threadPool.submit(() -> {
                System.out.println("[클라이언트 접속] IP: " + socket.getInetAddress() + ", Port: " + socket.getPort());
                handleChat(socket, sockets);
            });
        }
    }

    private static void handleChat(Socket socket, List<Socket> sockets) {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            int data;

            // read() 메서드는 데이터를 읽을 때까지 blocking 된다. 만약 더이상 읽을 게 없다면 -1을 리턴한다.
            while ((data = in.read()) != -1) {
                // 서버에 연결된 모든 클라이언트에게 메시지를 전송한다.
                for (Socket s : sockets) {
                    s.getOutputStream().write(data);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            // sockets에서 해당 소켓을 제거.
        }
    }
}

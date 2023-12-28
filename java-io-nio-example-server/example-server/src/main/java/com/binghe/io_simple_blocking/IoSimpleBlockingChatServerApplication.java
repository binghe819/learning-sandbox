package com.binghe.io_simple_blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 첫번째. 간단한 블로킹 채팅 서버 - 메인 스레드가 모든 작업을 처리한다.
 *
 * - 장점: 구현이 간단하고, 코드가 쉽다.
 * - 단점: 메인스레드가 소켓 입출력 작업을 처리하기 때문에, 클라이언트 하나만 접속할 수 있다. 사실상 채팅 서버 역할을 수행하지 못한다.
 */
public class IoSimpleBlockingChatServerApplication {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            // blocking call (새로운 클라이언트가 접속할 때까지 blocking 된다.)
            Socket socket = serverSocket.accept();

            handleChat(socket);
        }
    }

    private static void handleChat(Socket socket) {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            int data;

            // read() 메서드는 데이터를 읽을 때까지 blocking 된다. 만약 더이상 읽을 게 없다면 -1을 리턴한다.
            while ((data = in.read()) != -1) {
                data = Character.isLetter(data) ? toUpperCase(data) : data;
                out.write(data);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static int toUpperCase(int data) {
        return Character.toUpperCase(data);
    }
}

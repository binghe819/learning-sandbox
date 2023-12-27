package com.binghe.nio_blocking;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioBlockingChatServerApplication {

    public static void main(String[] args) throws IOException {
        // 서버 생성
        ServerSocketChannel serverSocket = ServerSocketChannel.open();

        // 서버를 8080 포트에 바인딩.
        serverSocket.bind(new InetSocketAddress(8080));

        while (true) {
            // NIO의 accept() 메서드도 blocking 된다.
            SocketChannel socket = serverSocket.accept();

            handleChat(socket);
        }
    }

    // 소켓으로부터 읽고 작업을 수행한 다음 소켓에 다시 쓰기 작업을 수행한다.
    private static void handleChat(SocketChannel socketChannel) {
        // ByteBuffer는 NIO에서 데이터를 읽고 쓰는 작업을 수행할 때 사용하며, Direct 메서드 사용시 Native 메모리에 할당된다.
        // 단방향인 InputStream, OutputStream과는 다르게 양방향인 ByteBuffer는 읽기와 쓰기를 모두 수행할 수 있다.
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(80);

        try {
            int data;
            while ((data = socketChannel.read(byteBuffer)) != -1) {
                // 소켓으로부터 데이터를 읽는다.
                byteBuffer.flip(); // 읽기 모드로 전환. (position을 0으로 세팅한다.)

                // 작업을 수행한다.

                // 소켓에 데이터를 쓴다.
                while (byteBuffer.hasRemaining()) {
                    socketChannel.write(byteBuffer);
                }

                // 버퍼의 position을 다시 0으로 세팅한다.
                byteBuffer.compact();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

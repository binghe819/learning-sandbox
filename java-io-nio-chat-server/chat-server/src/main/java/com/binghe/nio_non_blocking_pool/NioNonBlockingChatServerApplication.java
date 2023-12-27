package com.binghe.nio_non_blocking_pool;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Nio Bad Practice (ByteBuffer를 이해하는데 좋은 예시)
 */
public class NioNonBlockingChatServerApplication {

    public static void main(String[] args) throws IOException {
        // 서버 생성
        ServerSocketChannel serverSocket = ServerSocketChannel.open();

        // 서버를 8080 포트에 바인딩.
        serverSocket.bind(new InetSocketAddress(8080));

        // Non-Blocking 모드로 전환한다.
        serverSocket.configureBlocking(false);

        // SocketChannel별로 하나의 ByteBuffer를 사용한다.
        Map<SocketChannel, ByteBuffer> sockets = new ConcurrentHashMap<>();

        while (true) {
            // 여기서 accept()는 들어오는 연결 요청을 수락한다는 의미이다.
            SocketChannel socket = serverSocket.accept();
            // Non-Blocking 모드이기에 위 accept() 메서드는 blocking 되지 않고, null을 리턴한다.

            if (socket != null) {
                // 연결된 Socket을 Non-Blocking하게 처리하겠다는 의미.
                socket.configureBlocking(false);

                // 매 Socket마다 하나의 ByteBuffer를 할당한다.
                sockets.put(socket, ByteBuffer.allocateDirect(80));
            }

            // 연결된 SocketChannel을 순회하면서, 연결이 끊긴 SocketChannel은 제거한다.
            sockets.keySet().removeIf(it -> !it.isOpen());

            // 연결된 SocketChannel을 순회하면서, 데이터를 읽고 작업을 수행한 다음 소켓에 다시 쓰기 작업을 수행한다.
            sockets.forEach((socketCh, byteBuffer) -> {
                try {
                    // Non-Blocking 모드이기에 Blocking 모드와 다르게 read() 메서드 호출시 blocking 되지 않는다.
                    int data = socketCh.read(byteBuffer);

                    if (data == -1) { // 연결이 끊긴 경우
                        closeSocket(socketCh);
                    } else if (data != 0) { // 데이터가 들어온 경우
                        byteBuffer.flip(); // position=0으로해서 Read 모드로 전환한다.

                        while (byteBuffer.hasRemaining()) {
                            ByteBuffer writeBuffer = ByteBuffer.allocate(80);
                            writeBuffer.put(byteBuffer);

                            // 서버에 연결된 모든 클라이언트에게 메시지를 전송한다. (Bad Practice)
                            sockets.forEach((s, b) -> {
                                if (s != socketCh) {
                                    try {
                                        writeBuffer.flip();
                                        s.write(writeBuffer);
                                    } catch (IOException e) {
                                        System.out.println("test");
                                        closeSocket(s);
                                        throw new UncheckedIOException(e);
                                    }
                                }
                            });

                            writeBuffer.compact();
                        }

                        byteBuffer.compact();
                    }
                } catch (IOException e) {
                    closeSocket(socketCh);
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private static void closeSocket(SocketChannel socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

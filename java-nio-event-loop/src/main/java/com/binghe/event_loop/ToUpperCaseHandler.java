package com.binghe.event_loop;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ToUpperCaseHandler implements Handler {

    private final SocketChannel socketChannel;
    private final SelectionKey selectionKey;
    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256);

    public ToUpperCaseHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);

        this.selectionKey = this.socketChannel.register(selector, SelectionKey.OP_READ);
        this.selectionKey.attach(this);
    }

    @Override
    public void handle() {
        try {
            if (selectionKey.isReadable()) {
                handleReadEvent();
            } else if (selectionKey.isWritable()) {
                handleWriteEvent();
            }
        } catch (IOException e) {
            closeSocket(socketChannel);
            throw new UncheckedIOException(e);
        }
    }

    private void handleReadEvent() throws IOException {
        // 데이터 읽기
        int data = socketChannel.read(byteBuffer);

        if (data == -1) {
            closeSocket(socketChannel);
        }

        if (data > 0) {
            byteBuffer.flip();
            // 비즈니스 실행
            toUpperCase(byteBuffer);
            // 쓰기 모드로 전환
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void handleWriteEvent() throws IOException {
        socketChannel.write(byteBuffer);

        while (!byteBuffer.hasRemaining()) {
            byteBuffer.compact();
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private static void closeSocket(SocketChannel socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void toUpperCase(final ByteBuffer byteBuffer) {
        // ByteBuffer내 모든 데이터를 읽어서 대문자로 변환한다.
        for (int x = 0; x < byteBuffer.limit(); x++) {
            byteBuffer.put(x, (byte) toUpperCase(byteBuffer.get(x)));
        }
    }

    private static int toUpperCase(int data) {
        return Character.isLetter(data) ? Character.toUpperCase(data) : data;
    }
}

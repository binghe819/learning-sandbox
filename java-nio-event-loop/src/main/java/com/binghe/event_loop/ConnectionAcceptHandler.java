package com.binghe.event_loop;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@AllArgsConstructor
public class ConnectionAcceptHandler implements Handler {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    @Override
    public void handle() {
        try {
            SocketChannel socket = serverSocketChannel.accept();

            if (socket != null) {
                ToUpperCaseHandler toUpperCaseHandler = new ToUpperCaseHandler(selector, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

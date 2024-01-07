package com.binghe.event_loop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

/**
 * Reactor - I/O 이벤트가 발생할 때까지 대기하다가 이벤트가 발생하면 처리할 수 있는 각 채널의 핸들러에게 dispatch 한다.
 */
public class EventLoop implements Runnable {

    private final ServerSocketChannel serverSocket;
    private final Selector selector;

    public EventLoop(int port) throws IOException {
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);

        this.selector = Selector.open();

        // ServerSocketChannel은 새로운 커넥션을 받아들이는 역할을한다. 따라서 OP_ACCEPT를 등록해준다.
        // ServerSocketChannel에 대한 이벤트는 새로운 커넥션에 대한 이벤트이기에, ServerSocketChannel엔 새로운 커넥션이 오면 처리해야하는 Handler를 등록해준다.
        SelectionKey selectionKey = this.serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new ConnectionAcceptHandler(serverSocket, selector));
    }

    @Override
    public void run() {
        // I/O 이벤트가 발생할 때까지 대기하다가 이벤트가 발생하면 처리할 수 있는 핸들러에게 dispatch 한다.
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey selectionKey : selected) {
                    dispatch(selectionKey);
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 이벤트에 알맞는 Handler에게 dispatch.
    private void dispatch(SelectionKey selectionKey) {
        Handler handler = (Handler) selectionKey.attachment();
        handler.handle();
    }
}

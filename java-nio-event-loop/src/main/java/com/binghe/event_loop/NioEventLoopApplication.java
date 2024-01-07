package com.binghe.event_loop;

import java.io.IOException;

public class NioEventLoopApplication {

    public static void main(String[] args) throws IOException {
        new EventLoop(8080).run();
    }
}

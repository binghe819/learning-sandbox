package com.binghe.event_loop;

/**
 * 여기선 Channel 1 : 1 Handler이다.
 *
 * Netty에선 Channel 1 : N Handler (pipeline)를 지원한다.
 */
public interface Handler {

    void handle();
}

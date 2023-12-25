package com.binghe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 1의 클라이언트-서버간의 커넥션 관리 객체.
 */
@Builder
@AllArgsConstructor
@Getter
public class ClientConnection {

    /**
     * 클라이언트의 이름.
     */
    private final String name;

    /**
     * 클라이언트와 연결된 소켓.
     */
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
}

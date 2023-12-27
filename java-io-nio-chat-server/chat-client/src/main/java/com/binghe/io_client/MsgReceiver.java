package com.binghe.io_client;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

@AllArgsConstructor
public class MsgReceiver implements Runnable {

    private final Socket socket;

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream()) {
            int data;

            // char 하나당 한번씩 반복.
            while ((data = in.read()) != -1) {
                System.out.print((char) data); // 문자 하나.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

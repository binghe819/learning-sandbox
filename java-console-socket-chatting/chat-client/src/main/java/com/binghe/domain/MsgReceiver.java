package com.binghe.domain;

import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.net.Socket;

@AllArgsConstructor
public class MsgReceiver implements Runnable {

    private final Socket socket;
    private boolean exit;

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            while (in != null && !exit) {
                printReceivedMsg(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printReceivedMsg(DataInputStream inputStream) {
        try {
            System.out.println(inputStream.readUTF());
            System.out.println();
        } catch (Exception e) {
        }
    }
}

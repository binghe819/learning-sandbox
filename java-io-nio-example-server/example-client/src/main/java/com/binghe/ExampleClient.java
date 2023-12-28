package com.binghe;

import java.io.IOException;
import java.net.Socket;

public class ExampleClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);

        new Thread(new MsgReceiver(socket)).start();
        new Thread(new MsgSender(socket)).start();
    }
}

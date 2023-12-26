package com.binghe.domain;

import com.binghe.presentation.InputView;
import lombok.AllArgsConstructor;

import java.io.DataOutputStream;
import java.net.Socket;

@AllArgsConstructor
public class MsgSender implements Runnable {

    private final Socket socket;
    private final String roomId;
    private boolean exit;

    @Override
    public void run() {
        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            // 소켓 연결후 가장 먼저 roomId 전송.
            if (out != null) {
                out.writeUTF(roomId);
            }

            while (true) {
                String msg = InputView.inputMsg();

                if (msg.isBlank()) {
                    continue;
                }

                if ("exit".equals(msg)) {
                    socket.close();
                    exit = true;
                    return;
                }

                out.writeUTF(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

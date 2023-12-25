package com.binghe.domain;

import com.binghe.presentation.OutputView;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;

@AllArgsConstructor
public class MsgReceiver implements Runnable {

    private final Room room;
    private final ClientConnection clientConnection;

    @Override
    public void run() {
        try {
            OutputView.printServerText(clientConnection.getName() + "입장하셨습니다.");
            OutputView.printServerText("현재 접속자 수 : " + room.getNumberOfClients());
            room.sendToAll(clientConnection, " 입장하셨습니다.");

            DataInputStream dataInputStream = new DataInputStream(clientConnection.getIn());

            while (dataInputStream != null) {
                room.sendToAll(clientConnection, dataInputStream.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
            OutputView.printServerText(clientConnection.getName() + "님이 퇴장하셨습니다.");
        } finally {
            room.sendToAll(clientConnection," 퇴장하셨습니다.");
            room.remove(clientConnection);
        }
    }
}

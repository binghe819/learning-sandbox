package com.binghe.domain;

import com.binghe.presentation.OutputView;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.DataOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Room {

    /**
     * 방 번호
     */
    private final String id;

    /**
     * 방 이름
     */
    private final String name;

    /**
     * 방에 접속한 클라이언트들
     */
    private final Map<String, ClientConnection> clientConnections;

    public void add(ClientConnection clientConnection) {
        try {
            clientConnections.put(clientConnection.getName(), clientConnection);
            MsgReceiver msgReceiver = new MsgReceiver(this, clientConnection);
            new Thread(msgReceiver).start();
        } catch (Exception e) {
            OutputView.printServerText("클라이언트 연결 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public void sendToAll(ClientConnection sender, String message) {
        Iterator<String> it = clientConnections.keySet().iterator();

        while (it.hasNext()) {
            try {
                DataOutputStream out = new DataOutputStream(clientConnections.get(it.next()).getOut());
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                out.writeUTF(now + " [" + sender.getName() + "] " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getNumberOfClients() {
        return clientConnections.size();
    }

    public void remove(ClientConnection clientConnection) {
        clientConnections.remove(clientConnection.getName());
    }
}

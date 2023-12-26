package com.binghe.service;

import com.binghe.domain.ClientConnection;
import com.binghe.domain.Room;
import com.binghe.presentation.OutputView;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class ChatServerService {

    private final int port;
    private final List<Room> rooms;

    public void startChattingServer() {
        ServerSocket serverSocket;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(port);

            OutputView.printServerText("Server is running on port " + port);

            // 새로운 클라이언트가 접속할 때마다 새로운 소켓을 생성하여 저장한다.
            while (true) {
                socket = serverSocket.accept(); // 스레드 블로킹.

                String userName = socket.getInetAddress() + " : " + socket.getPort();
                OutputView.printServerText(userName + " connected.");

                ClientConnection clientConnection = ClientConnection.builder()
                        .socket(socket)
                        .name(userName)
                        .in(socket.getInputStream())
                        .out(socket.getOutputStream())
                        .build();

                // 소켓 연결후 첫 통신으로 방 번호를 받아 접속한다.
                String roomId = new DataInputStream(clientConnection.getIn()).readUTF();
                Room room = findById(roomId);

                room.add(clientConnection);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Room findById(String roomId) {
        Room room = rooms.stream()
                .filter(it -> roomId.equals(it.getId()))
                .findFirst()
                .orElse(null);

        if (room == null) {
            Room newRoom = new Room(roomId, roomId, new HashMap<>());
            rooms.add(newRoom);
            return newRoom;
        }
        return room;
    }
}

package com.binghe.service;

import com.binghe.domain.MsgReceiver;
import com.binghe.domain.MsgSender;
import com.binghe.presentation.InputView;

import java.io.IOException;
import java.net.Socket;

public class ChatClientService {

    public void startChatClient() throws IOException {
        // 채팅 서버 접속에 필요한 정보 입력
        String chatServerIp = InputView.inputChatServerIp();
        Integer chatServerPort = InputView.inputChatServerPort();
        String roomId = InputView.inputRoomId();

        // 소켓 연결
        Socket socket = new Socket(chatServerIp, chatServerPort);

        // Sender, Receiver 실행
        boolean exit = false;
        new Thread(new MsgSender(socket, roomId, exit)).start();
        new Thread(new MsgReceiver(socket, exit)).start();
    }
}

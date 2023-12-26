package com.binghe;

import com.binghe.service.ChatClientService;

import java.io.IOException;

public class ChatClientApplication {

    public static void main(String[] args) throws IOException {
        new ChatClientService().startChatClient();
    }
}

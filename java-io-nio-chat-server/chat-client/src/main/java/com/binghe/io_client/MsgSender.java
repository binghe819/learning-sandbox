package com.binghe.io_client;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@AllArgsConstructor
public class MsgSender implements Runnable {

    private static final Scanner scanner = new Scanner(System.in);
    private final Socket socket;

    @Override
    public void run() {
        try (OutputStream out = socket.getOutputStream()) {
            while (true) {
                String input = input();

                if (input.isBlank()) {
                    continue;
                }

                // input 문자열 사이즈만큼 반복하며 byte 전송.
                out.write(input.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String input() {
        return scanner.nextLine();
    }
}

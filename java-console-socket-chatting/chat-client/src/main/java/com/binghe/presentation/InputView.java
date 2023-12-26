package com.binghe.presentation;

import com.binghe.domain.validate.IntergerValidation;
import com.binghe.domain.validate.IpAddressValidation;

import java.util.Scanner;

public class InputView {

    private static final Scanner scanner = new Scanner(System.in);

    private InputView() {
    }

    public static String inputChatServerIp() {
        try {
            System.out.print("IP > ");
            String input = scanner.nextLine();

            if (!IpAddressValidation.validate(input)) {
                throw new IllegalArgumentException("IP 형식이 올바르지 않습니다.");
            }

            return input;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return inputChatServerIp();
        }
    }

    public static Integer inputChatServerPort() {
        try {
            System.out.print("PORT > ");
            String input = scanner.nextLine();

            if (!IntergerValidation.validate(input)) {
                throw new IllegalArgumentException("PORT는 숫자만 입력 가능합니다.");
            }

            return Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return inputChatServerPort();
        }
    }

    public static String inputRoomId() {
        try {
            System.out.print("ROOM ID > ");
            return scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return inputRoomId();
        }
    }

    public static String inputMsg() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return inputMsg();
        }
    }
}

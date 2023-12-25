package com.binghe.presentation;

import java.util.Scanner;

public class InputView {

    private static final Scanner scanner = new Scanner(System.in);

    private InputView() {
    }

    public static String inputServerPort() {
        OutputView.printServerText("Enter the port number to start the server : ");
        return scanner.nextLine();
    }
}

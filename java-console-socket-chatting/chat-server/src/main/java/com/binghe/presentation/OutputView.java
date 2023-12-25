package com.binghe.presentation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OutputView {

    private OutputView() {
    }

    public static void printServerText(String text) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("[%s] %s%n", now, text);
    }

    public static void printClientText(String name, String text) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("[%s] %s: %s%n", now, name, text);
    }
}

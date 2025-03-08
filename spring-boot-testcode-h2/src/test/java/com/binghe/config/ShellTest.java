package com.binghe.config;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellTest {

    @Test
    void test() throws IOException {
        // port
        int port = 6379;

        // netstat
        String command = String.format("netstat -nat | grep LISTEN | grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        Process process = Runtime.getRuntime().exec(shell);

        // InputStream으로 읽기
        String line;
        StringBuilder pidInfo = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        }

        System.out.println(pidInfo.toString());
    }
}

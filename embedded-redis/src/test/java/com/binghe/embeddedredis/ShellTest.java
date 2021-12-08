package com.binghe.embeddedredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Test;

public class ShellTest {

    @Test
    void test() throws IOException {
        // port
        int port = 22;

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

//    private boolean isRunning(Process process) {
//        String line;
//        StringBuilder pidInfo = new StringBuilder();
//
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//
//            while ((line = input.readLine()) != null) {
//                pidInfo.append(line);
//            }
//
//        } catch (Exception e) {
//            System.out.println("### Error!!!");
//        }
//
//        return !pidInfo.toString().isEmpty();
//    }
//
//    @Test
//    void test1() {
//        String command = String.format("ps -aux");
//
//    }
}

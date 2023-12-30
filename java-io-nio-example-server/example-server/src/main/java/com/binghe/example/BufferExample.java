package com.binghe.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BufferExample {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("temp.txt");
        write(path);
        read(path);
    }

    private static void write(Path path) throws IOException {
        String input = "NIO Buffer Hello World!";
        byte[] inputBytes = input.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputBytes);
        FileChannel channelWrite = FileChannel.open(path,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        channelWrite.write(byteBuffer);
        channelWrite.close();
    }

    private static void read(Path path) throws IOException {
        FileChannel channelRead = FileChannel.open(path);
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        channelRead.read(byteBuffer);
        byte[] byteArray = byteBuffer.array();
        String fileContent = new String(byteArray).trim();
        System.out.println("File Content: " + fileContent);
        channelRead.close();
    }
}

package com.binghe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClientConnection {

    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;

    public TcpClientConnection(String host, int port, int connectTimeoutMs, int socketReadTimeoutMs) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(host, port), connectTimeoutMs);
        this.socket.setSoTimeout(socketReadTimeoutMs);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public void write(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return;
        }
        out.write(data);
        out.flush();
    }

    public void write(String message) throws IOException {
        if (message == null || message.isEmpty()) {
            return;
        }
        write(message.getBytes("UTF-8"));
    }

    public byte[] read(int maxBytes) throws IOException {
        byte[] buffer = new byte[maxBytes];
        int bytesRead = in.read(buffer); // Blocking 콜 (응답이 올 때 까지 기다림)
        
        if (bytesRead == -1) {
            throw new IOException("Connection closed by remote host");
        }
        
        if (bytesRead < maxBytes) {
            byte[] result = new byte[bytesRead];
            System.arraycopy(buffer, 0, result, 0, bytesRead);
            return result;
        }
        
        return buffer;
    }

    public int read(byte[] buffer) throws IOException {
        int bytesRead = in.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Connection closed by remote host");
        }
        return bytesRead;
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        int bytesRead = in.read(buffer, offset, length);
        if (bytesRead == -1) {
            throw new IOException("Connection closed by remote host");
        }
        return bytesRead;
    }

    

    public boolean isValid() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public void close() {
        try { in.close(); } catch (IOException ignored) {}
        try { out.close(); } catch (IOException ignored) {}
        try { socket.close(); } catch (IOException ignored) {}
    }
}

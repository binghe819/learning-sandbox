package com.binghe;

import com.binghe.service.ChatServerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ChatServerApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("Private IP : " + getPrivateIPAddresses());
        System.out.println("Public IP : " + getPublicIp());
        ChatServerService chatServerService = new ChatServerService(8080, new ArrayList<>());
        chatServerService.startChattingServer();
    }

    // 공인 IP 조회하는 메서드
    public static String getPublicIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<String> getPrivateIPAddresses() throws SocketException {
        List<String> privateIPs = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();

                    // Check if it's a private IP address
                    if (isPrivateIPAddress(inetAddress.getHostAddress())) {
                        privateIPs.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return privateIPs;
    }

    private static boolean isPrivateIPAddress(String ipAddress) {
        return ipAddress.startsWith("192.168.") || ipAddress.startsWith("10.") || ipAddress.startsWith("172.")
                || ipAddress.startsWith("169.254.");
    }
}

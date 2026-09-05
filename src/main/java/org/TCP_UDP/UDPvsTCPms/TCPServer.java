package org.TCP_UDP.UDPvsTCPms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        System.out.println("TCP-serveren lytter på port 12501...");

        try (ServerSocket serverSocket = new ServerSocket(12501);
             Socket socket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Klient forbundet: " + socket.getInetAddress());

            int antal = 1000;
            int modtaget = 0;
            String linje;

            while ((linje = in.readLine()) != null) {
                if (linje.equals("FÆRDIG")) {
                    break;
                }
                modtaget++;
            }

            System.out.println("Modtaget: " + modtaget + " af " + antal);
            System.out.println("Tabte: " + (antal - modtaget));

        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
        }
    }
}
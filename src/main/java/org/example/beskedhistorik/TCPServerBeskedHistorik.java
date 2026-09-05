package org.example.beskedhistorik;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerBeskedHistorik {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is listening on port 5000");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Hver klient får sin egen tråd – så serveren kan vente på flere klienter
                TCPClientHandler handler = new TCPClientHandler(socket);
                new Thread(handler).start();
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}
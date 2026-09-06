package org.Multithreading_Threadpools.ChatServer;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultithreadedServer {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server kører på port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept(); // venter på en klient
                System.out.println("Ny klient forbundet: " + socket.getInetAddress());
                new ClientHandler(socket).start();     // én tråd pr. klient
            }
        } catch (IOException e) {
            System.err.println("Serverfejl: " + e.getMessage());
        }
    }
}
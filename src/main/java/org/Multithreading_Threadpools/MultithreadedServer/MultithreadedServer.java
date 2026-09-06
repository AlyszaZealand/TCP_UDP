package org.Multithreading_Threadpools.MultithreadedServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultithreadedServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        // Server-socket i try-with-resources, så den lukkes automatisk
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is listening on port " + PORT);

            // Evig løkke: vent på klienter
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Opret en NY tråd pr. klient og start den med det samme
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
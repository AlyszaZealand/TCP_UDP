package org.Multithreading_Threadpools.FTPapp;

import java.io.*;
import java.net.*;

public class SimpleFTPServer {
    private static final int PORT = 5000; // Server port
    private static final String SERVER_DIRECTORY = "C:\\Temp\\Srv"; // Directory for files

    public static void main(String[] args) {
        // Ensure the server directory exists (mkdirs creates the whole path)
        File dir = new File(SERVER_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                // Accept a new client connection
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Handle the client in a new thread
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
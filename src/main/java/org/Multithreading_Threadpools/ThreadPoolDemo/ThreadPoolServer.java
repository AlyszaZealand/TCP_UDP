package org.Multithreading_Threadpools.ThreadPoolDemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolServer {
    private static final int PORT = 5000;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        // Opret server-socket med try-with-resources
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE)) {

            System.out.println("Server is listening on port " + PORT);

            // Lyt efter klientforbindelser i en evig løkke
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Håndter klienten via trådpoolen
                threadPool.submit(new ClientHandler(socket));
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

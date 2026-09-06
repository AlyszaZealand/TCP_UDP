package org.Multithreading_Threadpools.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolServer {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(5);

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server running on port 1234...");

            while (true) {
                Socket socket = serverSocket.accept();
                pool.submit(new ClientHandler(socket));
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            pool.shutdown();
            System.out.println("Thread pool closed.");
        }
    }
}
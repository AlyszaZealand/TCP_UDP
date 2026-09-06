package org.Multithreading_Threadpools.FejlhåndteringOgRessourcestyring;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolServer {
    private static final int PORT = 5000;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        // Try-with-resources: serverSocket og trådpoolen lukkes automatisk
        // (close() på ExecutorService kalder internt shutdown())
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE)) {

            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected: " +
                            socket.getInetAddress());

                    // Send klienten videre til trådpoolen
                    threadPool.submit(new ClientHandler(socket));

                } catch (SocketException ex) {
                    // Fejl på én enkelt forbindelse skal ikke dræbe serveren
                    System.out.println("Connection error: " + ex.getMessage());
                }
            }
        } catch (BindException ex) {
            System.err.println("Port " + PORT + " is already in use!");
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
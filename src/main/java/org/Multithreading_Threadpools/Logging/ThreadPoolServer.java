package org.Multithreading_Threadpools.Logging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolServer {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server kører på port 8080...");
            Logger.log("Server startet");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    pool.execute(new ClientHandler(socket));
                } catch (IOException e) {
                    Logger.log("FEJL: Kunne ikke acceptere klient: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            Logger.log("KRITISK: Serveren kunne ikke starte: " + e.getMessage());
        }
    }
}
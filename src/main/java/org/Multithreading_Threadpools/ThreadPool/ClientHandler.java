package org.Multithreading_Threadpools.ThreadPool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        int clientNumber = counter.incrementAndGet();

        try (Socket s = socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            out.println("Velkommen klient nr. " + clientNumber);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client " + clientNumber + ": " + message);
                out.println("Server replied: " + message);
            }

        } catch (Exception e) {
            System.out.println("Error with client " + clientNumber + ": " + e.getMessage());
        }

        System.out.println("Client " + clientNumber + " finished.");
    }
}
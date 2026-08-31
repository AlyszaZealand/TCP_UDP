package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPServerWithTimeout {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is listening on port 5000");
            Socket socket = serverSocket.accept();
            socket.setSoTimeout(5000); // Indstiller en timeout på 5 sekunder
            System.out.println("New client connected");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            try {
                String message = reader.readLine();
                System.out.println("Received message: " + message);
                writer.println("Echo: " + message);
            } catch (SocketTimeoutException sockettimeoutexception) {
                System.out.println("Read timed out");
            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }


    }
}

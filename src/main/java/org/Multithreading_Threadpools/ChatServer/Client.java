package org.Multithreading_Threadpools.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8080);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner keyboard = new Scanner(System.in);

        // Read messages from the server in the background
        Thread listener = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception ignored) {}
        });
        listener.start();

        // Send what the user types to the server
        while (keyboard.hasNextLine()) {
            String message = keyboard.nextLine();
            out.println(message);
        }

        socket.close();
    }
}
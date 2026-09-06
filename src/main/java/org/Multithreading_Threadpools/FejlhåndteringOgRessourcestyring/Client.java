package org.Multithreading_Threadpools.FejlhåndteringOgRessourcestyring;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server. Write a message ('bye' to quit):");

            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                writer.println(userInput);

                String response = reader.readLine();
                if (response == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }
                System.out.println("Server: " + response);

                if (userInput.equalsIgnoreCase("bye")) {
                    System.out.println("Closing connection...");
                    break;
                }
            }
        } catch (UnknownHostException ex) {
            System.err.println("Unknown host: " + SERVER_ADDRESS);
        } catch (IOException ex) {
            System.out.println("Could not connect to server: " + ex.getMessage());
        }
    }
}
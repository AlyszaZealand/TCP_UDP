package org.TCP_UDP.BeskedHistorik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClientBeskedHistorik {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;

        try (
                Socket socket = new Socket(hostname, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader keyboard = new BufferedReader(
                        new InputStreamReader(System.in))
        ) {
            System.out.println("Forbundet til serveren!");

            // Lytte-tråd: læser ALT, serveren sender, og udskriver det løbende
            Thread listener = new Thread(() -> {
                try (
                        BufferedReader serverReader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()))
                ) {
                    String line;
                    while ((line = serverReader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Forbindelsen til serveren blev lukket.");
                }
            });
            listener.setDaemon(true);   // tråden lukker, når main stopper
            listener.start();

            // Hovedtråden: læs fra tastaturet og send
            System.out.println("Skriv en besked (skriv 'exit' for at afslutte):");
            String line;
            while ((line = keyboard.readLine()) != null) {
                writer.println(line.trim());
                if (line.trim().equals("exit")) {
                    break;
                }
            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}

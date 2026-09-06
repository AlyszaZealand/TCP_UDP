package org.Multithreading_Threadpools.ChatServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private String username = "Anonym";

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Velkommen! Skriv dit brugernavn:");
            String navn = in.readLine();
            if (navn != null && !navn.isBlank()) {
                username = navn.trim();
            }
            out.println("Hej " + username + "! Skriv beskeder, eller /joke for en nørdejoke.");

            String besked;
            while ((besked = in.readLine()) != null) {
                System.out.println(username + " skrev: " + besked);

                if (besked.startsWith("/joke")) {
                    out.println("Joke: Hvorfor går programmører ikke ud i skoven? "
                            + "Fordi der er for mange bugs!");
                } else {
                    out.println("Echo: " + besked);
                }
            }
        } catch (IOException e) {
            System.err.println("Fejl ved håndtering af klient " + username + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println(username + " afbrød forbindelsen.");
            } catch (IOException e) {
                System.err.println("Kunne ikke lukke socket: " + e.getMessage());
            }
        }
    }
}
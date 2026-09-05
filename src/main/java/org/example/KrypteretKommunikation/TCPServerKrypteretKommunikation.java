package org.example.KrypteretKommunikation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerKrypteretKommunikation {

    public static void main(String[] args) throws IOException {
        System.out.println("Server startet på port 12345...");

        try (ServerSocket serverSocket = new ServerSocket(12345)) {

            try (Socket socket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Klient forbundet!");
                String besked;

                while ((besked = in.readLine()) != null) {
                    if (besked.equalsIgnoreCase("exit")) {
                        System.out.println("Klienten afsluttede forbindelsen.");
                        break;
                    }
                    System.out.println("Klient siger: " + besked);
                    String svar = rot13(besked);
                    out.println(svar);
                }
            }
        }
        System.out.println("Server lukket.");
    }

    // Roterer hvert bogstav 13 pladser frem i alfabetet
    public static String rot13(String input) {
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                c = (char) ('a' + (c - 'a' + 13) % 26);
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) ('A' + (c - 'A' + 13) % 26);
            }
            // Alt andet (tal, mellemrum, tegn) ændres ikke
            sb.append(c);
        }
        return sb.toString();
    }
}
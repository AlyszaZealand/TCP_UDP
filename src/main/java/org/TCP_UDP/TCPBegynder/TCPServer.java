package org.TCP_UDP.TCPBegynder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer { // klasse
    public static void main(String[] args) { // main methode, her starter programmet
        // try-with-resources statement, som automatisk lukker ressourcerne efter brug, her ServerSocket objektet, som lytter på port 5000
        // try-with-resources indikeres med try() parenteser
        // try-catch indikeres med try{} catch(){} blokke.
        try (ServerSocket serverSocket = new ServerSocket(5000)) { // instantierer (opretter) et nyt objekt af ServerSocket klassen, som lytter på port 5000 som argument
            // og erklærer en variable af typen ServerSocket, som hedder serverSocket
            System.out.println("Server is listening on port 5000"); // kan se programmet kører
            Socket socket = serverSocket.accept(); // erklærer en variable af typen Socket, som hedder socket
            // jeg kalder metoden accept() på serverSocket objektet, som venter på en klientforbindelse
            // når en klient etablerer forbindelse, returnerer accept() et nyt Socket objekt
            System.out.println("New client connected"); // kan ses først når en klient har oprettet forbindelse

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // erklærer en variable af typen BufferedReader, som hedder reader,
            // og instantierer (opretter) et nyt objekt af BufferedReader klassen, som læser fra socket objektets input stream

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // erklærer en variable af typen PrintWriter, som hedder writer,
            // og instantierer (opretter) et nyt objekt af PrintWriter klassen, som skriver til socket objektets output stream

            String message = reader.readLine(); // erklærer en variable af typen String, som hedder message,
            // og som initialiseres med værdien af reader objektets readLine() metode, som læser en linje tekst fra klienten

            System.out.println("Received message: " + message); // udskriver klientens besked til konsollen
            writer.println("Echo: " + message); // skriver "Echo: " + message til klienten via writer objektet


        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}

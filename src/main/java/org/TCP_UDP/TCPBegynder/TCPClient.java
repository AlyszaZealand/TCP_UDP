package org.TCP_UDP.TCPBegynder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient { // klasse
    public static void main(String[] args) {
        String hostname = "localhost"; // variable af typen String, som erklæret navnet hostname, og som initialiseres med værdien "localhost"
        int port = 5000; // variable af typen int, som erklæret navnet port, og som initialiseres med værdien 5000

        try (Socket socket = new Socket(hostname, port); // erklærer en variable af typen Socket, som hedder socket,
             // og instantierer (opretter) et nyt objekt af Socket klassen, som opretter forbindelse til serveren på hostname og port

             //NOTE: OutputStream skriver data UD (Klient -> Server)
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // erklærer en variable af typen PrintWriter, som hedder writer,
             // og instantierer (opretter) et nyt objekt af PrintWriter klassen, som skriver til socket objektets output stream

             //NOTE: InputStream læser data IND (Server -> Klient)
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // erklærer en variable af typen BufferedReader,som hedder reader,
            // og instantierer (opretter) et nyt objekt af BufferedReader klassen, som læser fra socket objektets input stream

            writer.println("Hello Server"); // skriver "Hello Server" til serveren via writer objektet

            String response = reader.readLine(); // erklærer en variable af typen String, som hedder response,
            // og som initialiseres med værdien af reader objektets readLine() metode

            System.out.println("Server response: " + response); // udskriver serverens svar til konsollen

        } catch (IOException ioexception) {
        ioexception.printStackTrace();
        }
    }
}

package org.TCP_UDP.TCPTimeout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClientWithTimeout {
    public static void main(String[] args) {

        String hostname = "localhost"; // variable af typen String, som erklæret navnet hostname,
        // og som initialiseres med værdien "localhost"
        int port = 5000; // variable af typen int, som erklæret navnet port,
        // og som initialiseres med værdien 5000

        try (Socket socket = new Socket()) { // erklærer en variable af typen Socket, som hedder socket,
            // og instantierer (opretter) et nyt objekt af Socket klassen

            socket.connect(new InetSocketAddress(hostname, port), 5000); // Indstiller en connection timeout på 5 sekunder
            socket.setSoTimeout(5000); // Indstiller en read timeout på 5 sekunder

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("Hello Server");
            try {
                String response = reader.readLine();
                System.out.println("Server response: " + response);
            } catch (SocketTimeoutException sockettimeoutexception) {
                System.out.println("Read timed out");
            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}

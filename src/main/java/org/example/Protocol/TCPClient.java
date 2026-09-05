package org.example.Protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Indtast dit brugernavn: ");
        String navn = scanner.nextLine();

        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Forbundet til serveren! Skriv beskeder (afslut med 'stop'):");

            while (true) {
                System.out.print("> ");
                String besked = scanner.nextLine();

                if (besked.equalsIgnoreCase("stop")) {
                    break;
                }

                // Protokollen sørger for både format og tidsstempel
                out.println(Protocol.lavBesked(navn, besked));

                String svar = in.readLine();
                System.out.println("Server: " + svar);
            }

        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

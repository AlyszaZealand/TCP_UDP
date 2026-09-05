package org.example.krypteretkommunikation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClientKrypteretKommunikation {

    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Forbundet til serveren. Skriv en besked ('exit' for at stoppe):");

            while (true) {
                System.out.print("Klient: ");
                String besked = scanner.nextLine();

                out.println(besked); // send til serveren

                if (besked.equalsIgnoreCase("exit")) {
                    System.out.println("Forbindelsen lukkes.");
                    break;
                }

                String svar = in.readLine(); // vent på krypteret svar
                System.out.println("Server (ROT13): " + svar);
            }
        }
    }
}
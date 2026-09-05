package org.TCP_UDP.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server kører på port 1234. Venter på klient...");

            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                System.out.println("Klient forbundet!");

                String line;
                while ((line = in.readLine()) != null) {
                    String[] dele = line.split("\\|");

                    if (Protocol.erGyldig(dele)) {
                        System.out.println("----------------------------");
                        System.out.println("Brugernavn : " + Protocol.hentNavn(dele));
                        System.out.println("Tidspunkt  : " + Protocol.hentTid(dele));
                        System.out.println("Besked     : " + Protocol.hentBesked(dele));

                        out.println(Protocol.okSvar());
                    } else {
                        out.println(Protocol.fejlsSvar());
                    }
                }

                System.out.println("Klient afbrød forbindelsen.");
            }

        } catch (IOException e) {
            System.out.println("Fejl: " + e.getMessage());
        }
    }
}

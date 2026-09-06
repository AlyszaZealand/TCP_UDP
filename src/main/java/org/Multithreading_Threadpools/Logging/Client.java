package org.Multithreading_Threadpools.Logging;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner tastatur = new Scanner(System.in)) {

            System.out.println("Skriv beskeder ('stop' for at afslutte):");
            while (true) {
                String tekst = tastatur.nextLine();
                out.println(tekst);
                System.out.println(in.readLine());
                if (tekst.equalsIgnoreCase("stop")) break;
            }
        } catch (IOException e) {
            System.out.println("Forbindelsesfejl: " + e.getMessage());
        }
    }
}
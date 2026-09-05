package org.TCP_UDP.UDPvsTCPms;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Hvor mange beskeder skal sendes? (f.eks. 1000) ");
        int antal = scanner.nextInt();

        try (Socket socket = new Socket("localhost", 12501);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Sender " + antal + " beskeder via TCP...");

            long start = System.currentTimeMillis();
            for (int i = 1; i <= antal; i++) {
                out.println("Besked " + i);
            }
            out.println("FÆRDIG");
            long slut = System.currentTimeMillis();

            System.out.println("Tid: " + (slut - start) + " ms");

        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
        }

        scanner.close();
    }
}
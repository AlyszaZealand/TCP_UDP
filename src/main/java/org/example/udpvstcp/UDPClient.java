package org.example.udpvstcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Hvor mange beskeder skal sendes? (f.eks. 1000) ");
        int antal = scanner.nextInt();

        try (DatagramSocket socket = new DatagramSocket()) {
            java.net.InetAddress adresse = java.net.InetAddress.getByName("localhost");

            System.out.println("Sender " + antal + " beskeder via UDP...");

            long start = System.currentTimeMillis();
            for (int i = 1; i <= antal; i++) {
                byte[] data = ("Besked " + i).getBytes();
                socket.send(new DatagramPacket(data, data.length, adresse, 12502));
            }

            // Send "FÆRDIG" tre gange, da én kopi sagtens kan gå tabt
            byte[] færdig = "FÆRDIG".getBytes();
            for (int i = 0; i < 3; i++) {
                socket.send(new DatagramPacket(færdig, færdig.length, adresse, 12502));
            }
            long slut = System.currentTimeMillis();

            System.out.println("Tid: " + (slut - start) + " ms");

        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
        }

        scanner.close();
    }
}

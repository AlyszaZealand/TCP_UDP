package org.example.udpvstcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        System.out.println("UDP-serveren lytter på port 12502...");
        System.out.println("Venter på første besked fra klienten...");

        try (DatagramSocket socket = new DatagramSocket(12502)) {
            byte[] buffer = new byte[1024];
            int antal = 1000;
            int modtaget = 0;

            // 1) Vent på FØRSTE besked uden tidsgrænse (blokerer indtil klienten sender)
            DatagramPacket pakke = new DatagramPacket(buffer, buffer.length);
            socket.receive(pakke);
            String besked = new String(pakke.getData(), 0, pakke.getLength());
            if (!besked.equals("FÆRDIG")) {
                modtaget++;
            }

            // 2) Nu er gangen i gang — sæt kun en kort timeout for de resterende
            socket.setSoTimeout(2000); // stop 2 sekunder efter sidste besked

            while (true) {
                pakke = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(pakke);
                    besked = new String(pakke.getData(), 0, pakke.getLength());
                    if (besked.equals("FÆRDIG")) {
                        break;
                    }
                    modtaget++;
                } catch (java.net.SocketTimeoutException e) {
                    break; // ingen flere beskeder kommer
                }
            }

            System.out.println("Modtaget: " + modtaget + " af " + antal);
            System.out.println("Det var: " + (antal - modtaget) + " tabt");

        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
        }
    }
}
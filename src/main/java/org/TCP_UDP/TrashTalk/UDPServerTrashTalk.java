package org.TCP_UDP.TrashTalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

public class UDPServerTrashTalk {
    public static void main(String[] args) {
        String[] trashTalkMessages = {
                "Du spiller som en floppy disk!",
                "Min mormor har hurtigere ping!",
                "Er din internetforbindelse fra 1998?",
                "Selv min toaster kan regne hurtigere end dig!",
                "Har du lagt tastaturet i køleskabet?",
                "Du rammer lige så sjældent som en Wi-Fi forbindelse i kælderen!",
                "Ping: 12 ms. Din IQ: samme tal.",
                "Jeg har set bedre refleksioner fra en inaktiv skærm!",
                "Selv Pac-Man-størrelsen ved bedre at flygte fra dig!",
                "Du er grunden til, at der findes tutorials."
        };
        Random random = new Random();


        try (DatagramSocket socket = new DatagramSocket(5000)) {
            System.out.println("Trash talk-server kører på port 5000...");
            byte[] buffer = new byte[512];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + message);

                // Vælg en tilfældig trash talk-besked
                String response = trashTalkMessages[random.nextInt(trashTalkMessages.length)];
                byte[] responseBytes = response.getBytes();

                // Send svaret tilbage til klientens adresse og port
                DatagramPacket responsePacket = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        packet.getAddress(), packet.getPort());
                socket.send(responsePacket);

            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}

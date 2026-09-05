package org.example.beskedhistorik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class TCPClientHandler implements Runnable {
    private static ArrayList<String> messageHistory = new ArrayList<>();
    private static ArrayList<PrintWriter> clients = new ArrayList<>();

    // Palette af ANSI-farver – rækkefølgen er tilfældig, pointen er bare at de skiller sig ud
    private static final String[] COLORS = {
            "\u001B[31m",   // rød
            "\u001B[32m",   // grøn
            "\u001B[33m",   // gul
            "\u001B[34m",   // blå
            "\u001B[35m",   // magenta
            "\u001B[36m"    // cyan
    };
    private static final String RESET = "\u001B[0m";

    private static int nextColorIndex = 0;   // tæller, så hver ny klient får næste farve

    private final Socket socket;
    private final String username;
    private final String color;      // denne klients farve
    private PrintWriter writer;

    public TCPClientHandler(Socket socket) {
        this.socket = socket;
        this.username = "[" + socket.getPort() + "]";
        this.color = COLORS[nextColorIndex % COLORS.length];   // kører i ring, så der aldrig går "tomme"
        nextColorIndex++;
    }

    @Override
    public void run() {
        handleClient();
    }

    public void handleClient() {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            writer = new PrintWriter(socket.getOutputStream(), true);
            clients.add(writer);

            writer.println(colorMsg("Velkommen " + username + " til chatten!"));
            broadcast(colorMsg(username + " er kommet med i chatten."));

            sendHistory(writer);

            String message;
            while ((message = reader.readLine()) != null) {
                message = message.trim();

                if (message.isEmpty()) {
                    writer.println("Tom besked - prøv igen.");
                    continue;
                }

                if (message.equals("exit")) {
                    System.out.println(username + " vil afslutte.");
                    break;
                }

                messageHistory.add(message);
                System.out.println("Received message from " + coloredName() + ": " + message);

                broadcast(coloredName() + " " + message);
            }

        } catch (IOException e) {
            System.out.println("En klient afbrød forbindelsen.");
        } finally {
            clients.remove(writer);
            broadcast(coloredName() + " har forladt chatten.");
            System.out.println(username + " er taget ud af chatten.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendHistory(PrintWriter writer) {
        writer.println("--- Beskedhistorik ---");
        for (String oldMessage : messageHistory) {
            writer.println(oldMessage);
        }
        writer.println("--- Slut på historik ---");
    }

    // Navnet med farve omkring, fx "[49290]" i rødt
    private String coloredName() {
        return color + username + RESET;
    }

    // Hele beskeden i samme farve som navnet (kan ændres efter smag)
    private String colorMsg(String message) {
        return color + message + RESET;
    }

    private static synchronized void broadcast(String message) {
        for (PrintWriter writer : clients) {
            writer.println(message);
        }
    }
}

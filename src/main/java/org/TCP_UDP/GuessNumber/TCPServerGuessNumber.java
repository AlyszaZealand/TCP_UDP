package org.TCP_UDP.GuessNumber;
import java.io.*;
import java.net.*;

public class TCPServerGuessNumber {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is listening on port 5000");
            Socket socket = serverSocket.accept(); // venter på en klientforbindelse
            System.out.println("New client connected");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // genereres KUN EN gang, før løkken starter
            int numberToGuess = (int) (Math.random() * 10) + 1;
            System.out.println("(Secret number: " + numberToGuess + ")");

            // serveren sender instruktion til klienten
            writer.println("Guess a number between 1 and 10");

            while (true) {
                String input = reader.readLine();
                if (input == null) {
                    break; // klienten har lukket forbindelsen
                }

                try {
                    int guess = Integer.parseInt(input.trim());

                    if (guess == numberToGuess) {
                        writer.println("Correct! The number was " + numberToGuess);
                        System.out.println("Client guessed correctly: " + guess);
                        break; // afslutter løkken, når gættet er korrekt
                    } else if (guess < numberToGuess) {
                        writer.println("Too low, try again.");
                        System.out.println("Client guessed too low: " + guess);
                    } else {
                        writer.println("Too high, try again.");
                        System.out.println("Client guessed too high: " + guess);
                    }
                } catch (NumberFormatException e) {
                    writer.println("Please enter a valid number.");
                    System.out.println("Invalid input from client.");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

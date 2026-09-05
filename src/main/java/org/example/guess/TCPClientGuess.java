package org.example.guess;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClientGuess {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);

            // Modtager serverens instruktion ("Gæt 1 - 10")
            System.out.println("Server: " + reader.readLine());

            boolean correctGuess = false;
            while (!correctGuess) {
                System.out.print("Your guess: ");
                String input = scanner.nextLine();


                int guess;
                try {
                    guess = Integer.parseInt(input.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue; // spring tilbage til starten af løkken
                }

                writer.println(guess); // Send gættet til serveren

                String response = reader.readLine(); // vent på serverens svar
                System.out.println("Server response: " + response);

                if (response.startsWith("Correct!")) {
                    correctGuess = true;
                }
            }
            System.out.println("Game over. Thanks for playing!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

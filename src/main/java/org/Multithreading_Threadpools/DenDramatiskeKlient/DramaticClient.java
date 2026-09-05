package org.Multithreading_Threadpools.DenDramatiskeKlient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DramaticClient {

    private static final String HOST =
            "localhost";

    private static final int PORT = 5002;

    public static void main(String[] args) {

        try (
                Socket socket =
                        new Socket(HOST, PORT);

                BufferedReader serverReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        );

                PrintWriter serverWriter =
                        new PrintWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream(),
                                        StandardCharsets.UTF_8
                                ),
                                true
                        );

                BufferedReader keyboard =
                        new BufferedReader(
                                new InputStreamReader(
                                        System.in,
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            System.out.println(
                    "Forbundet til serveren"
            );

            System.out.println(
                    "Skriv fx SAY|hej"
            );

            System.out.println(
                    "Skriv QUIT for en pæn afslutning"
            );

            System.out.println();
            System.out.println(
                    "Eller stop klienten direkte i IntelliJ"
            );

            System.out.println(
                    "hvis du vil smække med døren."
            );

            while (true) {

                System.out.print("> ");

                String request =
                        keyboard.readLine();

                if (request == null) {
                    break;
                }

                serverWriter.println(request);

                String response =
                        serverReader.readLine();

                if (response == null) {

                    System.out.println(
                            "Serveren lukkede forbindelsen"
                    );

                    break;
                }

                System.out.println(
                        "Server: " + response
                );

                if ("BYE".equals(response)) {
                    break;
                }
            }

        } catch (IOException exception) {

            System.out.println(
                    "Forbindelsen forsvandt: "
                            + exception.getMessage()
            );
        }
    }
}
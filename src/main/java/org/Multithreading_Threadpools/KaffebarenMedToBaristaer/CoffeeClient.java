package org.Multithreading_Threadpools.KaffebarenMedToBaristaer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CoffeeClient {

    private static final String HOST =
            "localhost";

    private static final int PORT = 5001;

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
                    "Velkommen til Java Beans"
            );

            System.out.println(
                    "Skriv fx COFFEE|Latte"
            );

            System.out.println(
                    "Skriv QUIT for at gå"
            );

            while (true) {

                System.out.print("> ");

                String request =
                        keyboard.readLine();

                if (request == null) {
                    break;
                }

                serverWriter.println(request);

                String firstResponse =
                        serverReader.readLine();

                if (firstResponse == null) {

                    System.out.println(
                            "Kaffebaren lukkede"
                    );

                    break;
                }

                System.out.println(
                        "Server: " + firstResponse
                );

                if (firstResponse.startsWith(
                        "ORDER_RECEIVED|"
                )) {

                    System.out.println(
                            "Baristaen arbejder..."
                    );

                    String readyResponse =
                            serverReader.readLine();

                    System.out.println(
                            "Server: " + readyResponse
                    );
                }

                if ("BYE".equals(firstResponse)) {
                    break;
                }
            }

        } catch (IOException exception) {

            System.out.println(
                    "Kunne ikke komme ind på kaffebaren: "
                            + exception.getMessage()
            );
        }
    }
}
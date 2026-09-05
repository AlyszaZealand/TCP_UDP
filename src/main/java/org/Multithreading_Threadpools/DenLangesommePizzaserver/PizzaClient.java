package org.Multithreading_Threadpools.DenLangesommePizzaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PizzaClient {

    private static final String HOST = "localhost";

    private static final int PORT = 5000;

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
                    "Forbundet til pizzaserveren"
            );

            System.out.println(
                    "Skriv fx PIZZA|Pepperoni"
            );

            System.out.println(
                    "Skriv QUIT for at afslutte"
            );

            while (true) {

                System.out.print("> ");

                String request =
                        keyboard.readLine();

                if (request == null) {
                    break;
                }

                /*
                 * Send kommandoen til serveren.
                 */
                serverWriter.println(request);

                /*
                 * Vent på serverens første svar.
                 */
                String firstResponse =
                        serverReader.readLine();

                if (firstResponse == null) {

                    System.out.println(
                            "Serveren lukkede forbindelsen"
                    );

                    break;
                }

                System.out.println(
                        "Server: " + firstResponse
                );

                /*
                 * Ved en pizzaordre kommer der to svar:
                 *
                 * ORDER_RECEIVED
                 * READY
                 */
                if (firstResponse.startsWith(
                        "ORDER_RECEIVED|"
                )) {

                    System.out.println(
                            "Venter på pizzaen..."
                    );

                    String secondResponse =
                            serverReader.readLine();

                    System.out.println(
                            "Server: " + secondResponse
                    );
                }

                if ("BYE".equals(firstResponse)) {
                    break;
                }
            }

        } catch (IOException exception) {

            System.out.println(
                    "Kunne ikke kommunikere med serveren: "
                            + exception.getMessage()
            );
        }
    }
}

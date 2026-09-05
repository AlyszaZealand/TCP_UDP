package org.Multithreading_Threadpools.DenLangsommePizzaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SequentialPizzaServer {

    private static final int PORT = 5000;

    // Vi lader med vilje en pizza tage lang tid.
    // Det gør problemet med den sekventielle server tydeligt.
    private static final long PIZZA_TIME_MS = 10_000;

    public static void main(String[] args) {

        System.out.println(
                "Mario's langsomme pizzaserver starter på port " + PORT
        );

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                System.out.println("Venter på næste kunde...");

                /*
                 * accept() blokerer, indtil en klient forbinder.
                 */
                Socket clientSocket = serverSocket.accept();

                System.out.println(
                        "Ny kunde: "
                                + clientSocket.getRemoteSocketAddress()
                );

                /*
                 * PROBLEMET:
                 *
                 * Main-tråden håndterer selv hele klienten.
                 *
                 * Så længe handleClient() arbejder,
                 * kommer serveren ikke tilbage til accept().
                 */
                handleClient(clientSocket);
            }

        } catch (IOException exception) {

            System.out.println(
                    "Serverfejl: " + exception.getMessage()
            );
        }
    }

    private static void handleClient(Socket clientSocket) {

        try (
                Socket socket = clientSocket;

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        );

                PrintWriter writer =
                        new PrintWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream(),
                                        StandardCharsets.UTF_8
                                ),
                                true
                        )
        ) {

            String request;

            while ((request = reader.readLine()) != null) {

                System.out.println(
                        "Kunden sendte: " + request
                );

                if ("QUIT".equalsIgnoreCase(request)) {

                    writer.println("BYE");
                    break;
                }

                if (request.startsWith("PIZZA|")) {

                    String pizza =
                            request.substring("PIZZA|".length()).trim();

                    if (pizza.isBlank()) {

                        writer.println(
                                "ERROR|Du skal vælge en pizza"
                        );

                        continue;
                    }

                    writer.println(
                            "ORDER_RECEIVED|" + pizza
                    );

                    System.out.println(
                            "Mario laver " + pizza
                    );

                    /*
                     * Vi simulerer, at pizzaen tager
                     * 10 sekunder at lave.
                     *
                     * Main-tråden er blokeret imens.
                     */
                    Thread.sleep(PIZZA_TIME_MS);

                    writer.println(
                            "READY|" + pizza
                    );

                    System.out.println(
                            pizza + " er klar"
                    );

                } else {

                    writer.println(
                            "ERROR|Ukendt kommando"
                    );
                }
            }

        } catch (InterruptedException exception) {

            /*
             * Hvis tråden bliver interrupted,
             * gendanner vi interrupt-status.
             */
            Thread.currentThread().interrupt();

            System.out.println(
                    "Pizzabagningen blev afbrudt"
            );

        } catch (IOException exception) {

            System.out.println(
                    "Fejl hos klient: "
                            + exception.getMessage()
            );
        }
    }
}
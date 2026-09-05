package org.Multithreading_Threadpools.DenLangesommePizzaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
 * Denne klasse håndterer præcis én klient.
 *
 * Den implementerer Runnable.
 *
 * Runnable betyder:
 * "Dette objekt beskriver en opgave, der kan udføres."
 *
 * Runnable betyder IKKE:
 * "Der bliver automatisk lavet en ny tråd."
 */
public class PizzaClientHandler implements Runnable {

    private static final long PIZZA_TIME_MS = 10_000;

    private final Socket clientSocket;

    public PizzaClientHandler(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        /*
         * Nu kan vi se, hvilken tråd der
         * faktisk udfører denne handler.
         */
        String threadName =
                Thread.currentThread().getName();

        System.out.printf(
                "[%s] Starter kunde %s%n",
                threadName,
                clientSocket.getRemoteSocketAddress()
        );

        try (
                /*
                 * ClientHandler har fået socketen.
                 *
                 * Handleren har derfor også ansvaret
                 * for at lukke den.
                 */
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

                    System.out.printf(
                            "[%s] Laver %s...%n",
                            threadName,
                            pizza
                    );

                    /*
                     * Kun denne klienttråd sover.
                     *
                     * Serverens main-tråd kan stadig
                     * acceptere nye klienter.
                     */
                    Thread.sleep(PIZZA_TIME_MS);

                    writer.println(
                            "READY|" + pizza
                    );

                    System.out.printf(
                            "[%s] %s er klar%n",
                            threadName,
                            pizza
                    );

                } else {

                    writer.println(
                            "ERROR|Ukendt kommando"
                    );
                }
            }

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            System.out.printf(
                    "[%s] Arbejdet blev afbrudt%n",
                    threadName
            );

        } catch (IOException exception) {

            System.out.printf(
                    "[%s] Klientfejl: %s%n",
                    threadName,
                    exception.getMessage()
            );
        }

        System.out.printf(
                "[%s] Kunden er færdig%n",
                threadName
        );
    }
}
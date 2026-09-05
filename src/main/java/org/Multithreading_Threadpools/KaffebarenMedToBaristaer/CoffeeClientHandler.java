package org.Multithreading_Threadpools.KaffebarenMedToBaristaer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CoffeeClientHandler implements Runnable {

    /*
     * Vi gør kaffen langsom med vilje,
     * så vi kan se trådpuljen arbejde.
     */
    private static final long BREW_TIME_MS = 8_000;

    private final Socket clientSocket;

    public CoffeeClientHandler(
            Socket clientSocket
    ) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        /*
         * Hvilken worker-tråd har fået opgaven?
         */
        String workerName =
                Thread.currentThread().getName();

        System.out.printf(
                "[%s] Begynder at betjene %s%n",
                workerName,
                clientSocket.getRemoteSocketAddress()
        );

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

                if ("QUIT".equalsIgnoreCase(request)) {

                    writer.println("BYE");
                    break;
                }

                if (request.startsWith("COFFEE|")) {

                    String coffee =
                            request.substring(
                                    "COFFEE|".length()
                            ).trim();

                    if (coffee.isBlank()) {

                        writer.println(
                                "ERROR|Hvilken kaffe?"
                        );

                        continue;
                    }

                    writer.println(
                            "ORDER_RECEIVED|" + coffee
                    );

                    System.out.printf(
                            "[%s] laver %s%n",
                            workerName,
                            coffee
                    );

                    /*
                     * Worker-tråden er optaget,
                     * mens kaffen bliver lavet.
                     */
                    Thread.sleep(BREW_TIME_MS);

                    writer.println(
                            "READY|" + coffee
                    );

                    System.out.printf(
                            "[%s] %s er klar%n",
                            workerName,
                            coffee
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
                    "[%s] Kaffebrygningen blev afbrudt%n",
                    workerName
            );

        } catch (IOException exception) {

            System.out.printf(
                    "[%s] Klientfejl: %s%n",
                    workerName,
                    exception.getMessage()
            );
        }

        /*
         * Handleren er nu færdig.
         *
         * Worker-tråden kan genbruges
         * til en anden opgave.
         */
        System.out.printf(
                "[%s] Klar til næste kunde%n",
                workerName
        );
    }
}
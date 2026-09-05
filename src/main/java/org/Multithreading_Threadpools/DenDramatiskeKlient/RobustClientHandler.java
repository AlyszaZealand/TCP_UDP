package org.Multithreading_Threadpools.DenDramatiskeKlient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class RobustClientHandler implements Runnable {

    private final Socket clientSocket;

    public RobustClientHandler(
            Socket clientSocket
    ) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        String workerName =
                Thread.currentThread().getName();

        try (
                /*
                 * ClientHandler har overtaget socketen.
                 *
                 * Derfor har handleren også ansvaret
                 * for at lukke den.
                 *
                 * try-with-resources gør det automatisk.
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

            System.out.printf(
                    "[%s] Klienten er klar: %s%n",
                    workerName,
                    socket.getRemoteSocketAddress()
            );

            while (true) {

                /*
                 * readLine() blokerer,
                 * mens vi venter på data.
                 */
                String request =
                        reader.readLine();

                /*
                 * Hvis klienten lukker sin forbindelse,
                 * kan readLine() returnere null.
                 */
                if (request == null) {

                    System.out.printf(
                            "[%s] Klienten forsvandt uden at sige QUIT%n",
                            workerName
                    );

                    break;
                }

                System.out.printf(
                        "[%s] Modtaget: %s%n",
                        workerName,
                        request
                );

                /*
                 * Kontrolleret afslutning.
                 */
                if ("QUIT".equalsIgnoreCase(request)) {

                    writer.println("BYE");

                    System.out.printf(
                            "[%s] Klienten sagde pænt farvel%n",
                            workerName
                    );

                    break;
                }

                /*
                 * En meget simpel protokol.
                 */
                if (request.startsWith("SAY|")) {

                    String message =
                            request.substring(
                                    "SAY|".length()
                            );

                    writer.println(
                            "OK|" + message
                    );

                } else {

                    writer.println(
                            "ERROR|Ukendt kommando"
                    );
                }
            }

        } catch (SocketException exception) {

            /*
             * Ved en mere pludselig afbrydelse
             * kan der komme en SocketException.
             *
             * Fejlen håndteres KUN i denne handler.
             *
             * De andre klienter fortsætter.
             */
            System.out.printf(
                    "[%s] Klienten smækkede med døren: %s%n",
                    workerName,
                    exception.getMessage()
            );

        } catch (IOException exception) {

            /*
             * Andre kommunikationsfejl håndteres
             * også lokalt i denne ClientHandler.
             */
            System.out.printf(
                    "[%s] Kommunikationsfejl: %s%n",
                    workerName,
                    exception.getMessage()
            );
        }

        /*
         * Kun denne handler afsluttes.
         *
         * Serverens main-tråd og de andre
         * handlers fortsætter.
         */
        System.out.printf(
                "[%s] Handler afsluttet - serveren lever stadig%n",
                workerName
        );
    }
}
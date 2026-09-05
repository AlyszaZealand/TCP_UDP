package org.Multithreading_Threadpools.KaffebarenMedToBaristaer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoffeeServer {

    private static final int PORT = 5001;

    /*
     * Kaffebaren har kun to baristaer.
     *
     * Det svarer til to worker-tråde.
     */
    private static final int NUMBER_OF_BARISTAS = 2;

    public static void main(String[] args) {

        /*
         * Opret en fast trådpulje.
         *
         * Puljen har præcis to worker-tråde.
         */
        ExecutorService baristaPool =
                Executors.newFixedThreadPool(
                        NUMBER_OF_BARISTAS
                );

        System.out.printf(
                "Java Beans åbner på port %d med %d baristaer%n",
                PORT,
                NUMBER_OF_BARISTAS
        );

        try (
                ServerSocket serverSocket =
                        new ServerSocket(PORT)
        ) {

            while (true) {

                /*
                 * Serverens main-tråd accepterer klienter.
                 */
                Socket clientSocket =
                        serverSocket.accept();

                System.out.println(
                        "Ny kunde: "
                                + clientSocket.getRemoteSocketAddress()
                );

                /*
                 * ClientHandler er en OPGAVE.
                 *
                 * Den er ikke selv en tråd.
                 */
                CoffeeClientHandler order =
                        new CoffeeClientHandler(
                                clientSocket
                        );

                /*
                 * Vi laver IKKE:
                 *
                 * new Thread(order).start();
                 *
                 * I stedet afleverer vi opgaven
                 * til ExecutorService.
                 */
                baristaPool.execute(order);

                /*
                 * Hvis begge worker-tråde er optaget,
                 * placeres opgaven i køen.
                 */
            }

        } catch (IOException exception) {

            System.out.println(
                    "Serverfejl: "
                            + exception.getMessage()
            );

        } finally {

            /*
             * Når serveren afslutter,
             * lukker vi også trådpuljen.
             */
            baristaPool.shutdown();
        }
    }
}
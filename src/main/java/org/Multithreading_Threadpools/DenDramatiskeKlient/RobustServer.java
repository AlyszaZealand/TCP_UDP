package org.Multithreading_Threadpools.DenDramatiskeKlient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobustServer {

    private static final int PORT = 5002;

    private static final int NUMBER_OF_WORKERS = 4;

    public static void main(String[] args) {

        ExecutorService clientPool =
                Executors.newFixedThreadPool(
                        NUMBER_OF_WORKERS
                );

        System.out.println(
                "Den robuste server starter på port "
                        + PORT
        );

        try (
                ServerSocket serverSocket =
                        new ServerSocket(PORT)
        ) {

            while (true) {

                /*
                 * Serverens main-tråd har ét vigtigt job:
                 *
                 * Modtag nye klienter.
                 */
                Socket clientSocket =
                        serverSocket.accept();

                System.out.println(
                        "Ny klient: "
                                + clientSocket.getRemoteSocketAddress()
                );

                /*
                 * Den løbende kommunikation
                 * flyttes til en ClientHandler.
                 */
                RobustClientHandler handler =
                        new RobustClientHandler(
                                clientSocket
                        );

                /*
                 * Handleren afleveres til trådpuljen.
                 */
                clientPool.execute(handler);
            }

        } catch (IOException exception) {

            /*
             * Her håndteres fejl på SERVER-niveau.
             *
             * Fejl hos den enkelte klient
             * håndteres i ClientHandler.
             */
            System.out.println(
                    "Serverfejl: "
                            + exception.getMessage()
            );

        } finally {

            clientPool.shutdown();
        }
    }
}
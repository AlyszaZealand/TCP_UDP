package org.Multithreading_Threadpools.DenLangsommePizzaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedPizzaServer {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        System.out.println(
                "Mario's multithreaded pizzaserver starter på port "
                        + PORT
        );

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            while (true) {

                /*
                 * Main-tråden venter på en ny klient.
                 */
                Socket clientSocket =
                        serverSocket.accept();

                System.out.println(
                        "Ny kunde: "
                                + clientSocket.getRemoteSocketAddress()
                );

                /*
                 * ClientHandler indeholder arbejdet
                 * med én klient.
                 */
                PizzaClientHandler handler =
                        new PizzaClientHandler(
                                clientSocket
                        );

                /*
                 * VIGTIGT:
                 *
                 * new Thread(handler) opretter
                 * et Thread-objekt.
                 *
                 * start() starter en NY tråd.
                 *
                 * Den nye tråd kalder derefter
                 * handler.run().
                 */
                Thread clientThread =
                        new Thread(handler);

                clientThread.start();

                /*
                 * Main-tråden fortsætter med det samme.
                 *
                 * Den kan derfor gå tilbage
                 * til accept().
                 */
            }

        } catch (IOException exception) {

            System.out.println(
                    "Serverfejl: "
                            + exception.getMessage()
            );
        }
    }
}
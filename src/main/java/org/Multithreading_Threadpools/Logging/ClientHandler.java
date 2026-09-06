package org.Multithreading_Threadpools.Logging;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Socket s = socket;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            s.setSoTimeout(60_000); //timeout
            Logger.log("Klient forbundet: " + s.getRemoteSocketAddress());

            String besked;
            while ((besked = in.readLine()) != null) {
                Logger.log("Modtaget: " + besked);
                out.println("Echo: " + besked);
            }
            Logger.log("Klient lukkede selv forbindelsen");

        } catch (SocketTimeoutException e) {
            Logger.log("FEJL: Timeout - klienten svarede ikke: " + e.getMessage());
        } catch (IOException e) {
            Logger.log("FEJL: Klient afbrød midt i overførsel: " + e.getMessage());
        } catch (NullPointerException e) {
            Logger.log("FEJL: Uventet null-værdi: " + e.getMessage());
        }
    }
}
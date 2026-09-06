package org.Multithreading_Threadpools.FejlhåndteringOgRessourcestyring;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // try-with-resources sørger for, at streams OG socket lukkes
        try (socket;
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String text;
            while ((text = reader.readLine()) != null) {
                System.out.println("Received from client: " + text);
                writer.println("Echo: " + text);
            }
        } catch (SocketException ex) {
            System.out.println("Client disconnected abruptly: " + ex.getMessage());
        } catch (EOFException ex) {
            System.out.println("Client closed the stream.");
        } catch (IOException ex) {
            System.out.println("Handler exception: " + ex.getMessage());
        } finally {
            System.out.println("Handler finished for client: " +
                    socket.getRemoteSocketAddress());
        }
    }
}
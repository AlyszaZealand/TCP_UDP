package org.Multithreading_Threadpools.FTPapp;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private static final String SERVER_DIRECTORY = "C:\\Temp\\Srv";

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = socket.getOutputStream()) {

            // Read the requested file name from the client
            String fileName = reader.readLine();
            System.out.println("Client requested: " + fileName);

            // Send the file to the client
            sendFile(fileName, output);
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Could not close socket: " + ex.getMessage());
            }
        }
    }

    private void sendFile(String fileName, OutputStream outputStream) throws IOException {
        // Prevent path traversal: only use the file name, not a full path
        File file = new File(SERVER_DIRECTORY, new File(fileName).getName());

        if (file.exists() && file.isFile()) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[8192]; // larger buffer = faster transfer
                int bytesRead;

                // Read the file and send it to the client in chunks
                while ((bytesRead = bis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
            System.out.println("File sent to client: " + fileName);
        } else {
            // Send an error message if the file does not exist
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println("FILE NOT FOUND");
            System.out.println("File not found: " + fileName);
        }
    }
}
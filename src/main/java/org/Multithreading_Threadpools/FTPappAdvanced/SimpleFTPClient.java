package org.Multithreading_Threadpools.FTPappAdvanced;

import java.io.*;
import java.net.*;

public class SimpleFTPClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final String CLIENT_DIRECTORY = "C:\\Temp\\klient"; // Files are saved here

    public static void main(String[] args) {
        // Ensure the client directory exists
        File dir = new File(CLIENT_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true)) {

            // Ask the user for the command, e.g. "UPLOAD file.txt" or "DOWNLOAD file.txt"
            System.out.print("Enter command (UPLOAD filename / DOWNLOAD filename): ");
            String[] parts = consoleReader.readLine().trim().split("\\s+", 2);

            if (parts.length != 2) {
                System.out.println("Usage: UPLOAD filename | DOWNLOAD filename");
                return;
            }

            String command = parts[0].toUpperCase();
            String fileName = new File(parts[1]).getName();
            serverWriter.println(command + " " + fileName); // Send command + file name to server

            if (command.equals("UPLOAD")) {
                String response = serverReader.readLine(); // wait for "READY"
                if (response.equals("READY")) {
                    uploadFile(fileName, socket.getOutputStream());
                } else {
                    System.out.println("Server is not ready: " + response);
                }
            } else if (command.equals("DOWNLOAD")) {
                downloadFile(fileName, socket.getInputStream());
            }
        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Client -> Server: read local file and send its bytes
    private static void uploadFile(String fileName, OutputStream outputStream) throws IOException {
        File file = new File(CLIENT_DIRECTORY, fileName);

        if (!file.exists() || !file.isFile()) {
            System.out.println("Local file not found: " + fileName);
            return;
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            System.out.println("File uploaded: " + fileName);
        }
    }

    // Server -> Client: read incoming bytes and save to C:\Temp\klient
    private static void downloadFile(String fileName, InputStream inputStream) throws IOException {
        File file = new File(CLIENT_DIRECTORY, fileName);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
            System.out.println("File downloaded: " + fileName);
        }
    }
}
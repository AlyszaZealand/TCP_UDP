package org.Multithreading_Threadpools.FTPappAdvanced;

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
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true)) {

            // Read the command, e.g. "UPLOAD file.txt" or "DOWNLOAD file.txt"
            String line = reader.readLine();
            System.out.println("Client command: " + line);

            String[] parts = line.trim().split("\\s+", 2);
            String command = parts[0].toUpperCase();
            String fileName = new File(parts[1]).getName();

            if (command.equals("UPLOAD")) {
                writer.println("READY");          // tell client we are ready for bytes
                uploadFile(fileName, input);
            } else if (command.equals("DOWNLOAD")) {
                downloadFile(fileName, output);
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Could not close socket: " + ex.getMessage());
            }
        }
    }

    // Client -> Server : read incoming bytes and save to C:\Temp\Srv
    private void uploadFile(String fileName, InputStream input) throws IOException {
        File file = new File(SERVER_DIRECTORY, fileName);

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        }
        System.out.println("File received from client: " + fileName);
    }

    // Server -> Client : send the file, or "-1" as an error signal
    private void downloadFile(String fileName, OutputStream output) throws IOException {
        File file = new File(SERVER_DIRECTORY, fileName);

        if (file.exists() && file.isFile()) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush();
            }
            System.out.println("File sent to client: " + fileName);
        } else {
            PrintWriter writer = new PrintWriter(output, true);
            writer.println("-1"); // error signal
            System.out.println("File not found: " + fileName);
        }
    }
}
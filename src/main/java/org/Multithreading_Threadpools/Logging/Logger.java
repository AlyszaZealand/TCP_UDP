package org.Multithreading_Threadpools.Logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Logger {

    private static final String LOG_FILE =
            "src/main/java/org/Multithreading_Threadpools/Logging/serverlog.txt";

    public static void log(String tekst) {
        String linje = LocalDateTime.now() + " | " + tekst;
        System.out.println(linje);
        try (PrintWriter log = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            log.println(linje);
        } catch (IOException e) {
            System.err.println("Kunne ikke skrive til logfil: " + e.getMessage());
        }
    }
}
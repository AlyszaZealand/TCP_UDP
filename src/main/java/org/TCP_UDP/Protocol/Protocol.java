package org.TCP_UDP.Protocol;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Protocol {

    // Reglerne for vores protokol ligger samlet her
    private static final String SEPARATOR   = "|";
    private static final String OK_SVAR     = "OK: besked modtaget";
    private static final String FEJL_SVAR   = "FEJL: forkert format! Brug NAVN|TID|BESKED";

    // Bygger en besked i formatet: NAVN|TID|BESKED
    public static String lavBesked(String navn, String besked) {
        String tid = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        return navn + SEPARATOR + tid + SEPARATOR + besked;
    }

    // Returnerer true hvis beskeden følger protokollen
    public static boolean erGyldig(String[] dele) {
        return dele.length == 3;
    }

    // Henter navnet ud af de splittede dele
    public static String hentNavn(String[] dele) {
        return dele[0];
    }

    // Henter tidspunktet ud af de splittede dele
    public static String hentTid(String[] dele) {
        return dele[1];
    }

    // Henter selve beskeden ud af de splittede dele
    public static String hentBesked(String[] dele) {
        return dele[2];
    }

    // Serverens svar ved en gyldig besked
    public static String okSvar() {
        return OK_SVAR;
    }

    // Serverens svar ved en ugyldig besked
    public static String fejlsSvar() {
        return FEJL_SVAR;
    }
}
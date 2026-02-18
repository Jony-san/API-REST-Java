package com.chakray.usersapi.validation;

public class RfcValidator {

    /**
     * Buscamos el formato
     * 4 letras
     * 6 números (fecha)
     * 3 alfanuméricos
     */
    private static final String RFC_REGEX =
            "^[A-Z]{4}[0-9]{6}[A-Z0-9]{3}$";

    public static boolean isValid(String rfc) {
        return rfc != null && rfc.matches(RFC_REGEX);
    }
}

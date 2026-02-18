package com.chakray.usersapi.validation;

public class PhoneValidator {

    public static boolean isValid(String phone) {

        if (phone == null) return false;
        //Elimina caracteres no númericos
        String digits = phone.replaceAll("\\D", "");

        return digits.length() == 10;
    }
}

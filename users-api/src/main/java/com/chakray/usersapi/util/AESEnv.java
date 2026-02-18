package com.chakray.usersapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESEnv {

    private final String secretKey;

    public AESEnv(@Value("${app.aes.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String encrypt(String value) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(),
                    "AES"//Algoritmo a Usar
            );

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password");
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(),
                    "AES"
            );

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(encryptedValue);

            return new String(cipher.doFinal(decoded));

        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password");
        }
    }
}

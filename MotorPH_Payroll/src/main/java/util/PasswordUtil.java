/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Jae
 */
public class PasswordUtil {
    
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    public static String hashPassword(String password) {

        try {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            byte[] hash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

            spec.clearPassword();

            return "PBKDF2$"
                    + ITERATIONS + "$"
                    + Base64.getEncoder().encodeToString(salt) + "$"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unable to hash password.", e
            );
        }
    }

    public static boolean verifyPassword(
            String password,
            String storedPassword
    ) {

        try {
            String[] parts = storedPassword.split("\\$");

            if (parts.length != 4 || !parts[0].equals("PBKDF2")) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    iterations,
                    expectedHash.length * 8
            );

            byte[] actualHash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

            spec.clearPassword();

            if (actualHash.length != expectedHash.length) {
                return false;
            }

            int difference = 0;

            for (int i = 0; i < actualHash.length; i++) {
                difference |= actualHash[i] ^ expectedHash[i];
            }

            return difference == 0;

        } catch (GeneralSecurityException
                | IllegalArgumentException
                | NullPointerException e) {

            return false;
        }
    }
    
}

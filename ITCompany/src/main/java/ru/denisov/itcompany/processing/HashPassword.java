package ru.denisov.itcompany.processing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashPassword {
    private static final int SALT_LENGTH = 8;
    private static final Logger LOGGER = Logger.getLogger(HashPassword.class.getName());

    public static String hash(String password) {
        try {
            byte[] salt = generateSalt();
            byte[] hash = hashPassword(password, salt);

            String saltBase = Base64.getEncoder().encodeToString(salt);
            String hashBase = Base64.getEncoder().encodeToString(hash);

            return saltBase + ":" + hashBase;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при хешировании пароля: ", e.getMessage());

            return "";
        }
    }

    public static boolean verify(String password, String hash) throws NoSuchAlgorithmException {
        String[] parts = hash.split(":");

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedHash = Base64.getDecoder().decode(parts[1]);
        byte[] hashToVerify = hashPassword(password, salt);

        return MessageDigest.isEqual(storedHash, hashToVerify);
    }

    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[SALT_LENGTH];

        random.nextBytes(salt);

        return salt;
    }

    private static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        digest.reset();
        digest.update(salt);
        digest.update(password.getBytes());

        return digest.digest();
    }
}

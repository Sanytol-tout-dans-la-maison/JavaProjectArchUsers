package org.isep.javaprojectarchusers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
    public static String toHash(String stringToHAsh) throws NoSuchAlgorithmException {

        // Hashing
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(stringToHAsh.getBytes());

        // Convert bytes to hex
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
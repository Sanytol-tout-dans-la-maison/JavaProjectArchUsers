package org.isep.javaprojectarchusers.Encryption;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {

    private static byte[] iv = new byte[] {(byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67,
            (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,
            (byte)0x9a, (byte)0xbc, (byte)0xde, (byte)0xf0
    };

    private static byte[] keyBytes = new byte[] {
            (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
            (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
            (byte)0x88, (byte)0x99, (byte)0xaa, (byte)0xbb,
            (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff
    };

    private static int tagLength = 128;


    private static SecretKey key = new SecretKeySpec(keyBytes, "AES");

    private static GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(tagLength, iv);

    private static String algorithm = "AES/GCM/NoPadding";

    public static SecretKey getKey() {
        return key;
    }

    public static GCMParameterSpec getGcmParameterSpec() {
        return gcmParameterSpec;
    }

    public static void givenFile_whenEncrypt_thenSuccess()
            throws Exception {



        System.out.println(key.toString());

        File inputFile = new File("src/main/resources/org/isep/javaprojectarchusers/blockchain.json");
        File encryptedFile = new File("src/main/resources/org/isep/javaprojectarchusers/blockchaincrypted.json");
        File decryptedFile = new File("src/main/resources/org/isep/javaprojectarchusers/blockchaindecrypted.json");
        AESUtil.encryptFile( algorithm, key, gcmParameterSpec, inputFile.toString(), encryptedFile.toString());
        AESUtil.decryptFile(algorithm,  key, gcmParameterSpec, encryptedFile.toString(), decryptedFile.toString());
    }

    public static void encryptFile(SecretKey key, String inputFile, String outputFile) throws Exception {
        AESUtil.encryptFile(algorithm,key,gcmParameterSpec, inputFile, outputFile);
    }
    public static void decryptFile(SecretKey key, String inputFile, String outputFile) throws Exception{
        AESUtil.decryptFile(algorithm,key,gcmParameterSpec, inputFile, outputFile);
    }

    public static void encryptAllFiles(SecretKey key) throws Exception {
        encryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/blockchain.json", "src/main/resources/org/isep/javaprojectarchusers/encryptedBlockchain.json");
        encryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/portfolios.json","src/main/resources/org/isep/javaprojectarchusers/encryptedPortfolios.json");
        encryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/events.json","src/main/resources/org/isep/javaprojectarchusers/encryptedEvents.json");
        encryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/savingAccounts.json","src/main/resources/org/isep/javaprojectarchusers/encryptedSavingAccounts.json");
        encryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/checkingAccounts.json", "src/main/resources/org/isep/javaprojectarchusers/encryptedCheckingAccounts.json");
        File blockchainFile = new File("src/main/resources/org/isep/javaprojectarchusers/blockchain.json");
        blockchainFile.delete();
        File portfolioFile = new File("src/main/resources/org/isep/javaprojectarchusers/portfolios.json");
        portfolioFile.delete();
        File eventsFile = new File("src/main/resources/org/isep/javaprojectarchusers/events.json");
        eventsFile.delete();
        File savingAccountsFile = new File("src/main/resources/org/isep/javaprojectarchusers/savingAccounts.json");
        savingAccountsFile.delete();
        File checkingAccountsFile = new File("src/main/resources/org/isep/javaprojectarchusers/checkingAccounts.json");
        checkingAccountsFile.delete();
    }

    public static void decryptAllFiles(SecretKey key) throws Exception {
        decryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/encryptedBlockchain.json","src/main/resources/org/isep/javaprojectarchusers/blockchain.json");
        decryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/encryptedPortfolios.json","src/main/resources/org/isep/javaprojectarchusers/portfolios.json");
        decryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/encryptedEvents.json","src/main/resources/org/isep/javaprojectarchusers/events.json");
        decryptFile(key,"src/main/resources/org/isep/javaprojectarchusers/encryptedSavingAccounts.json","src/main/resources/org/isep/javaprojectarchusers/savingAccounts.json");
        decryptFile(key, "src/main/resources/org/isep/javaprojectarchusers/encryptedCheckingAccounts.json", "src/main/resources/org/isep/javaprojectarchusers/checkingAccounts.json");
        File blockchainFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedBlockchain.json");
        blockchainFile.delete();
        File portfolioFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedPortfolios.json");
        portfolioFile.delete();
        File eventsFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedEvents.json");
        eventsFile.delete();
        File savingAccountsFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedSavingAccounts.json");
        savingAccountsFile.delete();
        File checkingAccountsFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedCheckingAccounts.json");
        checkingAccountsFile.delete();
    }

    public static String keyToString(SecretKey keyToEncode){
        return Base64.getEncoder().encodeToString(keyToEncode.getEncoded());
    }

    public static SecretKey stringToKey(String encodedKey) {
        for(;encodedKey.length() < 22;) encodedKey = encodedKey.concat("0");
        encodedKey = encodedKey.substring(0, 21).concat("0");
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static String encryptString(String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decryptString(String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }
}

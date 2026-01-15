package org.isep.javaprojectarchusers;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    public static void encryptFile(String inputFile, String outputFile) throws Exception {
        AESUtil.encryptFile(algorithm,key,gcmParameterSpec, inputFile, outputFile);
    }
    public static void decryptFile(String inputFile, String outputFile) throws Exception{
        AESUtil.decryptFile(algorithm,key,gcmParameterSpec, inputFile, outputFile);
    }

    public static void encryptAllFiles() throws Exception {
        encryptFile("src/main/resources/org/isep/javaprojectarchusers/blockchain.json", "src/main/resources/org/isep/javaprojectarchusers/encryptedBlockchain.json");
        encryptFile("src/main/resources/org/isep/javaprojectarchusers/portfolios.json","src/main/resources/org/isep/javaprojectarchusers/encryptedPortfolios.json");
        encryptFile("src/main/resources/org/isep/javaprojectarchusers/events.json","src/main/resources/org/isep/javaprojectarchusers/encryptedEvents.json");
        File blockchainFile = new File("src/main/resources/org/isep/javaprojectarchusers/blockchain.json");
        blockchainFile.delete();
        File portfolioFile = new File("src/main/resources/org/isep/javaprojectarchusers/portfolios.json");
        portfolioFile.delete();
        File eventsFile = new File("src/main/resources/org/isep/javaprojectarchusers/events.json");
        eventsFile.delete();
    }

    public static void decryptAllFiles() throws Exception {
        decryptFile("src/main/resources/org/isep/javaprojectarchusers/encryptedBlockchain.json","src/main/resources/org/isep/javaprojectarchusers/blockchain.json");
        decryptFile("src/main/resources/org/isep/javaprojectarchusers/encryptedPortfolios.json","src/main/resources/org/isep/javaprojectarchusers/portfolios.json");
        decryptFile("src/main/resources/org/isep/javaprojectarchusers/encryptedEvents.json","src/main/resources/org/isep/javaprojectarchusers/events.json");
        File blockchainFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedBlockchain.json");
        blockchainFile.delete();
        File portfolioFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedPortfolios.json");
        portfolioFile.delete();
        File eventsFile = new File("src/main/resources/org/isep/javaprojectarchusers/encryptedEvents.json");
        eventsFile.delete();
    }
}

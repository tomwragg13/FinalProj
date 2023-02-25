package com.example.finalproj;

public class Encrypt {

    // Creates Encryption Instance Variables
    static int encryptionKey = 0;

    public static String encryptData(String plaintextString){
        String container = "";

        char[] chars = plaintextString.toCharArray();

        for(char character : chars) {
            character += encryptionKey;
            container += character;
        }
        return container;
    }

    public static String decryptData(String encryptedString){
        String container = "";

        char[] chars = encryptedString.toCharArray();

        for(char character : chars){
            character -= encryptionKey;
            container += character;
        }
        return container;
    }
}

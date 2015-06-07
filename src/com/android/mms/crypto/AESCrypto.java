package com.android.mms.crypto;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.mms.crypto_models.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {
    static final String TAG = "SymmetricAlgorithmAES";

    //returns randomly generated aes key as String
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String generateAESKey() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(
                "password".toCharArray(),
                generateSalt().getBytes("UTF-8"),
                65536,
                256
            );

            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
        } catch(Exception e) {
            Log.e(TAG, "AES encryption error: " + e.toString());
            return null;
        }
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return new String(bytes);
    }

    /*
    * encrypts and encodes given string
    * using given pair's session_key
    * returns encoded message
    */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String encrypt(Pair pair, String message) {
        String encoded = null;
        byte[] encryptedBytes = null;
        try {
            byte[] decodedSessionKey = Base64.decode(pair.sessionKey, Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedSessionKey, "AES");

            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            encryptedBytes = c.doFinal(message.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error: " + e.toString());
        }
        encoded = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        Log.d(TAG, "Encrypted is " + encoded);
        return encoded;
    }

    /*
    * encrypts given data
    * using given pair's session_key
    * returns encrypted data
    */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static byte[] encrypt(Pair pair, byte[] data) {
        byte[] encryptedBytes = null;
        try {
            byte[] decodedSessionKey = Base64.decode(pair.sessionKey, Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedSessionKey, "AES");

            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            encryptedBytes = c.doFinal(data);
            Log.d(TAG, "Encrypted is " + new String(encryptedBytes));
            return encryptedBytes;
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error: " + e.toString());
            return null;
        }
    }

    /*
    * decodes and decrypts given encrypted string
    * using given pair's session_key
    * returns original message
    */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String decrypt(Pair pair, String encoded) {
        Log.d(TAG, "Try to decrypt " + encoded);
        byte[] decryptedBytes = null;
        try {
            byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);

            byte[] decodedSessionKey = Base64.decode(pair.sessionKey, Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedSessionKey, "AES");

            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, secretKeySpec);

            decryptedBytes = c.doFinal(decoded);
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error: " + e.toString());
        }
        return new String(decryptedBytes);
    }

    /*
    * decrypts given data
    * using given pair's session_key
    * returns decrypted data
    */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static byte[] decrypt(Pair pair, byte[] data) {
        byte[] decryptedBytes = null;
        try {
            byte[] decodedSessionKey = Base64.decode(pair.sessionKey, Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedSessionKey, "AES");

            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, secretKeySpec);

            decryptedBytes = c.doFinal(data);
            Log.d(TAG, "decrypted is " + new String(decryptedBytes));
            return decryptedBytes;
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error: " + e.toString());
            return null;
        }
    }

}
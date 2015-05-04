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
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {
    static final String TAG = "SymmetricAlgorithmAES";

    //returns randomly generated aes key as String
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String generateAESKey() {
        SecretKeySpec sks = null;
        byte[] sksbyte;
        String sessionKey;

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
            sksbyte = sks.getEncoded();
            sessionKey = Base64.encodeToString(sksbyte, Base64.DEFAULT);
            return sessionKey;
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }
        return  null;
    }

    /*
    * encrypts and encodes given string
    * using given pair's session_key
    * returns encoded message
    */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String encrypt(Pair pair, String message) {
        String encoded;
        byte[] encryptedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            SecretKeySpec sks = new SecretKeySpec(pair.sessionKey.getBytes(), "AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encryptedBytes = c.doFinal(message.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }
        encoded = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        return encoded;
    }

    /*
    * decodes and decrypts given encrypted string
    * using given pair's session_key
    * returns original message
    */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String decrypt(Pair pair, String encoded) {
        byte[] decryptedBytes = null;
        try {
            byte[] decoded = Base64.decode(encoded,Base64.DEFAULT);
            Cipher c = Cipher.getInstance("AES");
            SecretKeySpec sks = new SecretKeySpec(pair.sessionKey.getBytes(), "AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decryptedBytes = c.doFinal(decoded);
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error");
        }
        return decryptedBytes.toString();
    }

}
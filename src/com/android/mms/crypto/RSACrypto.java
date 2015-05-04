package com.android.mms.crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;
import java.math.BigInteger;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import com.android.mms.crypto_models.Pair;
import javax.crypto.Cipher;
import android.util.Base64;

public class RSACrypto {

  private static final String PRIVATE_FILE_NAME = "/private_key";
  private static final String PUBLIC_FILE_NAME = "/public_key";
  private static final String PHONE_NUMBER_FILE_NAME = "/phone_number";
  private Context context;
  private RSAPublicKeySpec publicKeySpec;
  private RSAPrivateKeySpec privateKeySpec;

  public RSACrypto(Context context) {
    this.context = context;
    checkFiles();
  }

  public void checkFiles() {
    String privateFilePath = context.getFilesDir().getPath().toString() + PRIVATE_FILE_NAME;
    String publicFilePath = context.getFilesDir().getPath().toString() + PUBLIC_FILE_NAME;
    File privateFile = new File(privateFilePath);
    File publicFile = new File(publicFilePath);
    String str = "";
    if(privateFile.exists()){
      str = "...Private Key File Exists...";
    } else {
      try {
        generateKeys();
        privateFile.createNewFile();
        ObjectOutputStream oout = new ObjectOutputStream(
          new BufferedOutputStream(new FileOutputStream(privateFile.getAbsoluteFile())));

        oout.writeObject(privateKeySpec.getModulus());
        oout.writeObject(privateKeySpec.getPrivateExponent());
        oout.close();

        publicFile.createNewFile();
        oout = new ObjectOutputStream(
          new BufferedOutputStream(new FileOutputStream(publicFile.getAbsoluteFile())));

        oout.writeObject(publicKeySpec.getModulus());
        oout.writeObject(publicKeySpec.getPublicExponent());
        oout.close();
      } catch (Exception e) {
        Log.e("File", e.getMessage());
      }
      str = "!!!Private File Doesn't Exist!!!";
    }
    Log.i("File", str);
  }

  private void generateKeys() {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
      kpg.initialize(790);
      KeyPair kp = kpg.genKeyPair();
      Key publicKey = kp.getPublic();
      Key privateKey = kp.getPrivate();

      KeyFactory fact = KeyFactory.getInstance("RSA");
      publicKeySpec = (RSAPublicKeySpec) fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
      privateKeySpec = (RSAPrivateKeySpec) fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);
    } catch(Exception ex) {
      
    }
  }

  private BigInteger[] readKeyFile(String fileName) {
    try {
      File file = new File(context.getFilesDir().getPath().toString() + fileName);
      ObjectInputStream oin = 
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(file.getAbsoluteFile())));

      BigInteger modulus = (BigInteger) oin.readObject();
      BigInteger exponent = (BigInteger) oin.readObject();
      oin.close();
      BigInteger[] returnData = {modulus, exponent};
      return returnData;
    } catch(Exception ex) {
      return null;
    }
  }

  public RSAPrivateKeySpec getPrivateKeySpec() {
    if(privateKeySpec == null) {
      BigInteger[] modExp = readKeyFile(PRIVATE_FILE_NAME);
      privateKeySpec = new RSAPrivateKeySpec(modExp[0], modExp[1]);
    }
    return privateKeySpec;
  } 

  public RSAPublicKeySpec getPublicKeySpec() {
    if(publicKeySpec == null) {
      BigInteger[] modExp = readKeyFile(PUBLIC_FILE_NAME);
      publicKeySpec = new RSAPublicKeySpec(modExp[0], modExp[1]);
    }
    return publicKeySpec;
  }

  public String decrypt(String string) {
    RSAPrivateKeySpec keySpec = getPrivateKeySpec();
    try {
      KeyFactory fact = KeyFactory.getInstance("RSA");
      java.security.PrivateKey privateKey = fact.generatePrivate(keySpec);

      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return new String(cipher.doFinal(Base64.decode(string, Base64.DEFAULT)));
    } catch (Exception ex) {
      return null;
    }
  }

  public static String encryptSessionKey(Pair pair) {
    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(pair.publicKeyModulus), new BigInteger(pair.publicKeyExponent));
    try {
      KeyFactory fact = KeyFactory.getInstance("RSA");
      PublicKey pubKey = fact.generatePublic(keySpec);

      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      byte[] cipherData = cipher.doFinal(pair.sessionKey.getBytes());
      return Base64.encodeToString(cipherData, Base64.DEFAULT);
    } catch (Exception ex) {
      return null;
    }
  }

}
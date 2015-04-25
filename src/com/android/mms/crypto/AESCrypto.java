package com.android.mms.crypto;

import com.android.mms.crypto_models.Pair;

public class AESCrypto {

  //returns randomly generated aes key as String
  public String generateAESKey() {
    return null;
  }

  /*
  * encrypts and encodes given string 
  * using given pair's session_key 
  * returns encoded message
  */
  public String encrypt(Pair pair, String message) {
    return null;
  }

  /*
  * decodes and decrypts given encrypted string 
  * using given pair's session_key
  * returns original message
  */
  public String decrypt(Pair pair, String message) {
    return null;
  }

}
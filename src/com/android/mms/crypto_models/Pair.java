package com.android.mms.crypto_models;

public class Pair {
  public int id;
  public String phoneNumber;
  public String publicKeyExponent;
  public String publicKeyModulus;
  public String sessionKey;

  public Pair(int id, String phoneNumber, String publicKeyExponent, String publicKeyModulus, String sessionKey) {
    this.id = id;
    this.phoneNumber = phoneNumber;
    this.publicKeyExponent = publicKeyExponent;
    this.publicKeyModulus = publicKeyModulus;
    this.sessionKey = sessionKey;
  }

  public Pair(String phoneNumber, String publicKeyExponent, String publicKeyModulus) {
    this.phoneNumber = phoneNumber;
    this.publicKeyExponent = publicKeyExponent;
    this.publicKeyModulus = publicKeyModulus;
  }
}
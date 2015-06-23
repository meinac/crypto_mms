package com.android.mms.crypto_models;

public class Pair {
  public int id;
  public String phoneNumber;
  public String publicKeyExponent;
  public String publicKeyModulus;
  public String sessionKey;
  public int sessionLife;

  public Pair(int id, String phoneNumber, String publicKeyExponent, String publicKeyModulus, String sessionKey, int sessionLife) {
    this.id = id;
    this.phoneNumber = phoneNumber;
    this.publicKeyExponent = publicKeyExponent;
    this.publicKeyModulus = publicKeyModulus;
    this.sessionKey = sessionKey;
    this.sessionLife = sessionLife;
  }

  public Pair(String phoneNumber, String publicKeyExponent, String publicKeyModulus) {
    this.phoneNumber = phoneNumber;
    this.publicKeyExponent = publicKeyExponent;
    this.publicKeyModulus = publicKeyModulus;
    this.sessionLife = 0;
  }
}
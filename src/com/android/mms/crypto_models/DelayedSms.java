package com.android.mms.crypto_models;

public class DelayedSms {
  public int id;
  public String to;
  public String message;

  public DelayedSms(int id, String to, String message) {
    this.id = id;
    this.to = to;
    this.message = message;
  }

  public DelayedSms(String to, String message) {
    this.to = to;
    this.message = message;
  }
}
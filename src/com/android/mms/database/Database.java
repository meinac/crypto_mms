package com.android.mms.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.Context;

public class Database extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 2;
  private static final String DATABASE_NAME = "csms";
  private static final String PAIR_TABLE_NAME = "pairs";
  private static final String SMS_TABLE_NAME = "delayed_sms";
  private static final String KEY_ID = "id";
  private static final String KEY_PHONE = "phone_number";
  private static final String KEY_PUBLIC_KEY_EXPONENT = "public_key_exponent";
  private static final String KEY_PUBLIC_KEY_MODULUS = "public_key_modulus";
  private static final String KEY_SESSION_KEY = "session_key";
  private static final String KEY_TO = "to_number";
  private static final String KEY_MESSAGE = "message";

  public Database(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    Log.d("CRYPTOMMS", "Creating tables");
    db.execSQL("CREATE TABLE " + PAIR_TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_PHONE + " TEXT, " + KEY_PUBLIC_KEY_EXPONENT + " TEXT, " + KEY_PUBLIC_KEY_MODULUS + " TEXT, " + KEY_SESSION_KEY + " TEXT )");
    db.execSQL("CREATE TABLE " + SMS_TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TO + " TEXT, " + KEY_MESSAGE + " TEXT )");

  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + PAIR_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + SMS_TABLE_NAME);
    onCreate(db);
  }

}
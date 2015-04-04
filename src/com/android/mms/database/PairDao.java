package com.android.mms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.android.mms.crypto_models.Pair;

public class PairDao extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "csms";
  private static final String TABLE_NAME = "pairs";
  private static final String KEY_ID = "id";
  private static final String KEY_PHONE = "phone_number";
  private static final String KEY_PUBLIC_KEY_EXPONENT = "public_key_exponent";
  private static final String KEY_PUBLIC_KEY_MODULUS = "public_key_modulus";
  private static final String KEY_SESSION_KEY = "session_key";

  public PairDao(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_PHONE + " TEXT, " + KEY_PUBLIC_KEY_EXPONENT + " TEXT, " + KEY_PUBLIC_KEY_MODULUS + " TEXT, " + KEY_SESSION_KEY + " TEXT )");
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }

  public void createPair(Pair pair) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_PHONE, pair.phoneNumber);
    values.put(KEY_PUBLIC_KEY_EXPONENT, pair.publicKeyExponent);
    values.put(KEY_PUBLIC_KEY_MODULUS, pair.publicKeyModulus);
    values.put(KEY_SESSION_KEY, pair.sessionKey);

    db.insert(TABLE_NAME, null, values);
    db.close();
  }

}
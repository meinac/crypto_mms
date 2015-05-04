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

  public void create(Pair pair) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_PHONE, pair.phoneNumber);
    values.put(KEY_PUBLIC_KEY_EXPONENT, pair.publicKeyExponent);
    values.put(KEY_PUBLIC_KEY_MODULUS, pair.publicKeyModulus);
    values.put(KEY_SESSION_KEY, pair.sessionKey);

    db.insert(TABLE_NAME, null, values);
    db.close();
  }

  public Pair getByPhoneNumber(String phoneNumber) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_NAME, 
      new String[] { KEY_ID, KEY_PHONE, KEY_PUBLIC_KEY_EXPONENT, KEY_PUBLIC_KEY_MODULUS, KEY_SESSION_KEY }, 
      KEY_PHONE + "=?", 
      new String[] { String.valueOf(phoneNumber) }, 
      null, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();
      Pair pair = new Pair(Integer.parseInt(cursor.getString(0)),
        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
      return pair;
    }
    return null;
  }

  public int update(Pair pair) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_PHONE, pair.phoneNumber);
    values.put(KEY_PUBLIC_KEY_EXPONENT, pair.publicKeyExponent);
    values.put(KEY_PUBLIC_KEY_MODULUS, pair.publicKeyModulus);
    values.put(KEY_SESSION_KEY, pair.sessionKey);

    return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(pair.id) });
  }

  public void delete(Pair pair) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(pair.id) });
    db.close();
  }

}
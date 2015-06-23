package com.android.mms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.android.mms.crypto_models.Pair;
import com.android.mms.database.Database;

public class PairDao {

  private static final String TABLE_NAME = "pairs";
  private static final String KEY_ID = "id";
  private static final String KEY_PHONE = "phone_number";
  private static final String KEY_PUBLIC_KEY_EXPONENT = "public_key_exponent";
  private static final String KEY_PUBLIC_KEY_MODULUS = "public_key_modulus";
  private static final String KEY_SESSION_KEY = "session_key";
  private static final String KEY_SESSION_LIFE = "session_life";
  private Database dbHelper;

  public PairDao(Context context) {
    dbHelper = new Database(context);
  }

  public void create(Pair pair) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_PHONE, pair.phoneNumber);
    values.put(KEY_PUBLIC_KEY_EXPONENT, pair.publicKeyExponent);
    values.put(KEY_PUBLIC_KEY_MODULUS, pair.publicKeyModulus);
    values.put(KEY_SESSION_KEY, pair.sessionKey);
    values.put(KEY_SESSION_LIFE, pair.sessionLife);

    db.insert(TABLE_NAME, null, values);
    db.close();
  }

  public Pair getByPhoneNumber(String phoneNumber) {
    SQLiteDatabase db = dbHelper.getReadableDatabase();

    Cursor cursor = db.query(TABLE_NAME, 
      new String[] { KEY_ID, KEY_PHONE, KEY_PUBLIC_KEY_EXPONENT, KEY_PUBLIC_KEY_MODULUS, KEY_SESSION_KEY, KEY_SESSION_LIFE },
      KEY_PHONE + "=?", 
      new String[] { String.valueOf(phoneNumber) }, 
      null, null, null, null);

    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      Pair pair = new Pair(Integer.parseInt(cursor.getString(0)),
        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
        Integer.parseInt(cursor.getString(5)));
      return pair;
    }
    return null;
  }

  public int update(Pair pair) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_PHONE, pair.phoneNumber);
    values.put(KEY_PUBLIC_KEY_EXPONENT, pair.publicKeyExponent);
    values.put(KEY_PUBLIC_KEY_MODULUS, pair.publicKeyModulus);
    values.put(KEY_SESSION_KEY, pair.sessionKey);
    values.put(KEY_SESSION_LIFE, pair.sessionLife);

    return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(pair.id) });
  }

  public void delete(Pair pair) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(pair.id) });
    db.close();
  }

}
package com.android.mms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.android.mms.crypto_models.DelayedSms;

public class DelayedSmsDao extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "csms";
  private static final String TABLE_NAME = "delayed_smses";
  private static final String KEY_ID = "id";
  private static final String KEY_TO = "to";
  private static final String KEY_MESSAGE = "message";

  public DelayedSmsDao(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TO + " TEXT)");
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }

  public void create(DelayedSms sms) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_TO, sms.to);
    values.put(KEY_MESSAGE, sms.message);

    db.insert(TABLE_NAME, null, values);
    db.close();
  }

  public DelayedSms getByDestinationNumber(String to) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_NAME, 
      new String[] { KEY_ID, KEY_TO, KEY_MESSAGE }, 
      KEY_TO + "=?", 
      new String[] { String.valueOf(to) }, 
      null, null, null, null);

    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      DelayedSms sms = new DelayedSms(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
      return sms;
    }
    return null;
  }

  public void delete(DelayedSms sms) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(sms.id) });
    db.close();
  }

}
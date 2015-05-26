package com.android.mms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.android.mms.crypto_models.DelayedSms;
import com.android.mms.database.Database;

public class DelayedSmsDao {

  private static final String TABLE_NAME = "delayed_sms";
  private static final String KEY_ID = "id";
  private static final String KEY_TO = "to_number";
  private static final String KEY_MESSAGE = "message";
  private Database dbHelper;

  public DelayedSmsDao(Context context) {
    dbHelper = new Database(context);
  }

  public void create(DelayedSms sms) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_TO, sms.to);
    values.put(KEY_MESSAGE, sms.message);

    db.insert(TABLE_NAME, null, values);
    db.close();
  }

  public DelayedSms getByDestinationNumber(String to) {
    SQLiteDatabase db = dbHelper.getReadableDatabase();

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
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(sms.id) });
    db.close();
  }

}
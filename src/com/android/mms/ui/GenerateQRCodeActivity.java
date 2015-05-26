package com.android.mms.ui;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.mms.R;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.preference.PreferenceManager;
import com.android.mms.crypto.RSACrypto;

public class GenerateQRCodeActivity extends Activity {

  private String phoneNumber;
  private RSACrypto rsaCrypto;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    phoneNumber = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_manage_phone_number", null).trim();
    if(phoneNumber.length() == 11) {
      phoneNumber = "+9" + phoneNumber;
    }
    rsaCrypto = new RSACrypto(this);
    setContentView(R.layout.activity_generate_qr_code);
    if(phoneNumber != null) {
      generateQRCode();
    } else {
      Toast.makeText(getApplicationContext(), R.string.phone_number_should_be_setted, Toast.LENGTH_LONG).show();
      finish();
    }
  }

  public void generateQRCode() {
    try {
      BitMatrix bitMatrix = new QRCodeWriter().encode("{\"phone_number\": \"" + phoneNumber + "\", \"exponent\": " + rsaCrypto.getPublicKeySpec().getPublicExponent().toString() + ", \"modulus\": \"" + rsaCrypto.getPublicKeySpec().getModulus().toString() + "\"}", BarcodeFormat.QR_CODE, 800, 800);

      Bitmap imageBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);

      for (int i = 0; i < 800; i++) {//width
        for (int j = 0; j < 800; j++) {//height
          imageBitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK: Color.WHITE);
        }
      }

      if (imageBitmap != null) {
        ImageView qrCode = (ImageView)findViewById(R.id.qrCode);
        qrCode.setImageBitmap(imageBitmap);
        qrCode.setMinimumHeight(800);
        qrCode.setMinimumWidth(800);
      } else {
        Toast.makeText(getApplicationContext(), "Couldn't Create QR Image", Toast.LENGTH_SHORT).show();
      }
    } catch (WriterException e) {
      e.printStackTrace();
    }
  }

}
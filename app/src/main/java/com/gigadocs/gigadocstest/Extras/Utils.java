package com.gigadocs.gigadocstest.Extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String imageToBase64(ImageView iv){
        iv.buildDrawingCache();
        Bitmap bitmap = iv.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        System.out.println("byte array:"+ Arrays.toString(image));
        return Base64.encodeToString(image, 0);
    }

    public static Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static String getCurrentTimeMillis(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return dateFormat.format(new Date()); // Find todays date
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTimeFormat(){
        return "yyyy-MM-dd HH:mm:ss";
    }

    public static boolean isValidEmail(TextInputEditText target,Context context) {
        if (!TextUtils.isEmpty(target.getText()) && Patterns.EMAIL_ADDRESS.matcher(target.getText().toString()).matches())
            return true;
        else{
            Toast.makeText(context, "Not a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean isEmpty(TextInputEditText[] editTexts, Context context) {
        boolean flag=false;
        for (TextInputEditText target:editTexts){
            if (!TextUtils.isEmpty(target.getText())){
                Toast.makeText(context, target.getHint().toString(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return flag;
    }
}

package com.tej.note_winmachines_android.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Helper {
    public static byte[] ImageToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        return  stream.toByteArray();
    }
    public  static  Bitmap ByteToImage(byte [] array){
        return BitmapFactory.decodeByteArray(array,0,array.length);
    }
    public  static  String ConvertDateToString(Date date){
        return DateFormat.format("MM/dd/yyyy", date).toString();
    }


}

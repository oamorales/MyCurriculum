package com.oamorales.myresume.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditImage extends AppCompatActivity {

    /** Empty image file instance */
    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        assert storageDir != null;
        String imageFileName = storageDir.getAbsolutePath() + File.separator + "img_" + timeStamp + "_" + ".jpg";
        return new File(imageFileName);
    }

    public static String storeFile(Context context, Uri selectedImgUri, ContentResolver contentResolver){
        try {
            /** New file name */
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            assert storageDir != null;
            String imageFileName = storageDir.getAbsolutePath() + File.separator + "img_" + timeStamp + "_" + ".jpg";
            /** le pasas al bitmap la uri de la imagen seleccionada */
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImgUri);
            // pones las medidas que quieras del nuevo .png
            int bitmapWidth = bitmap.getWidth()/2; // para utilizar width de la imagen original: bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight()/2; // para utilizar height de la imagen original: bitmap.getHeight();
            Bitmap bitmapout = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false);
            //creas el nuevo png en la nueva ruta
            bitmapout.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(imageFileName));
            Toast.makeText(context, "IMAGE SAVED", Toast.LENGTH_SHORT).show();
            return imageFileName;
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public static void discardImages(List<String> images){
        File file;
        for (String path: images) {
            file = new File(path);
            file.delete();
        }
    }
}

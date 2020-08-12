package com.oamorales.myresume.utils;

import android.content.Context;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditImage extends AppCompatActivity {


    /** Empty image file instance */
    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        assert storageDir != null;
        String imageFileName = storageDir.getAbsolutePath() + File.separator + "img_" + timeStamp + "_" + ".jpg";
        return new File(imageFileName);
    }

}

package ua.com.mcsim.photogallery.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mcsim on 13.11.2017.
 * Class with some utils
 */

public class Utils {
    private Context context;
    public boolean isNoImages = false;

    //Constructor
    public Utils(Context context) {
        this.context = context;
    }

    // Reading file paths
    public ArrayList<String> getFilePaths() {
        ArrayList<String> filePaths = new ArrayList<String>();
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + File.separator + Constant.PHOTO_ALBUM);

        if (directory.isDirectory()) {
            File[] listFiles = directory.listFiles();

            if (listFiles != null) {
                for (int i=0; i<listFiles.length; i++) {
                    String filePath = listFiles[i].getAbsolutePath();
                    Log.d("mLog",filePath);

                    if (isSupportedFile(filePath)) {
                        filePaths.add(filePath);
                    }
                }
            } else {
                Toast.makeText(context, "Directory " + Constant.PHOTO_ALBUM + " is empty.", Toast.LENGTH_LONG).show();
                isNoImages = true;
            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Error!");
            alert.setMessage(Constant.PHOTO_ALBUM + " directory path is not valid! Please, set the image directory name AppConstant.java class");
            alert.setPositiveButton("Ok", null);
            alert.show();
        }

        return filePaths;
    }

    private boolean isSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());

        if (Constant.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;

        return columnWidth;
    }
}
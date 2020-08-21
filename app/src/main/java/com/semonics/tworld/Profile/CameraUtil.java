package com.semonics.tworld.Profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import com.semonics.tworld.SimpleClasses.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("JavaDoc")
public class CameraUtil {     /*directory name to store captured images*/
    private static final String IMAGE_DIRECTORY_NAME = "TWorld";
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;
    private static final String IMG_PREFIX = "IMG_";
    private static final String IMG_POSTFIX = ".jpg";
    private static final String VIDEO_PREFIX = "VIDEO_";
    private static final String VIDEO_POSTFIX = ".mp4";

    /**
     * returning image
     */
    public static File getOutputMediaFile(int type) {         /*External sdcard location*/
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);         /*Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Utils.showLog(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
            }
        }         /*Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + IMG_PREFIX + timeStamp + IMG_POSTFIX);
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + VIDEO_PREFIX + timeStamp + VIDEO_POSTFIX);
        } else {
            return null;
        }
        return mediaFile;
    }

    /**
     * This method is use for check ExIfInof of image.      *      * @param mediaFile
     **/
    public static int checkExIfInfo(String mediaFile) {
        final ExifInterface exif;
        int rotation = 0;
        try {
            exif = new ExifInterface(mediaFile);
            final String exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            switch (exifOrientation) {
                case "6":
                    rotation = 90;/*Rotation angle*/
                    break;
                case "1":
                    rotation = 0;/*Rotation angle*/
                    break;
                case "8":
                    rotation = 270;/*Rotation angle*/
                    break;
                case "3":
                    rotation = 180;/*Rotation angle*/
                    break;
                case "0":
                    rotation = 0;/*Rotation angle*/
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotation;
    }

    /**
     * This method is use for rotation of image.      *      * @param mediaFile
     **/
    public static void rotateImage(String mediaFile, int rotation) {
        if (rotation != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(mediaFile, options);
            if (bitmap != null) {
                Matrix matrix = new Matrix();
                matrix.setRotate(rotation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                try {
                    final FileOutputStream fos = new FileOutputStream(mediaFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}


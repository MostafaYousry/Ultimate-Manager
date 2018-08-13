package com.example.android.employeesmanagementapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public final class ImageUtils {

    public static final int REQUEST_IMAGE_GET = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;

    public static String sCameraImageURI;

    public static void showPhotoCameraDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(new CharSequence[]{"Load Picture", "Take a Photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    pickPhoto(context);
                } else if (i == 1) {
                    dispatchTakePictureIntent(context);
                }
                dialogInterface.dismiss();
            }
        }).show();
    }

    private static void pickPhoto(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    private static void dispatchTakePictureIntent(Context context) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(context);
            } catch (IOException ex) {
                System.out.println("error occurred");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.example.android.employeesmanagementapp.fileprovider",
                        photoFile);
                sCameraImageURI = photoURI.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                ((AppCompatActivity) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private static File createImageFile(Context context) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,          /* prefix */
                ".jpg",          /* suffix */
                storageDir            /* directory */
        );


        return image;
    }
}

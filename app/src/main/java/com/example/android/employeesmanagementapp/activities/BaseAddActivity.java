package com.example.android.employeesmanagementapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.example.android.employeesmanagementapp.fragments.DatePickerDialogFragment;
import com.example.android.employeesmanagementapp.fragments.TimePickerFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

public abstract class BaseAddActivity extends AppCompatActivity {

    protected final int REQUEST_IMAGE_GET = 1;
    protected final int REQUEST_IMAGE_CAPTURE = 2;

    protected String cameraImageUri;

    protected abstract boolean isDataValid();

    protected abstract void updateErrorVisibility(String key, boolean show);

    protected abstract void save();

    protected abstract boolean fieldsChanged();

    protected abstract void showDiscardChangesDialog();

    @Override
    public void onBackPressed() {
        if (fieldsChanged()) {
            showDiscardChangesDialog();
        } else super.onBackPressed();
    }

    protected void showDatePicker(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerDialogFragment.KEY_DISPLAY_VIEW_ID, view.getId());

        DialogFragment datePickerFragment = new DatePickerDialogFragment();
        datePickerFragment.setArguments(bundle);

        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    protected void showTimePicker(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(TimePickerFragment.KEY_DISPLAY_VIEW_ID, view.getId());

        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);

        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }


    protected void showPhotoCameraDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]{"Load Picture", "Take a Photo"}, (dialogInterface, i) -> {
            if (i == 0) {
                pickPhoto();
            } else if (i == 1) {
                dispatchTakePictureIntent();
            }
            dialogInterface.dismiss();
        }).show();
    }

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("error occurred");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.employeesmanagementapp.fileprovider",
                        photoFile);
                cameraImageUri = photoURI.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,          /* prefix */
                ".jpg",          /* suffix */
                storageDir            /* directory */
        );
    }

}

package com.example.android.employeesmanagementapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.fragments.DatePickerDialogFragment;
import com.example.android.employeesmanagementapp.fragments.TimePickerDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

/**
 * abstract activity that defines some methods that will be used
 * in AddTaskActivity,AddEmployeeActivity,AddDepartmentActivity
 */
public abstract class BaseAddActivity extends AppCompatActivity {

    protected final int REQUEST_IMAGE_GET = 1;
    protected final int REQUEST_IMAGE_CAPTURE = 2;

    protected String cameraImageUri;

    protected abstract boolean isDataValid();

    protected abstract void updateErrorVisibility(String key, boolean show);

    protected abstract void save();

    protected abstract boolean fieldsChanged();

    /**
     * dialog that is shown when user has unsaved changes for a task/department/employee
     */
    protected void showDiscardChangesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_discard_changes));
        builder.setMessage(getString(R.string.dialog_message_discard));
        builder.setNegativeButton(getString(R.string.dialog_negative_btn_discard), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            finish();
        });

        builder.setPositiveButton(getString(R.string.dialog_positive_btn_save), (dialogInterface, i) -> {
            save();
            dialogInterface.dismiss();
        });
        builder.show();
    }

    /**
     * overrides activity on back pressed to check first if there is unsaved data
     * and to show discard changes dialog
     */
    @Override
    public void onBackPressed() {
        if (fieldsChanged()) {
            showDiscardChangesDialog();
        } else super.onBackPressed();
    }

    /**
     * shows date picker dialog to choose date
     *
     * @param view
     */
    protected void showDatePicker(View view) {
        Bundle bundle = new Bundle();

        //send the clicked view id to display date in after selecting one
        bundle.putInt(DatePickerDialogFragment.KEY_DISPLAY_VIEW_ID, view.getId());

        DialogFragment datePickerFragment = new DatePickerDialogFragment();
        datePickerFragment.setArguments(bundle);

        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * shows time picker dialog to choose time
     *
     * @param view
     */
    protected void showTimePicker(View view) {
        Bundle bundle = new Bundle();

        //send the clicked view id to display time in after selecting one
        bundle.putInt(TimePickerDialogFragment.KEY_DISPLAY_VIEW_ID, view.getId());

        DialogFragment timePickerFragment = new TimePickerDialogFragment();
        timePickerFragment.setArguments(bundle);

        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * shows dialog to choose weather to load image from device storage or to take a picture with the camera
     */
    protected void showPhotoCameraDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]{getString(R.string.image_option_load), getString(R.string.image_option_camera)}, (dialogInterface, i) -> {
            if (i == 0) {
                pickPhoto();
            } else if (i == 1) {
                dispatchTakePictureIntent();
            }
            dialogInterface.dismiss();
        }).show();
    }

    /**
     * launches file chooser to choose a photo
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //check if there is an app that can handle this intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    /**
     * launches camera to take a picture
     */
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
                //save uri to be later taken in onActivityResult() as the returned intent from camera
                //doesn't return captured image uri
                cameraImageUri = photoURI.toString();

                //add uri of the created file to the intent
                //that will be passed to the camera
                //so as the camera knows that it should store the captured image
                //in the provided file uri
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * creates a file with a unique name
     * the file format is jpg as it will be storing
     * an image taken by camera app
     *
     * @return
     * @throws IOException
     */
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

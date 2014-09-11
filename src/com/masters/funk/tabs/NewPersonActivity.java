package com.masters.funk.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.masters.funk.tabs.models.Person;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Activity for details about a new person
 */
public class NewPersonActivity extends DialogFragment {
  private String title;
  private int layoutId;
  private DialogInterface.OnClickListener listener;
  private static final int RESULT_LOAD_IMAGE = 0;
  private static final int PICK_FROM_GALLERY = 1;
  private ImageView imageView;

  public NewPersonActivity(String title, int layoutId, DialogInterface.OnClickListener listener) {
    this.title = title;
    this.layoutId = layoutId;
    this.listener = listener;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    // custom title view
    View titleView = inflater.inflate(R.layout.style_custom_dialog, null);
    TextView titleTv = (TextView) titleView.findViewById(R.id.dialogTitleText);
    titleTv.setText(title);
    View mainView = inflater.inflate(layoutId, null);
    imageView = (ImageView) mainView.findViewById(R.id.newPersonPic);
    imageView.setOnClickListener(imageClickListener);
    builder.setView(mainView)
      .setCustomTitle(titleView)
      .setPositiveButton("Add", listener)
      .setNegativeButton("Cancel", listener);
    return builder.create();
  }

  View.OnClickListener imageClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Intent intent = new Intent();
      // call android default gallery
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_PICK);
      // ******** code for crop image
      intent.putExtra("crop", "true");
      intent.putExtra("aspectX", 0);
      intent.putExtra("aspectY", 0);
      intent.putExtra("outputX", 100);
      intent.putExtra("outputY", 100);

      try {
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RESULT_LOAD_IMAGE);
      } catch (ActivityNotFoundException e) {
// Do nothing for now
      }
    }
  };

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.i("req code", requestCode + " " + resultCode + " " + data);
    if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
      // try to get cropped image. if that is not available, then manually crop the given image.
      Bundle extras = data.getExtras();
      if (extras != null) {
        Bitmap photo = extras.getParcelable("data");
        imageView.setImageBitmap(photo);
      } else {
        Uri imageUri = data.getData();
        try {
          Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
          imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
          e.printStackTrace();
        }
//        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
//        imageView.setImageBitmap(bitmap);

      }
    }
  }

}

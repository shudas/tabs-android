package com.masters.funk.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.masters.funk.tabs.models.Person;

/**
 *
 */
public class CustomDialogFragment extends DialogFragment {
  private String title;
  private int layoutId;
  private DialogInterface.OnClickListener listener;

  public CustomDialogFragment(String title, int layoutId, DialogInterface.OnClickListener listener) {
    this.title = title;
    this.layoutId = layoutId;
    this.listener = listener;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(getActivity().getLayoutInflater().inflate(layoutId, null))
      .setTitle(title)
      .setPositiveButton("Add", listener)
      .setNegativeButton("Cancel", listener);
    return builder.create();
  }
}

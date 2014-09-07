package com.masters.funk.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

/**
 *
 */
public class CatchupDialogFragment extends DialogFragment {

  private String title;
  private DialogInterface.OnClickListener listener;

  public CatchupDialogFragment(String title, DialogInterface.OnClickListener listener) {
    this.title = title;
    this.listener = listener;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    View mainView = getActivity().getLayoutInflater().inflate(R.layout.dialog_catchup, null);
    builder.setView(mainView)
      .setTitle(title)
      .setPositiveButton("Add", listener)
      .setNegativeButton("Cancel", listener);
    return builder.create();
  }

  public void onCatchupRadioClick(View v) {

  }
}

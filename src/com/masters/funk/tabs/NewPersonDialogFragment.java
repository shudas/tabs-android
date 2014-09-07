package com.masters.funk.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import com.masters.funk.tabs.models.Person;

/**
 *
 */
public class NewPersonDialogFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    return builder.setTitle(getString(R.string.add_person))
      .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_new_person, null))
      .setPositiveButton("Add", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          ((CatalogActivity) getActivity()).newPersonOnClick(dialog, which);
        }
      }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        ((CatalogActivity) getActivity()).newPersonOnClick(dialog, which);
      }
    }).create();
  }
}

package com.masters.funk.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import com.masters.funk.tabs.helpers.ImageAdapter;

/**
 *
 */
public class NewTabActivity extends Activity {

  EditText newTabEditText;
  Button okButton;
  Button cancelButton;
  boolean okEnabled = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_tab);
    setTitle(getString(R.string.new_tab_title));
    newTabEditText = (EditText) findViewById(R.id.newTabText);
    GridView gridview = (GridView) findViewById(R.id.tabIconGrid);
    gridview.setAdapter(new ImageAdapter(this));
    // select first item by default
    gridview.setSelection(0);
    newTabEditText.addTextChangedListener(watcher);
    okButton = (Button) findViewById(R.id.newTabOk);
    cancelButton = (Button) findViewById(R.id.newTabCancel);
    okButton.setOnClickListener(okOnClick);
    cancelButton.setOnClickListener(cancelOnClick);

    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        
      }
    });
  }

  TextWatcher watcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      if (okEnabled) {
        return;
      }

      if (newTabEditText != null) {
        if (!newTabEditText.getText().toString().trim().equals("")) {
          okButton.setEnabled(true);
        } else {
          okButton.setEnabled(false);
        }
      } else {
        okButton.setEnabled(false);
      }
    }
  };

  View.OnClickListener okOnClick = new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
  };

  View.OnClickListener cancelOnClick = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      finish();
    }
  };
}

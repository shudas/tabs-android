package com.masters.funk.tabs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.masters.funk.tabs.helpers.CardAdapter;
import com.masters.funk.tabs.helpers.DatabaseHelper;
import com.masters.funk.tabs.models.Person;
import com.masters.funk.tabs.models.Tab;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends ListActivity {

  private List<Tab> tabs = new ArrayList<Tab>();
  private CardAdapter adapter;
  private DatabaseHelper db;
  private Person person;
  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card);
    Intent intent = getIntent();
    try {
      person = intent.getExtras().getParcelable(CatalogActivity.EXTRA_PERSON);
      Log.d("YO", "PERSON ID IN CARD: " + String.valueOf(person.getId()));
      setTitle(person.getName());
    } catch (Exception e) {
      Log.e("YO", "Got exception");
      e.printStackTrace();
    }

    db = new DatabaseHelper(this);
    tabs = db.getTabsPerPerson(person.getId());
    adapter = new CardAdapter(this, R.layout.item, tabs);
    setListAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_card, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_catchup:
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        Toast.makeText(this, "Catch up selected!", Toast.LENGTH_SHORT).show();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void newTabOnClick(View view) {
    Intent intent = new Intent(this, NewTabActivity.class);
    startActivity(intent);
//    DialogFragment newFragment =
//      new NewPersonDialogFragment(getString(R.string.add_tab_dialog_title),
//                                  R.layout.dialog_new_tab,
//                                  new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                      newTabOnClick(dialog, which);
//                                    }
//                                  });
//    newFragment.show(getFragmentManager(), "new person");
  }

  public void catchUpOnClick(DialogInterface dialog, int which) {
    // store to db if pos, otherwise, close dialog
    if (which == DialogInterface.BUTTON_POSITIVE) {

    }
  }

  public void newTabOnClick(DialogInterface dialog, int which) {
    // store to db if pos, otherwise, close dialog
    if (which == DialogInterface.BUTTON_POSITIVE) {
      EditText name = (EditText) ((AlertDialog) dialog).findViewById(R.id.newTabText);
      Tab tab = new Tab();
      tab.setText(name.getText().toString());
      tab.setUpdateTimeMillis(System.currentTimeMillis());
      tab.setPersonId(person.getId());
      Log.d("NEW TAB ON CLICK", name.getText().toString());
      db.createTab(tab);
      adapter.insert(tab, 0);
      adapter.notifyDataSetChanged();
    }
  }
}

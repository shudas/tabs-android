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
import android.widget.ListView;
import com.masters.funk.tabs.helpers.CatalogAdapter;
import com.masters.funk.tabs.helpers.DatabaseHelper;
import com.masters.funk.tabs.models.Person;

import java.util.ArrayList;

/**
 *
 */
public class CatalogActivity extends ListActivity {

  public static final String EXTRA_PERSON = "person";
  private static final String EXTRA_ALL_PEOPLE = "allPeople";
  private static final String LOG = CatalogActivity.class.getSimpleName();

  private ArrayList<Person> persons = new ArrayList<Person>();
  private CatalogAdapter adapter;
  private DatabaseHelper db;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_catalog);
    setTitle(getString(R.string.catalog_title));
    db = new DatabaseHelper(this);
    persons = (ArrayList<Person>) db.getAllPersons();
    for (Person p : persons) {
      Log.d(LOG, p.getName());
    }
    adapter = new CatalogAdapter(this, R.layout.item, persons);
    setListAdapter(adapter);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Person person = (Person) getListAdapter().getItem(position);
    Intent intent = new Intent(this, CardActivity.class);
    intent.putExtra(EXTRA_PERSON, person);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    getMenuInflater().inflate(R.menu.menu_catalog, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
      case R.id.action_add_person:
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        DialogFragment newFragment =
          new NewPersonDialogFragment(getString(R.string.add_person_dialog_title),
                                      R.layout.dialog_new_person,
                                      new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                          newPersonOnClick(dialog, which);
                                        }
                                      });
        newFragment.show(getFragmentManager(), "new person");
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void newPersonOnClick(DialogInterface dialog, int which) {
    // store to db if pos, otherwise, close dialog
    if (which == DialogInterface.BUTTON_POSITIVE) {
      EditText name = (EditText) ((AlertDialog) dialog).findViewById(R.id.newPersonName);
      Person person = new Person();
      person.setName(name.getText().toString());
      // set the adapter person's unique id
      long uniqueId = db.createPerson(person);
      person.setId(uniqueId);
      adapter.insert(person, 0);
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onPause() {
    db.close();
    super.onPause();
  }
}

package com.masters.funk.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.masters.funk.tabs.helpers.DatabaseHelper;
import com.masters.funk.tabs.models.Person;
import org.w3c.dom.Comment;

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
    adapter = new CatalogAdapter(this, R.layout.item_tab, persons);
    setListAdapter(adapter);

//    // here we are defining our runnable thread.
//    Runnable viewParts = new Runnable() {
//      public void run() {
//        handler.sendEmptyMessage(0);
//      }
//    };
//
//    // here we call the thread we just defined - it is sent to the handler below.
//    Thread thread =  new Thread(null, viewParts, "catalogBackground");
//    thread.start();
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
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_catalog, menu);
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
        DialogFragment newFragment = new NewPersonDialogFragment();
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
      db.createPerson(person);
      adapter.add(person);
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onPause() {
    db.closeDB();
    super.onPause();
  }

//  @Override
//  public void onSaveInstanceState(Bundle savedState) {
//    savedState.putParcelableArrayList(EXTRA_ALL_PEOPLE, persons);
//    super.onSaveInstanceState(savedState);
//  }

//  @Override
//  public void onRestoreInstanceState(Bundle savedInstanceState) {
//    // Always call the superclass so it can restore the view hierarchy
//    super.onRestoreInstanceState(savedInstanceState);
//    // Restore state members from saved instance
//    persons = savedInstanceState.getParcelableArrayList(EXTRA_ALL_PEOPLE);
////    Toast.makeText(this, persons.size(), Toast.LENGTH_SHORT).show();
//    adapter.notifyDataSetChanged();
//  }

  private Handler handler = new Handler()
  {
    public void handleMessage(Message msg)
    {
      // create some objects
      // here is where you could also request data from a server
      // and then create objects from that data.
      Log.d(LOG, "Got here");
      persons = (ArrayList<Person>) db.getAllPersons();
      Log.d(LOG, String.valueOf(persons.size()));
      adapter = new CatalogAdapter(CatalogActivity.this, R.layout.item_tab, persons);

      // display the list.
      setListAdapter(adapter);
    }
  };
}

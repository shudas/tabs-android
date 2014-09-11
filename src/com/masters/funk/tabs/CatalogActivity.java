package com.masters.funk.tabs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.masters.funk.tabs.helpers.CatalogAdapter;
import com.masters.funk.tabs.helpers.DatabaseHelper;
import com.masters.funk.tabs.models.Person;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 *
 */
public class CatalogActivity extends ListActivity {

  public static final String EXTRA_PERSON = "person";
  private static final String EXTRA_ALL_PEOPLE = "allPeople";
  private static final int NEW_PERSON_ACTIVITY = 0;

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

    registerForContextMenu(getListView());
  }

  @Override
  public void onResume() {
    super.onResume();
    persons = (ArrayList<Person>) db.getAllPersons();
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
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
  {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.setHeaderTitle("Are you sure you wish to delete?");
    getMenuInflater().inflate(R.menu.menu_context, menu);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    //  info.position will give the index of selected item
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
      case R.id.deleteContext:
        Person person = adapter.getItem(info.position);
        db.deletePerson(person.getId());
        adapter.remove(person);
        return true;
      default:
        return super.onContextItemSelected(item);
    }
  }

    @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
      case R.id.action_add_person:
        createEditPerson(null);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void newPersonOnClick(DialogInterface dialog, int which) {
    // store to db if pos, otherwise, close dialog
    if (which == DialogInterface.BUTTON_POSITIVE) {
      EditText name = (EditText) ((AlertDialog) dialog).findViewById(R.id.newPersonName);
      ImageView imageView = (ImageView) ((AlertDialog) dialog).findViewById(R.id.newPersonPic);
      Person person = new Person();
      person.setName(name.getText().toString());
      Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
      person.setPhoto(stream.toByteArray());
      // set the adapter person's unique id
      long uniqueId = db.createPerson(person);
      person.setId(uniqueId);
      Log.i("NEW PERSON ON CLICK", person.toString());
      adapter.insert(person, 0);
      adapter.notifyDataSetChanged();
    }
  }

  private void createEditPerson(Person person) {
    // DialogFragment.show() will take care of adding the fragment
    // in a transaction.  We also want to remove any currently showing
    // dialog, so make our own transaction and take care of that here.
    DialogFragment newFragment =
      new NewPersonActivity(getString(R.string.add_person_dialog_title),
                               R.layout.dialog_new_person,
                               new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                   newPersonOnClick(dialog, which);
                                 }
                               });
    newFragment.show(getFragmentManager(), "new person");
  }

  @Override
  public void onPause() {
    db.close();
    super.onPause();
  }
}

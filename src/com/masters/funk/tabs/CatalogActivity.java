package com.masters.funk.tabs;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.masters.funk.tabs.helpers.DatabaseHelper;
import com.masters.funk.tabs.models.Person;

import java.util.ArrayList;

/**
 *
 */
public class CatalogActivity extends ListActivity {

  public static final String EXTRA_PERSON = "person";

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

    adapter = new CatalogAdapter(this, R.layout.item_tab, persons);
    setListAdapter(adapter);

    // here we are defining our runnable thread.
    Runnable viewParts = new Runnable() {
      public void run() {
        handler.sendEmptyMessage(0);
      }
    };

    // here we call the thread we just defined - it is sent to the handler below.
    Thread thread =  new Thread(null, viewParts, "catalogBackground");
    thread.start();
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
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private Handler handler = new Handler()
  {
    public void handleMessage(Message msg)
    {
      // create some objects
      // here is where you could also request data from a server
      // and then create objects from that data.
//      persons.add(new Person(0, "David Quesada", 5, null));
//      persons.add(new Person(0, "Shuvajit Das", 10, null));
//      persons.add(new Person(0, "bxxShuvajit Das", 10, null));
//      persons.add(new Person(0, "Shuvajit Das", 10, null));
//      persons.add(new Person(0, "ncShuvajit Das", 10, null));
//      persons.add(new Person(0, "Shuvajit Das", 10, null));
//      persons.add(new Person(0, "wyShuvajit Das", 10, null));
//      persons.add(new Person(0, "Shuvsdcsajit Das", 10, null));
//      persons.add(new Person(0, "njShuvajit Das", 10, null));
//      persons.add(new Person(0, "Shuvajiasdft Das", 10, null));
//      persons.add(new Person(0, "awefShuvajit Das", 10, null));
//      persons.add(new Person(0, "Shuvacejit Das", 10, null));
//      persons.add(new Person(0, "Shuvajaesfit Das", 10, null));
//      persons.add(new Person(0, "Shuvajit Das", 10, null));
//      persons.add(new Person(0, "Shuvajweffit Das", 10, null));
//      persons.add(new Person(0, "zSvxhuvajit Das", 10, null));
//      persons.add(new Person(0, "Shuvaewefjit Das", 10, null));
//      persons.add(new Person(0, "Sbuhuvajit Dsas", 10, null));
//      persons.add(new Person(0, "hrwShuvajit Das", 10, null));
//      persons.add(new Person(0, "Das", 10, null));
//      persons.add(new Person(0, "Shungvajit Das", 10, null));

      persons = (ArrayList<Person>) db.getAllPersons();
      adapter = new CatalogAdapter(CatalogActivity.this, R.layout.item_tab, persons);

      // display the list.
      setListAdapter(adapter);
    }
  };
}

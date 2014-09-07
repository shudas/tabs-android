package com.masters.funk.tabs;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
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
import com.masters.funk.tabs.helpers.TimeAlarm;
import com.masters.funk.tabs.models.Person;
import com.masters.funk.tabs.models.Tab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CardActivity extends ListActivity {

  private static final int CODE = 0;
  public static final String PERSON_NAME = "NAME";
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
        Toast.makeText(this, "Catch up selected!", Toast.LENGTH_SHORT).show();
        // Todo: Change this time
        createScheduledNotification(10);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      Tab tab = new Tab();
      tab.setText(data.getStringExtra(NewTabActivity.TAB_TEXT));
      tab.setUpdateTimeMillis(System.currentTimeMillis());
      tab.setPersonId(person.getId());
      // use puzzle drawable by default
      tab.setTabIcon(getResources().getResourceEntryName(data.getIntExtra(NewTabActivity.TABICON_PICK,
                                                                          R.drawable.tabi_puzzle)));
      Log.d("TAB", tab.getPersonId() + " " + tab.getTabIcon());
      db.createTab(tab);
      adapter.insert(tab, 0);
      adapter.notifyDataSetChanged();
    }
  }

  public void newTabOnClick(View view) {
    Intent intent = new Intent(this, NewTabActivity.class);
    startActivityForResult(intent, 0);
  }

  public void catchUpOnClick(DialogInterface dialog, int which) {
    // store to db if pos, otherwise, close dialog
    if (which == DialogInterface.BUTTON_POSITIVE) {

    }
  }

  private void createScheduledNotification(int secs)
  {
    // Get new calendar object and set the date to now
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    // Add defined amount of days to the date
    calendar.add(Calendar.SECOND, secs);
    Log.d("Calendar ", String.valueOf(calendar.getTimeInMillis()));

    // Every scheduled intent needs a different ID, else it is just executed once
    int id = (int) System.currentTimeMillis();

    // Prepare the intent which should be launched at the date
    Intent intent = new Intent(this, TimeAlarm.class);
    intent.putExtra(PERSON_NAME, person.getName());

    PendingIntent pendingIntent =
      PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
    AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarmMgr.cancel(pendingIntent);

    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                          30 * 10000,
                          pendingIntent);

  }

}

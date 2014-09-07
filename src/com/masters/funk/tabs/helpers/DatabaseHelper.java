package com.masters.funk.tabs.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.masters.funk.tabs.models.Person;
import com.masters.funk.tabs.models.Tab;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
  // Logcat tag
  private static final String LOG = "DatabaseHelper";

  // Database Version
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "tabsManager";

  // Table Names
  private static final String TABLE_PEOPLE = "people";
  private static final String TABLE_TAG = "tabs";

  // Common column names
  private static final String KEY_ID = "id";

  // PEOPLE Table - column names
  private static final String KEY_NAME = "name";
  private static final String KEY_CATCHUP = "catchup_setting";
  private static final String KEY_PHOTO = "photo";

  // TABS Table - column names
  private static final String KEY_TEXT = "text";
  private static final String KEY_ICON = "icon";
  private static final String KEY_UPDATE_TIME = "update_time";
  private static final String KEY_PEOPLE_ID = "people_id";

  // PEOPLE Table CREATE Statement
  private static final String CREATE_TABLE_PEOPLE = "CREATE TABLE "
    + TABLE_PEOPLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
    + " TEXT," + KEY_CATCHUP + " INTEGER," + KEY_PHOTO + " BLOB" + ")";

  private static final String CREATE_TABLE_TABS = "CREATE TABLE "
    + TABLE_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEXT
    + " TEXT," + KEY_ICON + " STRING," + KEY_UPDATE_TIME + " INTEGER"
    + KEY_PEOPLE_ID + " INTEGER" + ")";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // creating required tables
    db.execSQL(CREATE_TABLE_PEOPLE);
    db.execSQL(CREATE_TABLE_TABS);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // on upgrade drop older tables
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
    // create new tables
    onCreate(db);
  }

  /**
   * Create a Tab with a Person associated with the Tab
   *
   * @param tab
   */
  public void createTab(Tab tab) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_TEXT, tab.getText());
    values.put(KEY_ICON, tab.getTabIcon());
    values.put(KEY_UPDATE_TIME, tab.getUpdateTimeMillis());
    values.put(KEY_PEOPLE_ID, tab.getPersonId());

    // insert row
    db.insert(TABLE_TAG, null, values);
  }

  /**
   * Get single Tab from database given the unique id.
   * @param tabId the id of the Tab
   * @return the Tab or null if something goes wrong
   */
  @Nullable
  public Tab getTab(long tabId) {
    SQLiteDatabase db = this.getReadableDatabase();
    String selectQuery = "SELECT  * FROM " + TABLE_TAG + " WHERE "
      + KEY_ID + " = " + tabId;

    Log.d(LOG, selectQuery);

    Cursor c = db.rawQuery(selectQuery, null);

    if (c != null) {
      c.moveToFirst();
    } else {
      return null;
    }

    Tab tab = new Tab();
    tab.setId(c.getInt(c.getColumnIndex(KEY_ID)));
    tab.setText((c.getString(c.getColumnIndex(KEY_TEXT))));
    tab.setTabIcon(c.getString(c.getColumnIndex(KEY_ICON)));
    tab.setUpdateTimeMillis(c.getInt(c.getColumnIndex(KEY_PEOPLE_ID)));

    return tab;
  }

  /**
   * Get all Tabs.
   * @return List of all Tabs
   */
  public List<Tab> getAllTabs() {
    List<Tab> tabs = new ArrayList<Tab>();
    String selectQuery = "SELECT  * FROM " + TABLE_TAG;

    Log.d(LOG, selectQuery);

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (c.moveToFirst()) {
      do {
        Tab tab = new Tab();
        tab.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        tab.setText((c.getString(c.getColumnIndex(KEY_TEXT))));
        tab.setTabIcon(c.getString(c.getColumnIndex(KEY_ICON)));
        tab.setUpdateTimeMillis(c.getInt(c.getColumnIndex(KEY_PEOPLE_ID)));
      } while (c.moveToNext());
    }
    return tabs;
  }

  /**
   * Updating a Tab
   * @param tab Tab with new informations
   * @return
   */
  public int updateTab(Tab tab) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_TEXT, tab.getText());
    values.put(KEY_ICON, tab.getTabIcon());
    values.put(KEY_PEOPLE_ID, tab.getPersonId());

    // updating row
    return db.update(TABLE_TAG, values, KEY_ID + " = ?", new String[] { String.valueOf(tab.getId()) });
  }

  /**
  * Deleting a Tab
  */
  public void deleteTab(long tabId) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_TAG, KEY_ID + " = ?", new String[] { String.valueOf(tabId) });
  }

  /**
   * Creating a Person
   */
  public void createPerson(Person person) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_NAME, person.getName());
    values.put(KEY_CATCHUP, person.getCatchup());
    values.put(KEY_PHOTO, person.getPhoto());

    // insert row
    db.insert(TABLE_PEOPLE, null, values);
  }

  /**
   * Get single Person from database given the unique id.
   * @param personId the id of the person
   * @return the Person or null if something goes wrong
   */
  @Nullable
  public Person getPerson(long personId) {
    SQLiteDatabase db = this.getReadableDatabase();
    String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE + " WHERE " + KEY_ID + " = " + personId;

    Log.d(LOG, selectQuery);
    Cursor c = db.rawQuery(selectQuery, null);

    if (c != null) {
      c.moveToFirst();
    } else {
      return null;
    }

    Person person = new Person();
    person.setId(c.getInt(c.getColumnIndex(KEY_ID)));
    person.setName((c.getString(c.getColumnIndex(KEY_NAME))));
    person.setCatchup(c.getInt(c.getColumnIndex(KEY_CATCHUP)));
    person.setPhoto(c.getBlob(c.getColumnIndex(KEY_PHOTO)));

    return person;
  }

  /**
   * Get all persons.
   * @return List of all Persons
   */
  public List<Person> getAllPersons() {
    List<Person> persons = new ArrayList<Person>();
    String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE;

    Log.d(LOG, selectQuery);

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (c.moveToFirst()) {
      do {
        Person person = new Person();
        person.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        person.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        person.setCatchup(c.getInt(c.getColumnIndex(KEY_CATCHUP)));
        person.setPhoto(c.getBlob(c.getColumnIndex(KEY_PHOTO)));
      } while (c.moveToNext());
    }
    return persons;
  }

  /**
   * Updating a person
   * @param person Person with new informations
   * @return
   */
  public int updatePerson(Person person) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, person.getName());
    values.put(KEY_CATCHUP, person.getCatchup());
    values.put(KEY_PHOTO, person.getPhoto());

    // updating row
    return db.update(TABLE_PEOPLE, values, KEY_ID + " = ?", new String[] { String.valueOf(person.getId()) });
  }

  /**
   * Deleting a Person
   */
  public void deletePerson(long personId) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_PEOPLE, KEY_ID + " = ?", new String[] { String.valueOf(personId) });
  }

  /**
   * Close the database.
   */
  public void closeDB() {
    SQLiteDatabase db = this.getReadableDatabase();
    if (db != null && db.isOpen()) {
      db.close();
    }
  }
}

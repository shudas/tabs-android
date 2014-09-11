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

  // Database Version.
  private static final int DATABASE_VERSION = 2;

  // Database Name
  private static final String DATABASE_NAME = "tabsManager";

  // Table Names
  private static final String TABLE_PEOPLE = "people";
  private static final String TABLE_TAB = "tabs";

  // Common column names
  private static final String KEY_ID = "id";
  private static final String KEY_UPDATE_TIME = "update_time";

  // PEOPLE Table - column names
  private static final String KEY_NAME = "name";
  private static final String KEY_CATCHUP = "catchup_setting";
  private static final String KEY_PHOTO = "photo";

  // TABS Table - column names
  private static final String KEY_TEXT = "text";
  private static final String KEY_ICON = "icon";
  private static final String KEY_PEOPLE_ID = "people_id";

  // PEOPLE Table CREATE Statement
  private static final String CREATE_TABLE_PEOPLE = "CREATE TABLE "
    + TABLE_PEOPLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
    + KEY_CATCHUP + " INTEGER," + KEY_UPDATE_TIME + " INTEGER," + KEY_PHOTO + " BLOB" + ")";

  private static final String CREATE_TABLE_TABS = "CREATE TABLE "
    + TABLE_TAB + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEXT
    + " TEXT," + KEY_ICON + " STRING," + KEY_UPDATE_TIME + " INTEGER," + KEY_PEOPLE_ID + " INTEGER" + ")";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // creating required tables
    db.execSQL(CREATE_TABLE_PEOPLE);
    Log.d(LOG, CREATE_TABLE_TABS);
    db.execSQL(CREATE_TABLE_TABS);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // on upgrade drop older tables
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAB);
    // create new tables
    onCreate(db);
  }

  /**
   * Create a Tab with a Person associated with the Tab
   *
   * @param tab
   */
  public long createTab(Tab tab) {
    Log.d(LOG, "Create Tab got called");
    Log.d(LOG, "Create Table statment: " + CREATE_TABLE_TABS);
    Log.d(LOG, "Text " + tab.getText() + " UpdateTime " + tab.getUpdateTimeMillis() + " People Id " + tab.getPersonId());
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_TEXT, tab.getText());
    values.put(KEY_ICON, tab.getTabIcon());
    values.put(KEY_UPDATE_TIME, tab.getUpdateTimeMillis());
    values.put(KEY_PEOPLE_ID, tab.getPersonId());
    Log.i("Create Tab UPDATE TIME: ", values.getAsString(KEY_UPDATE_TIME));
    // insert row
    return db.insert(TABLE_TAB, null, values);
//    db.close();
  }

  /**
   * Get single Tab from database given the unique id.
   * @param tabId the id of the Tab
   * @return the Tab or null if something goes wrong
   */
  @Nullable
  public Tab getTab(long tabId) {
    SQLiteDatabase db = this.getReadableDatabase();
    String selectQuery = "SELECT  * FROM " + TABLE_TAB + " WHERE "
      + KEY_ID + " = " + tabId;
    Log.d(LOG, selectQuery);

    Cursor c = db.rawQuery(selectQuery, null);
    if (c != null) {
      c.moveToFirst();
    } else {
      return null;
    }

    Tab tab = new Tab();
    tab.setId(c.getLong(c.getColumnIndex(KEY_ID)));
    tab.setText((c.getString(c.getColumnIndex(KEY_TEXT))));
    tab.setTabIcon(c.getString(c.getColumnIndex(KEY_ICON)));
    tab.setUpdateTimeMillis(c.getLong(c.getColumnIndex(KEY_UPDATE_TIME)));
    tab.setPersonId(c.getLong(c.getColumnIndex(KEY_PEOPLE_ID)));
    Log.i("Tab UPDATE TIME: ", String.valueOf(tab.getUpdateTimeMillis()));
    c.close();
    return tab;
  }

  /**
   * Get all Tabs.
   * @return List of all Tabs
   */
  public List<Tab> getAllTabs() {
    List<Tab> tabs = new ArrayList<Tab>();
    String selectQuery = "SELECT  * FROM " + TABLE_TAB;

    Log.d(LOG, selectQuery);

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (c.moveToFirst()) {
      do {
        Tab tab = new Tab();
        tab.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        tab.setText((c.getString(c.getColumnIndex(KEY_TEXT))));
        tab.setTabIcon(c.getString(c.getColumnIndex(KEY_ICON)));
        tab.setUpdateTimeMillis(c.getLong(c.getColumnIndex(KEY_UPDATE_TIME)));
        tab.setPersonId(c.getLong(c.getColumnIndex(KEY_PEOPLE_ID)));
        tabs.add(tab);
      } while (c.moveToNext());
    }
    c.close();
    db.close();
    return tabs;
  }

  /**
   * Get all Tabs.
   * @return List of all Tabs
   */
  public List<Tab> getTabsPerPerson(long personId) {
    List<Tab> tabs = new ArrayList<Tab>();
    String selectQuery = "SELECT  * FROM " + TABLE_TAB + " WHERE " + KEY_PEOPLE_ID + " = " + personId
      + " ORDER BY " + KEY_UPDATE_TIME + " DESC";

    Log.d(LOG, selectQuery);

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (c.moveToFirst()) {
      do {
        Tab tab = new Tab();
        tab.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        tab.setText((c.getString(c.getColumnIndex(KEY_TEXT))));
        tab.setTabIcon(c.getString(c.getColumnIndex(KEY_ICON)));
        tab.setUpdateTimeMillis(c.getLong(c.getColumnIndex(KEY_UPDATE_TIME)));
        tab.setPersonId(c.getLong(c.getColumnIndex(KEY_PEOPLE_ID)));
        tabs.add(tab);
        Log.i("Tab UPDATE TIME: ", String.valueOf(tab.getUpdateTimeMillis()));
      } while (c.moveToNext());
    }
    c.close();
    db.close();
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
    return db.update(TABLE_TAB, values, KEY_ID + " = ?", new String[] { String.valueOf(tab.getId()) });
  }

  /**
  * Deleting a Tab
  */
  public void deleteTab(long tabId) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_TAB, KEY_ID + " = ?", new String[] { String.valueOf(tabId) });
    db.close();
  }

  /**
   * Creating a Person
   */
  public long createPerson(Person person) {
    Log.d(LOG, "Create person called");
    SQLiteDatabase db = this.getWritableDatabase();
    // insert row
    long id = db.insert(TABLE_PEOPLE, null, valuesFromPerson(person));
    db.close();
    return id;
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
    Person person = personFromCursor(c);
    c.close();
//    db.close();
    return person;
  }

  /**
   * Get all persons.
   * @return List of all Persons
   */
  public List<Person> getAllPersons() {
    Log.d(LOG, "Got getAllPersons");
    List<Person> persons = new ArrayList<Person>();
    String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE + " ORDER BY " + KEY_UPDATE_TIME + " DESC";

    Log.d(LOG, selectQuery);

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (c.moveToFirst()) {
      do {
        persons.add(personFromCursor(c));
      } while (c.moveToNext());
    }
//    c.close();
    Log.d(LOG, String.valueOf(persons.size()));
//    db.close();
    return persons;
  }

  /**
   * Updating a person
   * @param person Person with new informations
   * @return
   */
  public int updatePerson(Person person) {
    SQLiteDatabase db = this.getWritableDatabase();
    Log.i("DATABASE: ", "Database updated: " + person.toString());
    // updating row
    return db.update(TABLE_PEOPLE, valuesFromPerson(person), KEY_ID + " = ?",
                     new String[] { String.valueOf(person.getId()) });
  }

  /**
   * Deleting a Person
   */
  public void deletePerson(long personId) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_PEOPLE, KEY_ID + " = ?", new String[] { String.valueOf(personId) });
    db.close();
  }

  /**
   * Create ContentValues object from a Person
   *
   * @param person
   * @return
   */
  private ContentValues valuesFromPerson(Person person) {
    ContentValues values = new ContentValues();
    values.put(KEY_NAME, person.getName());
    values.put(KEY_CATCHUP, person.getCatchup());
    values.put(KEY_PHOTO, person.getPhoto());
    values.put(KEY_UPDATE_TIME, person.getUpdateTime());
    return values;
  }

  private Person personFromCursor(Cursor c) {
    Person person = new Person();
    person.setId(c.getInt(c.getColumnIndex(KEY_ID)));
    person.setName((c.getString(c.getColumnIndex(KEY_NAME))));
    person.setCatchup(c.getInt(c.getColumnIndex(KEY_CATCHUP)));
    person.setPhoto(c.getBlob(c.getColumnIndex(KEY_PHOTO)));
    person.setUpdateTime(c.getLong(c.getColumnIndex(KEY_UPDATE_TIME)));
    return person;
  }
}

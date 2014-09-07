package com.masters.funk.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.masters.funk.tabs.models.Person;

public class CardActivity extends Activity {
  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card);

    Intent intent = getIntent();
    Person person = intent.getExtras().getParcelable(CatalogActivity.EXTRA_PERSON);
    setTitle(person.getName());
  }
}

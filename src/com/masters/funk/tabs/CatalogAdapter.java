package com.masters.funk.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.masters.funk.tabs.models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the catalog page
 */
public class CatalogAdapter extends ArrayAdapter<Person> {

  private ArrayList<Person> persons;

  public CatalogAdapter(Context context, int resource, List<Person> objects) {
    super(context, resource, objects);
    this.persons = (ArrayList<Person>) objects;
  }

  @Override
  public Person getItem(int position) {
    return persons.get(position);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    // first check to see if the view is null. if so, we have to inflate it.
    // to inflate it basically means to render, or show, the view.
    if (v == null) {
      LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = inflater.inflate(R.layout.item_tab, null);
    }

    Person person = persons.get(position);

    if (person != null) {
      TextView name = (TextView) v.findViewById(R.id.tabText);
      name.setText(person.getName());
    }
    return v;
  }
}
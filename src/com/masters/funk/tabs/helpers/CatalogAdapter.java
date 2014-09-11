package com.masters.funk.tabs.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.masters.funk.tabs.R;
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
      v = inflater.inflate(R.layout.item, null);
    }

    Person person = persons.get(position);

    if (person != null) {
      TextView name = (TextView) v.findViewById(R.id.tabText);
      name.setText(person.getName());
      name.setTextSize(18);
      v.findViewById(R.id.updateTime).setVisibility(View.GONE);
      ImageView img = (ImageView) v.findViewById(R.id.icon);
      try {
        Bitmap bmp = BitmapFactory.decodeByteArray(person.getPhoto(), 0, person.getPhoto().length);
        img.setImageBitmap(bmp);
      } catch (Exception e) {
        // use face by default
        img.setImageResource(R.drawable.face);
      }
    }
    return v;
  }
}
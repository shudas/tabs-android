package com.masters.funk.tabs.helpers;

import android.content.Context;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.masters.funk.tabs.R;
import com.masters.funk.tabs.models.Tab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Adapter for the catalog page
 */
public class CardAdapter extends ArrayAdapter<Tab> {

  private static final String TIME_FORMAT = "h:mm";
  private static final String DAY_FORMAT = "MMM d";
  private ArrayList<Tab> tabs;

  public CardAdapter(Context context, int resource, List<Tab> objects) {
    super(context, resource, objects);
    this.tabs = (ArrayList<Tab>) objects;
  }

  @Override
  public Tab getItem(int position) {
    return tabs.get(position);
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

    Tab tab = tabs.get(position);

    if (tab != null) {
      TextView name = (TextView) v.findViewById(R.id.tabText);
      name.setText(tab.getText());
      TextView updateTime = (TextView) v.findViewById(R.id.updateTime);
      Calendar updateCal = Calendar.getInstance();
      updateCal.setTimeInMillis(tab.getUpdateTimeMillis());
      // display time if date is today, otherwise display date.
      if (inSameDay(Calendar.getInstance(), updateCal)) {
        updateTime.setText(new SimpleDateFormat(TIME_FORMAT).format(updateCal.getTime()));
      } else {
        updateTime.setText(new SimpleDateFormat(DAY_FORMAT).format(updateCal.getTime()));
      }

      ImageView img = (ImageView) v.findViewById(R.id.icon);
      try {
        img.setImageResource(getContext().getResources()
                               .getIdentifier(tab.getTabIcon(), "drawable", getContext().getPackageName()));
      } catch (Exception e) {
        img.setImageResource(R.drawable.tabi_puzzle);
        e.printStackTrace();
      }

    }
    return v;
  }

  public static boolean inSameDay(Calendar date1, Calendar date2) {
    return date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)
      && date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR);
  }
}
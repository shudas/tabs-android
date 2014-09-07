package com.masters.funk.tabs.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.masters.funk.tabs.R;

import java.util.ResourceBundle;

public class ImageAdapter extends BaseAdapter {
  private Context mContext;

  public ImageAdapter(Context c) {
    mContext = c;
  }

  public int getCount() {
    return mThumbIds.length;
  }

  @Override
  public Object getItem(int position) {
    Log.d("POS GET ITEM: ", String.valueOf(position));
    return (position < mThumbIds.length && position >= 0) ? mThumbIds[position] : null;
  }

  public long getItemId(int position) {
    return 0;
  }

  // create a new ImageView for each item referenced by the Adapter
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;
    if (convertView == null) {  // if it's not recycled, initialize some attributes
      imageView = new ImageView(mContext);
      imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
      imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//      imageView.setPadding(8, 8, 8, 8);
    } else {
      imageView = (ImageView) convertView;
    }

    imageView.setImageResource(mThumbIds[position]);
    return imageView;
  }

  // references to our images
  // Todo: Get these programmatically
  private Integer[] mThumbIds = {
    R.drawable.tabi_baloon, R.drawable.tabi_cal,
    R.drawable.tabi_coffee, R.drawable.tabi_fork,
    R.drawable.tabi_gift, R.drawable.tabi_heart,
    R.drawable.tabi_iou, R.drawable.tabi_key,
    R.drawable.tabi_knot, R.drawable.tabi_lock,
    R.drawable.tabi_music, R.drawable.tabi_pin,
    R.drawable.tabi_puzzle, R.drawable.tabi_shake,
    R.drawable.tabi_smiley, R.drawable.tabi_talk,
  };
}

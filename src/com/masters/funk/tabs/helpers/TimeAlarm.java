package com.masters.funk.tabs.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.masters.funk.tabs.CardActivity;
import com.masters.funk.tabs.R;

// The class has to extend the BroadcastReceiver to get the notification from the system
public class TimeAlarm extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent paramIntent) {

    // Request the notification manager
    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // Create a new intent which will be fired if you click on the notification
    Intent intent = new Intent();

    // Attach the intent to a pending intent
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    String message = "How is " + paramIntent.getStringExtra(CardActivity.PERSON_NAME) + " doing?";
    // Create the notification
    Notification notification = new Notification(R.drawable.launcher_04,
                                                 message,
                                                 System.currentTimeMillis());
    notification.setLatestEventInfo(context, message, "", pendingIntent);

    // Fire the notification
    notificationManager.notify(1, notification);
  }

}
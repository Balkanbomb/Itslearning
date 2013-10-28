package com.example.mad2013_itslearning;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class TimeAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String messageInfo = intent.getStringExtra("MessageInfo");
		ArrayList<String> ListInfo = intent.getStringArrayListExtra("Lista");
		 //invoking the default notification service
   		NotificationCompat.Builder mBuilder=
   				new NotificationCompat.Builder(context);
   		
   		mBuilder.setContentTitle("New Message");
   		mBuilder.setContentText(messageInfo);
   		mBuilder.setTicker("New Itslearning Message ");
   		mBuilder.setSmallIcon(R.drawable.ic_launcher);
   		
   		/* Add Big View Specific Configuration */
   	 NotificationCompat.InboxStyle inboxStyle =
	             new NotificationCompat.InboxStyle();
   	 
   	 // Sets a title for the Inbox style big view
      inboxStyle.setBigContentTitle("Big Title Details:");
      // Moves events into the big view
      for (int i=0; i < ListInfo.size(); i++) {

         inboxStyle.addLine(ListInfo.get(i));
      }
      mBuilder.setStyle(inboxStyle);
   		
   		//Creates an explicit intent in the app
   		Intent resultIntent = new Intent(context, MainActivity.class); 
   		
   		
   		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
   		stackBuilder.addParentStack(MainActivity.class);
   		
   		//ads the intent that starts the activity to the top of the stack
   		stackBuilder.addNextIntent(resultIntent);
   		PendingIntent resultPendingIntent =
   		         stackBuilder.getPendingIntent(
   		            0,
   		            PendingIntent.FLAG_UPDATE_CURRENT
   		         );
   		
   		mBuilder.setContentIntent(resultPendingIntent);

   		NotificationManager  mNotificationManager = 
   				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
   	      
   	     // notificationId allows you to update the notification later on
   	      //nm.notify( 0, mBuilder.build());
   	      
   	   

   	int mId=0;
	// mId allows you to update the notification later on.
   	mNotificationManager.notify(mId, mBuilder.build());
		
	}

}

package com.example.mad2013_itslearning;

import com.example.mad2013_itslearning.R;
import com.example.mad2013_itslearning.Article;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class TimeAlarm extends BroadcastReceiver {
	
NotificationManager nm;



    @Override
    public void onReceive(Context context, Intent intent) {

    	
    	
//     nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//     //Här hämtar vi det som skall visas
   String messageInfo = intent.getStringExtra("MessageInfo");
//     CharSequence from = "Itslearning";
//     CharSequence message = "ITSLEARNING MESS" ;
//     PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);     
//    // här skickar du messageInfo men dessa metoder verkar depricated så de bör inte användas
//     Notification notif = new Notification(R.drawable.ic_launcher, messageInfo , System.currentTimeMillis());
//     notif.setLatestEventInfo(context, from,messageInfo , contentIntent);
//     notif.
//     nm.notify(1, notif);
     
 
    
   
//     NotificationCompat.Builder  mBuilder = 
//   	      new NotificationCompat.Builder(context);	
//
//   	      mBuilder.setContentTitle("New Message");
//   	      mBuilder.setContentText("You've received new message.");
//   	     
//   	      mBuilder.setTicker("New Message Alert!");
//   	      mBuilder.setSmallIcon(R.drawable.ic_launcher);

   	     


   	      /* Add Big View Specific Configuration */
   	      //NotificationCompat.InboxStyle inboxStyle =
   	           //  new NotificationCompat.InboxStyle();

   	      //String[] events = new String[6];
   	      //events[0] = new String(messageInfo);
   	      //events[1] = new String("KD550 Teacher sick");
   	      //events[2] = new String("KD100 Class canceled");
   	      //events[3] = new String("KD400 Bla bla bla");
   	      //events[4] = new String("KD500 teacher is back");
   	      //events[5] = new String("KD589 Helooo");

   	      // Sets a title for the Inbox style big view
   	      //inboxStyle.setBigContentTitle("Big Title Details:");
   	      // Moves events into the big view
   	      //for (int i=0; i < events.length; i++) {

   	        // inboxStyle.addLine(events[i]);
   	      //}
   	      //mBuilder.setStyle(inboxStyle);
   	       
   	      
   	      /* Creates an explicit intent for an Activity in the app */
   	      //Intent resultIntent = new Intent();

   	      //TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
   	      //stackBuilder.addParentStack(NotificationView.class);

   	      /* Adds the Intent that starts the Activity to the top of the stack */
   	      //stackBuilder.addNextIntent(resultIntent);
   	      //PendingIntent resultPendingIntent =
   	         //stackBuilder.getPendingIntent(
   	            //0,
   	          //  PendingIntent.FLAG_UPDATE_CURRENT
   	        // );

   	      //mBuilder.setContentIntent(resultPendingIntent);

//   	      nm =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   	      /* notificationID allows you to update the notification later on. */
   	     // nm.notify(1, mBuilder.build());
 
//----------------------------------------------------------------------
     
   //invoking the default notification service
   		NotificationCompat.Builder mBuilder=
   				new NotificationCompat.Builder(context);
   		
   		mBuilder.setContentTitle("New Message");
   		mBuilder.setContentText(messageInfo);
   		mBuilder.setTicker("New Itslearning Message ");
   		mBuilder.setSmallIcon(R.drawable.ic_launcher);
   		
   		
   		
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
 
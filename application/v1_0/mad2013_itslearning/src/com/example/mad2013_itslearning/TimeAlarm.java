package com.example.mad2013_itslearning;

import java.util.ArrayList;
import java.util.Date;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class TimeAlarm extends IntentService implements FeedManager.FeedManagerDoneListener
{
	private final String PREFS_NAME = "data_storage.dat";
	private static final String TAG = "TimeAlarm";
	private Date latestUpdate;

	public TimeAlarm()
	{
		super("AutomaticAlarm");
		
	}

	public void onFeedManagerProgress(FeedManager fm, int progress, int max)
	{
	}

	public void onFeedManagerDone(FeedManager fm, ArrayList<Article> articles)
	{
		/*
		 * Save latest update time
		 */
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("latestUpdate", articles.get(0).getArticlePubDate().getTime());
		editor.commit();
		
		ArrayList<Article> newArticles = new ArrayList<Article>();
		for (Article a : articles)
		{
			if (a.getArticlePubDate().compareTo(latestUpdate) > 0)
			{
				newArticles.add(a);
				Log.i(TAG, a.getArticleDate() + " is newer than " + this.latestUpdate.toString());
			}
		}

		this.createNotification(newArticles);
		Log.i(TAG, newArticles.size() + " new articles, that will be sent to notification class");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		if (getApplicationContext() == null)
		{
			Log.e(TAG, "getContextOfApplication(); is null");
			return;
		}
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		latestUpdate = new Date(settings.getLong("latestUpdate", 0));
		latestUpdate = new Date(1382932800000L);

		FeedManager feedManager = new FeedManager(this, this);

		/*
		 * as soon as we add feeds to feedManager, queueSize is no longer 0,  
		 * therefore this will only be done once
		 */
		if (feedManager.queueSize() == 0)
		{
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=16066&PersonId=71004&CustomerId=719&Guid=52845be1dfae034819b676d6d2b18733&Culture=sv-SE");
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18190&PersonId=94952&CustomerId=719&Guid=96721ee137e0c918227093aa54f16f80&Culture=en-GB");
			feedManager.addFeedURL("https://mah.itslearning.com/Dashboard/NotificationRss.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
		}

		feedManager.processFeeds();
	}

	private void createNotification(ArrayList<Article> ListInfo)
	{
		//invoking the default notification service
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("New Message");
		mBuilder.setTicker("New Itslearning post");
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setAutoCancel(true);

		// Add Big View Specific Configuration 
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

		// Sets a title for the Inbox style big view
		inboxStyle.setBigContentTitle("News from Itslearning");
		
		// Moves events into the big view
		for (Article a : ListInfo)
		{
			inboxStyle.addLine(a.getArticleHeader());
		}
		
		mBuilder.setStyle(inboxStyle);

		//Creates an explicit intent in the app
		Intent resultIntent = new Intent(this, MainActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);

		//ads the intent that starts the activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());

	}
}

package com.example.mad2013_itslearning;

import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.Collections;
import java.util.List;

import org.mcsoxford.rss.RSSItem;
//import com.mah.aa_studentapp.R;
>>>>>>> origin/Sanel
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
<<<<<<< HEAD
=======
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
>>>>>>> origin/Sanel
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

/*
 * @author asampe, marcusmansson
 * 
 * 
 * HISTORY:
 * 0.7.4
 *  o moved all file handling to FeedManager
 *  o code and comments cleaned up
 *   
 * 0.7.3
 *  o merged two branches (unknown version) with 0.7.2 
 *  o course colors in the UI 
 *  o fixed text overflow in the UI
 *  
 * 0.7.2 
 * 	o added saving/loading of articles to/from cache
 *  o load saved articles and initialize list on app start 
 * 	o removed unused code
 * 
 *  
 */

public class MainActivity extends Activity implements FeedManager.FeedManagerDoneListener, 
	OnScrollListener, OnChildClickListener
{
	static final String TAG = "MainActivity";

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	FeedManager feedManager;
	ProgressDialog dialog;
	ProgressBar progBar;
	TextView txProgress;
	View headerView;
	private NotificationManager nm;
	AlarmManager am;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		ColorDrawable colorDrawable = new ColorDrawable();
		colorDrawable.setColor(0xffeeeeee);

		// custom ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.abs_layout);

		progBar = (ProgressBar) findViewById(R.id.progress);
		txProgress = (TextView) findViewById(R.id.txProgess);
		progBar.setVisibility(ProgressBar.GONE);
		txProgress.setVisibility(TextView.GONE);
		
		// create settings view and hide it
		headerView = getLayoutInflater().inflate(R.layout.itsl_list_header, null);
		this.hideSettingsView();
		
		// set up the listview
		feedManager = new FeedManager(this, this);

		listAdapter = new ExpandableListAdapter(this, feedManager.getArticles());
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		expListView.addHeaderView(headerView);
		expListView.setAdapter(listAdapter);
		expListView.setOnScrollListener(this);
<<<<<<< HEAD
		expListView.setOnChildClickListener(this);
=======
		
	//setRepeatingAlarm(feedManager.getArticles().get(0).getArticleHeader());
		
		
		
>>>>>>> origin/Sanel

		/*
		 *  in case there was nothing in the cache, or it didn't exist
		 *  we have to refresh
		 */
		if (feedManager.getArticles().isEmpty())
			refresh();
	}

	public void onFeedManagerProgress(int progress, int max)
	{
		// set up progress dialog if there isn't one
		if (dialog == null)
		{
			dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMessage("Downloading...");
			dialog.show();
		}
		dialog.setProgress(progress);
		dialog.setMax(max);

		/*
		progBar.setProgress(progress);
		progBar.setMax(max);
		*/
	}

	@Override
	public void onFeedManagerDone(ArrayList<Article> articles)
	{
		/*
		 * display the data in our listview
		 */
		listAdapter.notifyDataSetInvalidated();

		/*
		progBar.setVisibility(ProgressBar.GONE);
		txProgress.setVisibility(TextView.GONE);
		*/
		dialog.dismiss();
		dialog = null;

		Toast.makeText(getApplicationContext(), "" + articles.size() + " articles", Toast.LENGTH_LONG).show();
<<<<<<< HEAD
=======
		
		
		saveCache();
		
		//ALARM
		 ArrayList<String> events = new  ArrayList<String>();
 	     events.add(new String("k555"));
 	     events.add(new String("k600"));
 	     events.add(new String("k700"));
 	     events.add(new String("k800"));
		
		setRepeatingAlarm(articles.get(0).getArticleHeader().toString(), events);
>>>>>>> origin/Sanel
	}

	private void refresh()
	{
		/*
		 * close all expanded childviews, otherwise they will incorrectly 
		 * linger in the UI even after we invalidate the dataset
		 */
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++)
			expListView.collapseGroup(i);

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

		feedManager.reset();
		feedManager.processFeeds();
	}

	public void refreshButtonClicked(View v)
	{
		refresh();
		hideSettingsView();
	}

	public void clearAllData(View v)
	{
		feedManager.reset();
		feedManager.deleteCache();
		listAdapter.notifyDataSetInvalidated();
	}

	private void hideSettingsView()
	{
		headerView.findViewById(R.id.headerLayout).setVisibility(View.GONE);
	}

	private void showSettingsView()
	{
		headerView.findViewById(R.id.headerLayout).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (expListView.getFirstVisiblePosition() == 0 && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
			showSettingsView();
		else if (expListView.getFirstVisiblePosition() == 1 && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
			hideSettingsView();
	}
<<<<<<< HEAD

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		return parent.collapseGroup(groupPosition);
	}
=======
	
	public void setRepeatingAlarm(String message, ArrayList<String> events) {
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	      Intent intent = new Intent(this, TimeAlarm.class);
	      
	      feedManager = new FeedManager(this);
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=16066&PersonId=71004&CustomerId=719&Guid=52845be1dfae034819b676d6d2b18733&Culture=sv-SE");
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18190&PersonId=94952&CustomerId=719&Guid=96721ee137e0c918227093aa54f16f80&Culture=en-GB");
			feedManager.addFeedURL("http://www.mah.se/Nyheter/RSS/Anslagstavla-fran-Malmo-hogskola/");
			feedManager.addFeedURL("https://mah.itslearning.com/Dashboard/NotificationRss.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
	      
	      //Kan det räcka så här. Här lägger vi till det som skall visas så tt det följer med intenten till TimeAlarm
	      intent.putExtra("MessageInfo", message);
	      intent.putStringArrayListExtra("Lista", events);
	      PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
	        intent, PendingIntent.FLAG_CANCEL_CURRENT);
	      am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
	        (10 * 5000), pendingIntent);
	      System.out.println("Calling Alaram...");
	      
	      
	      
	      

	    
	     
	     }
>>>>>>> origin/Sanel
}

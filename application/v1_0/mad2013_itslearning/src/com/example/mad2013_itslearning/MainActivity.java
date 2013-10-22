package com.example.mad2013_itslearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/* @author asampe
 * @author marcusmansson
 * 
 * TODO:
 * 
 * o Save articleList (make serializable) 
 * 
 * o Load articleList
 * 
 * o Initialize list with cached articles when app is started  
 *   
 * o Set course colors and update course codes in the UI 
 * 
 * o Fix text overflow in the UI
 * 
 */
public class MainActivity extends Activity implements FeedManager.FeedManagerDoneListener
{
	private final String TAG = "RSSTEST";
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<Article> listDataHeader;
	FeedManager feedManager;
    private //*
    		//ProgressDialog dialog;
    ProgressBar progBar;
    TextView txProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Change color actionbar
		ColorDrawable colorDrawable = new ColorDrawable();
		colorDrawable.setColor(0xffdedede);

		// custom ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.abs_layout);

		//*		
		//dialog = new ProgressDialog(this);
		//dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		progBar = (ProgressBar) findViewById (R.id.progress);
		txProgress = (TextView) findViewById (R.id.txProgess);

		feedManager = new FeedManager(this);
		feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
		feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=16066&PersonId=71004&CustomerId=719&Guid=52845be1dfae034819b676d6d2b18733&Culture=sv-SE");
		feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18190&PersonId=94952&CustomerId=719&Guid=96721ee137e0c918227093aa54f16f80&Culture=en-GB");
		feedManager.addFeedURL("http://www.mah.se/Nyheter/RSS/Anslagstavla-fran-Malmo-hogskola/");
		feedManager.addFeedURL("https://mah.itslearning.com/Dashboard/NotificationRss.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
		
		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
			{
				// Toast.makeText(getApplicationContext(), "Group Clicked " + listDataHeader.get(groupPosition), Toast.LENGTH_SHORT).show();
				// parent.expandGroup(groupPosition, true);
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition)
			{
				// Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
			}
		});

		// Listview Group collapsed listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition)
			{
				// Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
			{
				parent.collapseGroup(groupPosition);
				return false;
			}
		});
		
		//Toast.makeText(getApplicationContext(), "Downloading " + feedManager.queueSize() + " feeds, please wait" , Toast.LENGTH_LONG).show();
		feedManager.processFeeds();
	}

	public void onFeedManagerProgress(int progress, int max)
	{
		
        //dialog.setMessage(String.format("Downloading feed %d of %d", progress, max));
        
		
		
		progBar.setProgress(progress);
		progBar.setMax(max);
		
		
		
		
		//*
		// dialog.setMessage("Updating");
        // dialog.setProgress(progress);
        // dialog.setMax(max);
        // dialog.show();
		//*		
		//Toast.makeText(getApplicationContext(), String.format("Downloading feed %d of %d", progress, max) , Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onFeedManagerDone(ArrayList<Article> articles)
	{
		
		Log.i(TAG, "# of articles in aggregated feed: " + articles.size());

		Toast.makeText(getApplicationContext(), "" + articles.size() + " articles", Toast.LENGTH_SHORT).show();
		
		/*
		 *  sorts the list by date in descending order (using Article.compareTo())
		 */
		Collections.sort(articles);
		
		/*
		 *  display the data in our listview
		 */
		listAdapter = new ExpandableListAdapter(this, articles);
		expListView.setAdapter(listAdapter);
		progBar.setVisibility(ProgressBar.GONE);
		txProgress.setVisibility(TextView.GONE);
        //* dialog.dismiss();
		
	}
}

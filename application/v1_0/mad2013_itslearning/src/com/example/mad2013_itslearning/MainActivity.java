package com.example.mad2013_itslearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.Toast;

/* @author asampe
 * @author marcusmansson
 * 
 * TODO:
 * o Save articleList (make serializable) and use that as default before refresh 
 *   is done when app is started
 * 
 * o Load articleList
 * 
 * o Set course colors and update course codes 
 * 
 * 
 */
public class MainActivity extends Activity implements FeedManager.FeedManagerDoneListener
{
	private final String TAG = "RSSTEST";
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<Article> listDataHeader;
	HashMap<Article, String> listDataChild;
	FeedManager feedManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Change color actionbar
		ColorDrawable colorDrawable = new ColorDrawable();
		colorDrawable.setColor(Color.WHITE);

		// custom ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.abs_layout);

		feedManager = new FeedManager(this);
		feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
		feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=16066&PersonId=71004&CustomerId=719&Guid=52845be1dfae034819b676d6d2b18733&Culture=sv-SE");
		feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18190&PersonId=94952&CustomerId=719&Guid=96721ee137e0c918227093aa54f16f80&Culture=en-GB");
		feedManager.addFeedURL("http://www.mah.se/Nyheter/RSS/Anslagstavla-fran-Malmo-hogskola/");
		
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
		
		feedManager.processFeeds();
	}

	@Override
	public void onFeedManagerDone(ArrayList<Article> articles)
	{
		Log.i(TAG, "# of articles red: " + articles.size());

		/*
		 *  sorts the list by date in descending order (using Article.compareTo())
		 */
		Collections.sort(articles);
		
		/*
		 *  display the data in our listview
		 */
		listAdapter = new ExpandableListAdapter(this, articles);
		expListView.setAdapter(listAdapter);
	}
}

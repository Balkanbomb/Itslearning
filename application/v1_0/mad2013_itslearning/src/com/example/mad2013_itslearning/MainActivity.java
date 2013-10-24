package com.example.mad2013_itslearning;

//import itslearning.platform.restApi.sdk.common.entities.UserInfo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import com.mah.aa_studentapp.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

/**
 * @author asampe, marcusmansson
 * 
 * 
 * HISTORY: 
 * 0.7.3
 *  o merged two branches (unknown version) with 0.7.2 
 *  
 * 0.7.2 
 * 	o added saving/loading of articles to/from cache
 *  o load saved articles and initialize list on app start 
 * 	o removed unused code
 * 
 *  
 * TODO: 
 * o Set course colors and update course codes in the UI 
 * o Fix text overflow in the UI
 */

public class MainActivity extends Activity implements FeedManager.FeedManagerDoneListener, OnScrollListener
{
	static final String TAG = "RSSTEST";
	static final String CACHE_FILENAME = "article_cache.ser";

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	FeedManager feedManager;
	ProgressDialog dialog;
	ProgressBar progBar;
	TextView txProgress;
	View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ColorDrawable colorDrawable = new ColorDrawable();
		colorDrawable.setColor(Color.WHITE);

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
		headerView = getLayoutInflater().inflate(R.layout.list_header, null);
		this.hideSettingsView();

		// set up the listview
		feedManager = new FeedManager(this);

		listAdapter = new ExpandableListAdapter(this, feedManager.getArticles());
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		expListView.addHeaderView(headerView);
		expListView.setAdapter(listAdapter);
		expListView.setOnScrollListener(this);

		// listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
			{
				return parent.collapseGroup(groupPosition);
			}
		});

		// fetch data from cache or web
		collectData();
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
		 * sorts the list by date in descending order
		 */
		Collections.sort(articles);

		/*
		 * display the data in our listview
		 */
		listAdapter.notifyDataSetInvalidated();

		progBar.setVisibility(ProgressBar.GONE);
		txProgress.setVisibility(TextView.GONE);
		dialog.dismiss();
		dialog = null;

		Log.i(TAG, "# of articles in feed: " + articles.size());
		Toast.makeText(getApplicationContext(), "" + articles.size() + " articles", Toast.LENGTH_LONG).show();

		saveCache();
	}

	private void saveCache()
	{
		try
		{
			FileOutputStream fos = openFileOutput(CACHE_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(listAdapter.getList());
			fos.close();
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void loadCache()
	{
		try
		{
			FileInputStream fis = openFileInput(CACHE_FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			feedManager.getArticles().addAll((List<Article>) ois.readObject());
			listAdapter.notifyDataSetInvalidated();
		}
		catch (Exception e)
		{
			// something is probably wrong with the cache file so let's delete it
			getBaseContext().getFileStreamPath(CACHE_FILENAME).delete();

			Toast.makeText(getApplicationContext(), "Please refresh", Toast.LENGTH_LONG).show();
			Log.e(TAG, e.toString());
			Log.e(TAG, CACHE_FILENAME + " deleted");
		}
	}

	private void hideSettingsView()
	{
		headerView.findViewById(R.id.headerLayout).setVisibility(View.GONE);
	}

	private void showSettingsView()
	{
		headerView.findViewById(R.id.headerLayout).setVisibility(View.VISIBLE);
	}

	private void collectData()
	{
		// check for cached content, otherwise download feeds
		if (getBaseContext().getFileStreamPath(CACHE_FILENAME).exists())
			loadCache();
		else
			refresh();
	}

	private void refresh()
	{
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++)
			expListView.collapseGroup(i);
		
		if (feedManager.queueSize() == 0)
		{
			/*
			 * as soon as we add feeds to feedManager, queueSize is no longer 0,  
			 * therefore this will only be done once
			 */
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=16066&PersonId=71004&CustomerId=719&Guid=52845be1dfae034819b676d6d2b18733&Culture=sv-SE");
			feedManager.addFeedURL("https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18190&PersonId=94952&CustomerId=719&Guid=96721ee137e0c918227093aa54f16f80&Culture=en-GB");
			feedManager.addFeedURL("http://www.mah.se/Nyheter/RSS/Anslagstavla-fran-Malmo-hogskola/");
			//feedManager.addFeedURL("https://mah.itslearning.com/Dashboard/NotificationRss.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE");
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
		getBaseContext().getFileStreamPath(CACHE_FILENAME).delete();
		listAdapter.notifyDataSetInvalidated();
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
		else
			hideSettingsView();
	}
}

package se.mah.kd330a.project.itsl;

import se.mah.kd330a.project.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import android.app.FragmentTransaction;

public class FragmentITSL extends Fragment implements FeedManager.FeedManagerDoneListener, OnChildClickListener, OnClickListener
{
	private static final String TAG = "FragmentITSL";
	private static final long UPDATE_INTERVAL = 30000; //every other minute
	FeedManager feedManager;
	ProgressDialog dialog;
	PendingIntent backgroundUpdateIntent;
	ArrayList<TabFragment> tabFragments;
	ViewPager mViewPager;
	ListPagerAdapter listPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/*
		 * set up the repeating task of updating data in the background 
		 * (but stop it while the app is running)
		 */
		Context appContext = getActivity().getApplicationContext();
		backgroundUpdateIntent = PendingIntent.getService(appContext, 0, new Intent(appContext, TimeAlarm.class), 0);

		feedManager = new FeedManager(this, appContext);
	}

	public void onPause()
	{
		super.onPause();

		Log.i(TAG, "Paused: Setting up background updates");

		AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + UPDATE_INTERVAL, UPDATE_INTERVAL, backgroundUpdateIntent);

		/*
		 * Remember when we last had this view opened 
		 */
		Date date = new Date(System.currentTimeMillis());
		date.setMonth(9); // zero based index (e.g. 0-11)
		date.setDate(20);

		Util.setLatestUpdate(getActivity().getApplicationContext(), date);
	}

	public void onResume()
	{
		super.onResume();

		Log.i(TAG, "Resumed: Stopping background updates");

		AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(backgroundUpdateIntent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_itsl, container, false);

		for (String url : Util.getBrowserBookmarks(getActivity().getApplicationContext()))
		{
			Log.i(TAG, "Got URL from bookmarks: " + url);
			feedManager.addFeedURL(url);
		}

		/*
		 *  in case there is nothing in the cache, or it doesn't exist
		 *  we have to refresh
		 */
		if (!feedManager.loadCache())
			refresh();

		/*
		 * Set up tabs.
		 */
		tabFragments = new ArrayList<TabFragment>();

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		listPagerAdapter = new ListPagerAdapter(getActivity().getSupportFragmentManager(), tabFragments);
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		mViewPager.setAdapter(listPagerAdapter);
		mViewPager.setOnPageChangeListener(
	            new ViewPager.SimpleOnPageChangeListener() {
	                @Override
	                public void onPageSelected(int position) {
	                    /*
	                     *  When swiping between pages, select the corresponding tab.
	                     */
	                    getActivity().getActionBar().setSelectedNavigationItem(position);
	                }
	            });
		/* 
		 *  Create a tab listener that is called when the user changes tabs.
		 */
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
			{
				/*
				 *  here we retrieve the tabfragment object that should already have 
				 *  been initialized and added to the adapter
				 */
				mViewPager.setCurrentItem(tab.getPosition());
			}

			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
			{
				// hide the given tab
			}

			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
			{
				// probably ignore this event
			}
		};

		/*
		 * The first tab contains everything unfiltered
		 */
		actionBar.addTab(
				actionBar.newTab()
				.setText("All")
				.setTabListener(tabListener));
		tabFragments.add(new TabFragment(feedManager.getArticles()));

		/*
		 * For as many feeds we have downloaded, create a new tab and add the 
		 * corresponding data to a new TabFragment
		 */
		HashMap<String, FeedObject> foList = getFeedObjects();

		for (String title : foList.keySet())
		{
			actionBar.addTab(
					actionBar.newTab()
					.setText(title)
					.setTabListener(tabListener));
			
			tabFragments.add(new TabFragment(foList.get(title).articles));
			
			Log.i(TAG, "Filter list has key: " + title);
		}

		listPagerAdapter.notifyDataSetChanged();
		return rootView;
	}

	public class FeedObject
	{
		public ArrayList<Article> articles;

		public FeedObject()
		{
			articles = new ArrayList<Article>();
		}
	}

	public HashMap<String, FeedObject> getFeedObjects()
	{
		HashMap<String, FeedObject> foList = new HashMap<String, FeedObject>();

		for (Article a : feedManager.getArticles())
		{
			FeedObject fo;

			if (foList.containsKey(a.getArticleCourseCode()))
			{
				fo = foList.get(a.getArticleCourseCode());
			}
			else
			{
				fo = new FeedObject();
				foList.put(a.getArticleCourseCode(), fo);
			}

			fo.articles.add(a);
		}

		return foList;
	}

	private void createTabs()
	{
		
	}
	
	public void onFeedManagerProgress(FeedManager fm, int progress, int max)
	{
		/*
		 *  set up progress dialog if there isn't one.
		 */
		if (dialog == null)
		{
			dialog = new ProgressDialog(getActivity());
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMessage("Downloading...");
			dialog.show();
		}

		dialog.setProgress(progress);
		dialog.setMax(max);
	}

	@Override
	public void onFeedManagerDone(FeedManager fm, ArrayList<Article> articles)
	{
		/*
		 * display the data in our listview
		 */

		// call refresh on each tabfragment
		
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
		
		Toast.makeText(getActivity(), "" + articles.size() + " articles", Toast.LENGTH_LONG).show();
	}

	private void refresh()
	{
		feedManager.reset();
		feedManager.processFeeds();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		return parent.collapseGroup(groupPosition);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.button1:
			refresh();
			break;
		case R.id.button2:
			feedManager.reset();
			feedManager.deleteCache();
			//listAdapter.notifyDataSetInvalidated();
			break;
		}
	}
}

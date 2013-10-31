package com.example.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView.OnChildClickListener;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/* För att använda en egen xml-layout - implementera en egen TabListener som 
	 * implements ActionBar.TabListener
	 */
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	PendingIntent backgroundUpdateIntent;
	static final long UPDATE_INTERVAL = 1800000; // 30 minutes
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		backgroundUpdateIntent = PendingIntent.getService(
				getApplicationContext(), 0, 
				new Intent(this, TimeAlarm.class), 0);

		stopBackgroundUpdates();

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setIcon(R.drawable.ic_launcher)
					.setCustomView(R.layout.tab)
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			
			//return FeedManager.getCorses().size();
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "All Courses";
			case 1:
				return "section 2";
			case 2:
				return "section 3";
			case 3:
				return "section 4";
			case 4:
				return "fifth really really long tab";
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment implements FeedManager.FeedManagerDoneListener, OnScrollListener, OnChildClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		static final String TAG = "MainActivity";
		
		
		ExpandableListAdapter listAdapter;
	    ExpandableListView expListView;
	    FeedManager feedManager;
		ProgressDialog dialog;
		ProgressBar progBar;
		TextView txProgress;
		View headerView;
		PendingIntent backgroundUpdateIntent;
	 


		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
//			dummyTextView.setText(Integer.toString(getArguments().getInt(
//					ARG_SECTION_NUMBER)));
			
			
			progBar = (ProgressBar) rootView.findViewById(R.id.progress);
			txProgress = (TextView) rootView.findViewById(R.id.txProgess);
			progBar.setVisibility(ProgressBar.GONE);
			txProgress.setVisibility(TextView.GONE);
			
			
		    // get the listview
	        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
	 
	        // preparing list data
	        //prepareListData();
	        headerView = getLayoutInflater(savedInstanceState).inflate(R.layout.itsl_list_header, null); //possible Error svaeInstanceState
			hideSettingsView();
			
			feedManager = new FeedManager(this, rootView.getContext()); //possible error getAplicationContext() -> getContext()
			
			listAdapter = new ExpandableListAdapter(this.getActivity(), feedManager.getArticles());
			expListView.addHeaderView(headerView);
			expListView.setAdapter(listAdapter);
			expListView.setOnScrollListener(this);
			expListView.setOnChildClickListener(this);
			
			//listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
	 
	        // setting list adapter
	        expListView.setAdapter(listAdapter);
	 	
	        feedManager.loadCache();
			
			/*
			 *  in case there is nothing in the cache, or it doesn't exist
			 *  we have to refresh
			 */
			if (feedManager.getArticles().isEmpty())
				refresh();
	        
	        
	        
			return rootView;
		}
		public void onFeedManagerProgress(FeedManager fm, int progress, int max)
		{
			// set up progress dialog if there isn't one
			if (dialog == null)
			{
				dialog = new ProgressDialog(this.getActivity());		//possible error getActivity;
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
		public void onFeedManagerDone(FeedManager fm, ArrayList<Article> articles)
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

			//Toast.makeText(getApplicationContext(), "" + articles.size() + " articles", Toast.LENGTH_LONG).show();
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

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
		{
			return parent.collapseGroup(groupPosition);
		}

		

		

		/*private void prepareListData() {
	        listDataHeader = new ArrayList<Article>();
	        listDataChild = new HashMap<Article, List<String>>();
	 
	        // Adding child data
	                
	        listDataHeader.add(new Article("New", "2013-10-15 10.30", "For this we will need your User stories and one or more of them will be put into the first sprint.","330A"));
	        listDataHeader.add(new Article("Not so new", "2013-10-14 10.30", "We will need your User stories and one or more of them will be put into the first sprint.","220B"));
	        listDataHeader.add(new Article("Old", "2013-10-13 10.30", "For this we will need your User stories.","330A"));
	        listDataHeader.add(new Article("Oldest", "2013-10-12 10.30", "For this we will need your User stories.","330A"));
	        
	        // Adding child data
	        List<String> top250 = new ArrayList<String>();
	        top250.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
	    
	        List<String> nowShowing = new ArrayList<String>();
	        //nowShowing.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
	        nowShowing.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
	        
	        List<String> comingSoon = new ArrayList<String>();
	    //    comingSoon.add("For this we will need your User stories and one or more of them will be put into the first sprint.");
	        comingSoon.add("For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint. For this we will need your User stories and oe or more of them will be put into the first sprint. For this we will need your User stories and one or more of them will be put into the first sprint.");
	        
	        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
	        listDataChild.put(listDataHeader.get(1), nowShowing);
	        listDataChild.put(listDataHeader.get(2), comingSoon);
	        listDataChild.put(listDataHeader.get(3), comingSoon);
	    }
*/
	}
	public void onDestroy()
	{
		super.onDestroy();
		startBackgroundUpdates();
	}
	private void startBackgroundUpdates()
	{
		//Log.i(TAG, "Setting up background updates");

		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, 
				System.currentTimeMillis() + UPDATE_INTERVAL, 
				UPDATE_INTERVAL, backgroundUpdateIntent);
	}
	
	private void stopBackgroundUpdates()
	{
		//Log.i(TAG, "Stopping background updates");

		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(backgroundUpdateIntent);
	}
	   

}

package se.mah.kd330a.project.itsl;

import se.mah.kd330a.project.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import se.mah.kd330a.project.itsl.SectionsPagerAdapter;
import se.mah.kd330a.project.itsl.FeedManager.FeedManagerDoneListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*
 * @author asampe, marcusmansson
 * 
 * 
 * HISTORY:
 * 
 * 0.8.1
 *  o added recent changes (notification, background updates, some rewrites, resources)
 *    from now on we will work in this project and not in our own
 *
 * 0.8.0
 *  o transfered files and resources to the framwork project, adapted code for
 *    fragment
 *   
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
 * TODO:
 *  o fix a better looking progressbar, until then use old progressdialog
 *  o seem to be a glitch when expanding, sometimes the child gets a black background 
 *    
 *    
 */
public class FragmentITSL extends Fragment implements FeedManagerDoneListener, OnClickListener, ActionBar.TabListener
{
	static final String TAG = "ITSL_fragment";
	static final long UPDATE_INTERVAL = 1000 * 120; //every other minute
	// 1800000 = 30 minutes
	SectionsPagerAdapter mSectionsPagerAdapter;
	FeedManager feedManager;
	ViewPager mViewPager;
	PendingIntent backgroundUpdateIntent;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		/*
		 * set up the repeating task of updating data in the background 
		 * (but stop it while the app is running)
		 */
		Context appContext = getActivity().getApplicationContext(); 
		backgroundUpdateIntent = PendingIntent.getService(
				appContext, 0, 
				new Intent(appContext, TimeAlarm.class), 0);

		
		feedManager = new FeedManager(this, appContext);
	}

	public void onPause()
	{
		super.onPause();
		
		Log.i(TAG, "Paused: Setting up background updates");

		AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, 
				System.currentTimeMillis() + UPDATE_INTERVAL, 
				UPDATE_INTERVAL, backgroundUpdateIntent);
		
		/*
		 * Remember when we last had this view opened 
		 */
		Util.setLatestUpdate(getActivity().getApplicationContext(), 
				new Date(System.currentTimeMillis()));
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

		/*
		 * custom ActionBar
		 */
		/*
		ColorDrawable colorDrawable = new ColorDrawable();
		colorDrawable.setColor(0xffeeeeee);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.itsl_abs_layout);
		*/
		
		/*
		 *  set up progressbar
		 */
		
		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getChildFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
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
					.setCustomView(R.layout.itsl_tab)
					.setTabListener(this));
		}
	
//		progBar = (ProgressBar) rootView.findViewById(R.id.progress);
//		txProgress = (TextView) rootView.findViewById(R.id.txProgess);
//		progBar.setVisibility(ProgressBar.GONE);
//		txProgress.setVisibility(TextView.GONE);
//		
//		/*
//		 *  create settings view and hide it
//		 */
//		headerView = inflater.inflate(R.layout.itsl_list_header, null);
//		headerView.findViewById(R.id.button1).setOnClickListener(this);
//		headerView.findViewById(R.id.button2).setOnClickListener(this);
//		hideSettingsView();
//
//		/*
//		 *  set up the listview
//		 */
//		listAdapter = new ExpandableListAdapter(getActivity(), feedManager.getArticles());
//		expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
//		expListView.addHeaderView(headerView);
//		expListView.setAdapter(listAdapter);
//		expListView.setOnScrollListener(this);
//		expListView.setOnChildClickListener(this);
//
//		feedManager.loadCache();
//
//		for (String url : Util.getBrowserBookmarks(getActivity().getApplicationContext()))
//		{
//			Log.i(TAG, "Got URL from bookmarks: " + url);
//			feedManager.addFeedURL(url);
//		}
//		
//		/*
//		 *  in case there is nothing in the cache, or it doesn't exist
//		 *  we have to refresh
//		 */
//		if (feedManager.getArticles().isEmpty())
//			refresh();
//		
//		
//		for (String title : getFeedObjects().keySet())
//		{
//			Log.i(TAG, "Filter list has key: " + title);
//		}
//
		return rootView;
	}

	public class FeedObject {
		public ArrayList<Article> articles;
		public FeedObject()
		{
			articles = new ArrayList<Article>();
		}
	}

	public HashMap<String, FeedObject> getFeedObjects() {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFeedManagerDone(FeedManager fm, ArrayList<Article> articles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFeedManagerProgress(FeedManager fm, int progress, int max) {
		// TODO Auto-generated method stub
		
	}
}
package se.mah.kd330a.project.itsl;

import se.mah.kd330a.project.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.SortedSet;

import se.mah.kd330a.project.itsl.FeedManager.FeedManagerDoneListener;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
	public SectionsPagerAdapter mSectionsPagerAdapter;
	FeedManager feedManager;
	ViewPager mViewPager;
	PendingIntent backgroundUpdateIntent;
	public HashMap<String, FeedObject> foList = new HashMap<String, FeedObject>();


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
		feedManager.loadCache();
		
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
		
		return rootView;
	}
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			
			super(fm);

		}
		@Override
		public int getItemPosition (Object object){
			return POSITION_NONE;
		}
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			ArrayList<String> keyList = new ArrayList<String>();
			for (String title : getFeedObjects().keySet())
			{
				keyList.add(title);
				Log.i(TAG, "Filter list has key: " + title);
			}
			
			Fragment fragment = new TabFragment();
			Bundle args = new Bundle();
			args.putInt(TabFragment.ARG_SECTION_NUMBER, position + 1);
			args.putString(TabFragment.ARG_COURSE_KEY, keyList.get(position));
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			
			if (getFeedObjects().keySet().size()==0){
				Log.i("Value", Integer.toString(getFeedObjects().keySet().size()));
				return 0;
				}
			else{
				Log.i("Value", Integer.toString(getFeedObjects().keySet().size()));
				return getFeedObjects().keySet().size();
			}
			
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
	public class FeedObject {
		public ArrayList<Article> articles;
		public FeedObject()
		{
			articles = new ArrayList<Article>();
		}
	}

	public HashMap<String, FeedObject> getFeedObjects() {
		FeedObject fo;
		Boolean changed = false;
		for (Article a : feedManager.getArticles())
		{
			
			if (foList.containsKey(a.getArticleCourseCode()))
			{
				fo = foList.get(a.getArticleCourseCode());
			}
			else
			{
				fo = new FeedObject();
				foList.put(a.getArticleCourseCode(), fo);
			}
			if(!fo.articles.contains(a)){
				fo.articles.add(a);
				changed = true;}
		}
		if(changed){
			Log.i(TAG, Integer.toString(foList.size()));
			mSectionsPagerAdapter.notifyDataSetChanged(); 
		}
		Log.i("foListSize", Integer.toString(foList.size()));
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
		//mSectionsPagerAdapter.startUpdate(mViewPager);
		//mSectionsPagerAdapter.finishUpdate(mViewPager);
		//mSectionsPagerAdapter.notifyDataSetChanged();
		getFeedObjects();

		
	}

	@Override
	public void onFeedManagerProgress(FeedManager fm, int progress, int max) {
		// TODO Auto-generated method stub
		
	}
}
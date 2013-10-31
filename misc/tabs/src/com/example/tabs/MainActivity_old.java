//package com.example.mad2013_itslearning;
//
//import java.util.ArrayList;
//import android.app.ActionBar;
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.ExpandableListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.Toast;
//
///*
// * @author asampe, marcusmansson
// * 
// * 
// * HISTORY:
// * 0.7.4
// *  o moved all file handling to FeedManager
// *  o code and comments cleaned up
// *   
// * 0.7.3
// *  o merged two branches (unknown version) with 0.7.2 
// *  o course colors in the UI 
// *  o fixed text overflow in the UI
// *  
// * 0.7.2 
// * 	o added saving/loading of articles to/from cache
// *  o load saved articles and initialize list on app start 
// * 	o removed unused code
// * 
// *  
// */
//
//public class MainActivity extends Activity implements FeedManager.FeedManagerDoneListener, OnScrollListener, OnChildClickListener
//{
//	static final String TAG = "MainActivity";
//	static final long UPDATE_INTERVAL = 1800000; // 30 minutes
//	
//	ExpandableListAdapter listAdapter;
//	ExpandableListView expListView;
//	FeedManager feedManager;
//	ProgressDialog dialog;
//	ProgressBar progBar;
//	TextView txProgress;
//	View headerView;
//	PendingIntent backgroundUpdateIntent;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		/*
//		 * set up the repeating task of updating data in the background 
//		 * (but stop it while the app is running)
//		 */
//		backgroundUpdateIntent = PendingIntent.getService(
//				getApplicationContext(), 0, 
//				new Intent(this, TimeAlarm.class), 0);
//
//		stopBackgroundUpdates();
//		
//		/*
//		 * custom ActionBar
//		 */
//		ColorDrawable colorDrawable = new ColorDrawable();
//		colorDrawable.setColor(0xffeeeeee);
//		ActionBar actionBar = getActionBar();
//		actionBar.setBackgroundDrawable(colorDrawable);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		actionBar.setCustomView(R.layout.abs_layout);
//
//		progBar = (ProgressBar) findViewById(R.id.progress);
//		txProgress = (TextView) findViewById(R.id.txProgess);
//		progBar.setVisibility(ProgressBar.GONE);
//		txProgress.setVisibility(TextView.GONE);
//
//		/*
//		 *  create settings view and hide it
//		 */
//		headerView = getLayoutInflater().inflate(R.layout.itsl_list_header, null);
//		hideSettingsView();
//
//		feedManager = new FeedManager(this, getApplicationContext());
//
//		/*
//		 *  set up the listview
//		 */
//		listAdapter = new ExpandableListAdapter(this, feedManager.getArticles());
//		expListView = (ExpandableListView) findViewById(R.id.lvExp);
//		expListView.addHeaderView(headerView);
//		expListView.setAdapter(listAdapter);
//		expListView.setOnScrollListener(this);
//		expListView.setOnChildClickListener(this);
//
//		feedManager.loadCache();
//		
//		/*
//		 *  in case there is nothing in the cache, or it doesn't exist
//		 *  we have to refresh
//		 */
//		if (feedManager.getArticles().isEmpty())
//			refresh();
//	}
//
//	public void onFeedManagerProgress(FeedManager fm, int progress, int max)
//	{
//		// set up progress dialog if there isn't one
//		if (dialog == null)
//		{
//			dialog = new ProgressDialog(this);
//			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			dialog.setMessage("Downloading...");
//			dialog.show();
//		}
//		dialog.setProgress(progress);
//		dialog.setMax(max);
//
//		/*
//		progBar.setProgress(progress);
//		progBar.setMax(max);
//		*/
//	}
//
//	@Override
//	public void onFeedManagerDone(FeedManager fm, ArrayList<Article> articles)
//	{
//		/*
//		 * display the data in our listview
//		 */
//		listAdapter.notifyDataSetInvalidated();
//
//		/*
//		progBar.setVisibility(ProgressBar.GONE);
//		txProgress.setVisibility(TextView.GONE);
//		*/
//		dialog.dismiss();
//		dialog = null;
//
//		Toast.makeText(getApplicationContext(), "" + articles.size() + " articles", Toast.LENGTH_LONG).show();
//	}
//
//	private void refresh()
//	{
//		/*
//		 * close all expanded childviews, otherwise they will incorrectly 
//		 * linger in the UI even after we invalidate the dataset
//		 */
//		int count = listAdapter.getGroupCount();
//		for (int i = 0; i < count; i++)
//			expListView.collapseGroup(i);
//
//		feedManager.reset();
//		feedManager.processFeeds();
//	}
//
//	public void refreshButtonClicked(View v)
//	{
//		refresh();
//		hideSettingsView();
//	}
//
//	public void clearAllData(View v)
//	{
//		feedManager.reset();
//		feedManager.deleteCache();
//		listAdapter.notifyDataSetInvalidated();
//	}
//
//	private void hideSettingsView()
//	{
//		headerView.findViewById(R.id.headerLayout).setVisibility(View.GONE);
//	}
//
//	private void showSettingsView()
//	{
//		headerView.findViewById(R.id.headerLayout).setVisibility(View.VISIBLE);
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
//	{
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState)
//	{
//		if (expListView.getFirstVisiblePosition() == 0 && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
//			showSettingsView();
//		else if (expListView.getFirstVisiblePosition() == 1 && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
//			hideSettingsView();
//	}
//
//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
//	{
//		return parent.collapseGroup(groupPosition);
//	}
//
//	
//	public void onDestroy()
//	{
//		super.onDestroy();
//		startBackgroundUpdates();
//	}
//	
//	private void startBackgroundUpdates()
//	{
//		Log.i(TAG, "Setting up background updates");
//
//		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		alarm.setRepeating(AlarmManager.RTC_WAKEUP, 
//				System.currentTimeMillis() + UPDATE_INTERVAL, 
//				UPDATE_INTERVAL, backgroundUpdateIntent);
//	}
//	
//	private void stopBackgroundUpdates()
//	{
//		Log.i(TAG, "Stopping background updates");
//
//		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		alarm.cancel(backgroundUpdateIntent);
//	}
//	
//}

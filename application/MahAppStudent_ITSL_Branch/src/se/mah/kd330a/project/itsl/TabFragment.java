package se.mah.kd330a.project.itsl;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView.OnChildClickListener;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.itsl.*;
import se.mah.kd330a.project.itsl.FragmentITSL.FeedObject;

public class TabFragment extends FragmentITSL implements FeedManager.FeedManagerDoneListener, OnScrollListener, OnChildClickListener, ActionBar.TabListener{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String ARG_COURSE_KEY = "course_key";
	static final String TAG = "MainActivity";
	public HashMap<String, FeedObject> foList2 = new HashMap<String, FeedObject>();
	
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
	ProgressDialog dialog;
	ProgressBar progBar;
	TextView txProgress;
	View headerView;
	PendingIntent backgroundUpdateIntent;
	
	public TabFragment() {
	}
	@Override
	public HashMap<String, FeedObject> getFeedObjects() {
		FeedObject fo = new FeedObject();
		for (Article a : feedManager.getArticles())
		{
			
			if (foList2.containsKey(a.getArticleCourseCode()))
			{
				fo = foList2.get(a.getArticleCourseCode());
			}
			else
			{
				fo = new FeedObject();
				foList2.put(a.getArticleCourseCode(), fo);
			}
				fo.articles.add(a);
		}
		return foList2;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.itsl_fragment_main,
				container, false);
		//TextView dummyTextView = (TextView) rootView
		//		.findViewById(R.id.section_label);
		
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
		
		//feedManager = new FeedManager(this, rootView.getContext()); //possible error getAplicationContext() -> getContext()
		Log.i(TAG, Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));
		Log.i(TAG, getArguments().getString(ARG_COURSE_KEY));

		listAdapter = new ExpandableListAdapter(this.getActivity(), getFeedObjects().get(getArguments().get(ARG_COURSE_KEY)).articles);
		
		expListView.addHeaderView(headerView);
		expListView.setAdapter(listAdapter);
		expListView.setOnScrollListener(this);
		expListView.setOnChildClickListener(this);
		
		//listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);

 	
        
		
		/*
		 *  in case there is nothing in the cache, or it doesn't exist
		 *  we have to refresh
		 */
		if (feedManager.getArticles().isEmpty())
			refresh();
        
        
        
		return rootView;
	}
	@Override
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

		/*
		progBar.setVisibility(ProgressBar.VISIBLE);
		txProgress.setVisibility(TextView.VISIBLE);
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

		Toast.makeText(getActivity(), "" + articles.size() + " articles", Toast.LENGTH_LONG).show();
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
			/*
			 * you could refresh content here directly instead of showing the header
			 */
			showSettingsView();
		else
			hideSettingsView();
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
			hideSettingsView();
			break;
		case R.id.button2:
			feedManager.reset();
			feedManager.deleteCache();
			listAdapter.notifyDataSetInvalidated();
			break;
		}
	}

	
}

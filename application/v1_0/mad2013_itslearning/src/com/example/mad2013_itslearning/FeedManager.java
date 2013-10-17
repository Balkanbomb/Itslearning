package com.example.mad2013_itslearning;

import java.util.ArrayList;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import android.util.Log;

/* @author marcusmansson
 * 
 * FeedManager is responsible for managing several feeds, fetching them sequentially 
 * and creating article objects of the items found.
 * 
 * FeedManager will return all articles to the registered FeedCompleteListener when done.
 * 
 * Usage:
 *  
 * FeedManager fm = new FeedManager(this);
 *  
 * fm.addFeedURL(url); // for all urls you want to process, then
 *  
 * fm.processFeeds();
 * 
 */
public class FeedManager implements FeedDownloadTask.FeedCompleteListener
{
	private final String TAG = "RSSTEST";
	private FeedDownloadTask downloadTask;
	private FeedManagerDoneListener callbackHandler;
	private ArrayList<Article> articleList;
	private ArrayList<String> feedList;
	private int feedQueueCounter;

	/*
	 * the listener must implement this method
	 */
	public interface FeedManagerDoneListener
	{
		public void onFeedManagerDone(ArrayList<Article> articles);
	}

	public FeedManager(FeedManagerDoneListener callbackHandler)
	{
		super();
		articleList = new ArrayList<Article>();
		feedList = new ArrayList<String>();
		feedQueueCounter = 0;
		
		try
		{
			this.callbackHandler = (FeedManagerDoneListener) callbackHandler;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(callbackHandler.toString() + " must implement FeedManagerDoneListener");
		}
	}

	public void addFeedURL(String url) // throws MalformedURLException
	{
		/*
		 *  even though we use simple strings, we should throw exception if the string is not 
		 *  a valid url for sake of problem finding
		try
		{
			URL test = new URL(url);
		}
		catch (MalformedURLException e)
		{
			throw e;
		}
		 */

		this.feedList.add(url);
	}

	public void removeFeedURL(String url)
	{
		this.feedList.remove(url);
	}
	
	public void reset() {
		feedQueueCounter = 0;
	}
	
	public ArrayList<Article> getArticles()
	{
		return articleList;
	}

	@Override
	public void onFeedComplete(RSSFeed feed)
	{
		if (downloadTask.hasException())
		{
			Log.e(TAG, downloadTask.getException().toString());
		}
		else
		{
			for (RSSItem rssItem : feed.getItems())
			{
				articleList.add(new Article(rssItem));
			}
		}
		
		if (feedQueueCounter < this.feedList.size())
		{
			// process next feed in queue
			processFeeds();
		}
		else
		{
			/*
			 *  reset counter
			 */
			feedQueueCounter = 0;
			
			/*
			 *  return the complete list of articles to the listener
			 */
			callbackHandler.onFeedManagerDone(getArticles());
		}
	}

	public void processFeeds()
	{
		if (this.feedList.isEmpty())
		{
			Log.e(TAG, "Feed list is empty, nothing to do!");
			return;
		}
		
		/* 
		 * there can only be one task at any time and it can only be used once
		 */
		downloadTask = new FeedDownloadTask(this);
		
		/* 
		 * we want to process the next url in queue but not pop it from the queue,
		 * in case we want to get all feeds again later (i.e. to refresh), that's
		 * why we use a counter to keep track of where in the queue we are 
		 */
		downloadTask.execute(feedList.get(feedQueueCounter++));
	}
}

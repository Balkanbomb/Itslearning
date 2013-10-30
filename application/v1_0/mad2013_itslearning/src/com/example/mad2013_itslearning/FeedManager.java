package com.example.mad2013_itslearning;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author marcusmansson
 * 
 *         FeedManager is responsible for managing several feeds, fetching them
 *         sequentially and creating article objects of the items found.
 * 
 *         FeedManager will return all articles to the registered
 *         FeedCompleteListener when done.
 * 
 *         Usage:
 * 
 *         FeedManager fm = new FeedManager(this, this);
 * 
 *         fm.addFeedURL(url); // for all urls you want to process, then
 * 
 *         fm.processFeeds();
 * 
 */
public class FeedManager implements FeedDownloadTask.FeedCompleteListener
{
	private final String TAG = "FeedManager";
	private final String CACHE_FILENAME = "article_cache.ser";
	private final String PREFS_NAME = "data_storage.dat";
	private FeedDownloadTask downloadTask;
	private FeedManagerDoneListener callbackHandler;
	private ArrayList<Article> articleList;
	private ArrayList<String> feedList;
	private int feedQueueCounter;
	private Context appContext;
	private Date latestUpdate;

	/*
	 * the listener must implement these methods
	 */
	public interface FeedManagerDoneListener
	{
		public void onFeedManagerDone(ArrayList<Article> articles);

		public void onFeedManagerProgress(int progress, int max);
	}

	public FeedManager(FeedManagerDoneListener callbackHandler, Context context)
	{
		appContext = context;
		articleList = new ArrayList<Article>();
		feedList = new ArrayList<String>();
		feedQueueCounter = 0;

		try
		{
			this.callbackHandler = (FeedManagerDoneListener) callbackHandler;

			// Restore preferences
			SharedPreferences settings = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
			latestUpdate = new Date(settings.getLong("latestUpdate", 0));
			latestUpdate = new Date(1382932800000L);
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(callbackHandler.toString() + " must implement FeedManagerDoneListener");
		}

		/*
		 *  check for cached content
		 */
		if (appContext.getFileStreamPath(CACHE_FILENAME).exists())
		{
			try
			{
				loadCache();
			}
			catch (Exception e)
			{
				Log.e(TAG, e.toString());

				/*
				 *  something is probably wrong with the cache file so let's delete it
				 */
				appContext.getFileStreamPath(CACHE_FILENAME).delete();
			}

		}
	}

	public void addFeedURL(String url) // throws MalformedURLException
	{
		/*
		 *  even though we use simple strings, we should throw  
		 *  exception if the string is not a valid url for sake 
		 *  of finding errors
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

	public int queueSize()
	{
		return feedList.size();
	}

	public void removeFeedURL(String url)
	{
		feedList.remove(url);
	}

	/**
	 * prepare for re-processing of feeds list; clears article list and resets
	 * feed queue
	 * 
	 */
	public void reset()
	{
		feedQueueCounter = 0;
		articleList.clear();
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
			Article article;
			String feedDescription = feed.getTitle();

			for (RSSItem rssItem : feed.getItems())
			{
				article = new Article(rssItem);
				articleList.add(article);
				article.setArticleCourseCode(feedDescription);
			}
		}

		if (feedQueueCounter < this.feedList.size())
		{
			/*
			 *  process next feed in queue
			 */
			processFeeds();
		}
		else
		{
			/*
			 * all feeds have been read so let's reset counter. 
			 * this way it's possible to call processFeeds() 
			 * again if we just want to refresh articleList.
			 */
			feedQueueCounter = 0;

			/*
			 * sorts the list by date in descending order
			 */
			Collections.sort(articleList);

			/*
			 * save data
			 */
			try
			{
				saveCache();

				SharedPreferences settings = appContext.getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putLong("latestUpdate", articleList.get(0).getArticlePubDate().getTime());
				editor.commit();
				
				ArrayList<Article> newArticles = new ArrayList<Article>();
				for (Article a : articleList)
				{
					if (a.getArticlePubDate().compareTo(this.latestUpdate) > 0)
					{
						newArticles.add(a);
						Log.i(TAG, a.getArticleDate() + " is newer than " + this.latestUpdate.toString());
					}
				}
				
				Log.i(TAG, newArticles.size() + " new articles, that will be sent to notification class");
				
			}
			catch (Exception e)
			{
				Log.e(TAG, e.toString());
			}

			/*
			 *  return the complete list of articles to the listener
			 *  when all items in the feed queue are processed
			 */
			callbackHandler.onFeedManagerDone(getArticles());
		}
	}

	/**
	 * downloads articles from one feed at a time, you must add feeds using
	 * addFeedURL(String url) before calling this method
	 */
	public void processFeeds()
	{
		if (this.feedList.isEmpty())
		{
			Log.e(TAG, "Feed list is empty, nothing to do!");
			return;
		}

		/*
		 * notify the UI of update
		 */
		callbackHandler.onFeedManagerProgress(feedQueueCounter + 1, feedList.size());

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

	private void saveCache() throws Exception
	{
		/*
		 * don't overwrite saved data with nothing 
		 */
		if (!articleList.isEmpty())
		{
			FileOutputStream fos = appContext.openFileOutput(CACHE_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(articleList);
			fos.close();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadCache() throws Exception
	{
		FileInputStream fis = appContext.openFileInput(CACHE_FILENAME);
		ObjectInputStream ois = new ObjectInputStream(fis);
		articleList.clear();
		articleList.addAll((List<Article>) ois.readObject());
		fis.close();
	}

	public void deleteCache()
	{
		appContext.getFileStreamPath(CACHE_FILENAME).delete();
	}

}

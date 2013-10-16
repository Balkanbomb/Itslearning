package com.example.mad2013_itslearning;

import java.util.LinkedList;
import java.util.List;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;

public class FeedDownloadTask extends AsyncTask<String, Void, List<RSSItem>> {

	private final String TAG = "RSSTEST";
	private FeedCompleteListener contextListener;

	/* 
	 * the calling class must implement this method
	 */
	public interface FeedCompleteListener {
		public void onFeedComplete(List<RSSItem> list);
	}

	public FeedDownloadTask(Context context) {
		super();
		
		/*
		 * in order to handle callbacks, we require that the calling class
		 * implements the interface FeedCompleteListener, otherwise an
		 * Exception is thrown
		 */
		try {
			this.contextListener = (FeedCompleteListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement FeedCompleteListener");
		}
	}

	protected List<RSSItem> doInBackground(String... url) {
		List<RSSItem> list = new LinkedList<RSSItem>();
		RSSReader reader = new RSSReader();
		String uri = url[0];

		try {
			RSSFeed feed = reader.load(uri);
			list.addAll(feed.getItems());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			// release resources
			reader.close();
		}

		return list;
	}

	protected void onPostExecute(List<RSSItem> list) {
		/*
		 * pass the results back to the caller
		 * 
		 */
		contextListener.onFeedComplete(list);
	}

}

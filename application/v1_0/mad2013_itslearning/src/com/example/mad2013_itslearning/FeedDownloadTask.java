package com.example.mad2013_itslearning;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;
import android.os.AsyncTask;

public class FeedDownloadTask extends AsyncTask<String, Void, RSSFeed> {

	private final String TAG = "RSSTEST";
	private FeedCompleteListener callbackHandler;
	private Exception exception;
		
	/* 
	 * the calling class must implement this method
	 */
	public interface FeedCompleteListener {
		public void onFeedComplete(RSSFeed feed);
	}

	public FeedDownloadTask(FeedCompleteListener callbackHandler) {
		super();
		
		this.exception = null;
		
		/*
		 * in order to handle callbacks, we require that the calling class
		 * implements the interface FeedCompleteListener, otherwise an
		 * Exception is thrown
		 */
		try {
			this.callbackHandler = (FeedCompleteListener) callbackHandler;
		} catch (ClassCastException e) {
			throw new ClassCastException(callbackHandler.toString()
					+ " must implement FeedCompleteListener");
		}
	}

	protected RSSFeed doInBackground(String... feedUrl) {
		RSSReader reader = new RSSReader();
		RSSFeed feed = null;
		String url = feedUrl[0];

		try {
			/* 
			 * get the feed
			 */
			feed = reader.load(url);
		} catch (Exception e) {
			/*
			 * if that fails, save the exception
			 */
			exception = e;
		} finally {
			/*
			 * always release resources
			 */
			
			reader.close();
		}

		/* 
		 * might be null
		 */
		return feed; 
	}

	protected void onPostExecute(RSSFeed feed) {
		/*
		 * pass the result back to the caller
		 * 
		 */
		callbackHandler.onFeedComplete(feed);
	}

	public boolean hasException()
	{
		return exception != null;
	}
	
	public Exception getException()
	{
		return exception;
	}

}

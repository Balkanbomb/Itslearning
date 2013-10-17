package com.example.mad2013_itslearning;

import java.util.ArrayList;
import java.util.Date;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import android.app.Activity;
import android.util.Log;
import android.view.View;

public class FeedManager implements
		FeedDownloadTask.FeedCompleteListener {
	
	private final String TAG = "RSSTEST";
	private String url;
	private FeedDownloadTask downloadTask;
	private ArrayList<Article> articleList;

	public FeedManager ()
	{
		super();
		articleList = new ArrayList<Article>();
		url = "https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE";
	}
	
	public void setFeedURL(String url) {
		this.url = url;
	}
	
	public ArrayList<Article> getArticles()
	{
		return this.articleList;
	}
	
	@Override
	public void onFeedComplete(RSSFeed feed) {
		
		if (downloadTask.hasException()) {
			Log.e(TAG, downloadTask.getException().toString());
		} else {
			for (RSSItem rssItem : feed.getItems()) {
				articleList.add(new Article(rssItem));
			}

		}
	}

	public void doRefresh() {
		downloadTask = new FeedDownloadTask(this);
		downloadTask.execute(url);
	}
}

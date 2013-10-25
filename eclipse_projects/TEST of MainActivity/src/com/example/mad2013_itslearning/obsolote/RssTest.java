package com.example.mad2013_itslearning.obsolote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;

import com.example.mad2013_itslearning.Article;
import com.example.mad2013_itslearning.FeedDownloadTask;
import com.example.mad2013_itslearning.R;
import com.example.mad2013_itslearning.FeedDownloadTask.FeedCompleteListener;
import com.example.mad2013_itslearning.R.id;
import com.example.mad2013_itslearning.R.layout;
import com.example.mad2013_itslearning.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 * @author marcusmansson
 */
public class RssTest extends Activity implements
		FeedDownloadTask.FeedCompleteListener {

	private final String TAG = "RSSTEST";
	private final String url = "https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE";
	private FeedDownloadTask downloadTask;
	private long time1, time2, time3, time4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rss_test, menu);
		return true;
	}

	@Override
	public void onFeedComplete(RSSFeed feed) {
		time2 = new Date().getTime() - time1;

		if (downloadTask.hasException()) {
			Log.e(TAG, downloadTask.getException().toString());
		} else {
			List<RSSItem> list = feed.getItems();
			ArrayList<Article> articleList = new ArrayList<Article>();

			for (RSSItem i : list) {
				articleList.add(new Article(i));
			}
			
			time3 = new Date().getTime() - time1;

			// put the articlelist in a listview, just for show
			try {
				ListView view = (ListView) findViewById(R.id.listView1);
				view.setScrollingCacheEnabled(false);
				ArrayAdapter<Article> adapter = new ArrayAdapter<Article>(this,
						android.R.layout.simple_list_item_1, articleList);
				view.setAdapter(adapter);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			time4 = new Date().getTime() - time1;
			
		}
		
		Log.e(TAG, "times: " + String.format("rss: %d ms | articles: %d ms | adapter: %d ms", time2, time3, time4));
	}

	public void doRefresh(View v) {
		time1 = new Date().getTime();
		downloadTask = new FeedDownloadTask(this);
		downloadTask.execute(url);
	}
}

package com.example.mad2013_itslearning;

import java.util.List;
import org.mcsoxford.rss.RSSItem;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RssTest extends Activity implements
		FeedDownloadTask.FeedCompleteListener {

	private final String TAG = "RSSTEST";
	private final String url = "https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=25776&CustomerId=719&Guid=d50eaf8a1781e4c8c7cdc9086d1248b1&Culture=sv-SE";
	private FeedDownloadTask downloadTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss_test);
		
		downloadTask = new FeedDownloadTask(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rss_test, menu);
		return true;
	}

	@Override
	public void onFeedComplete(List<RSSItem> list) {
		if (!list.isEmpty()) {
			try {
				ListView view = (ListView) findViewById(R.id.listView1);
				ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(this,
						android.R.layout.simple_list_item_1, list);
				view.setAdapter(adapter);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}
	
	public void doRefresh(View v)
	{
		downloadTask.execute(url);
	}
}

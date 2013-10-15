package com.td.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.td.rssreader.parser.DOMParser;
import com.td.rssreader.parser.RSSFeed;

public class SplashActivity extends Activity {

	private String RSSFEEDURL = "https://mah.itslearning.com/Bulletin/RssFeed.aspx?LocationType=1&LocationID=18178&PersonId=72327&CustomerId=719&Guid=30fe1d327a3189ed6b9a94616608ac03&Culture=sv-SE";
	RSSFeed feed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() == null
				&& !conMgr.getActiveNetworkInfo().isConnected()
				&& !conMgr.getActiveNetworkInfo().isAvailable()) {
			// No connectivity - Show alert
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Unable to reach server, \nPlease check your connectivity.")
					.setTitle("Itslearning")
					.setCancelable(false)
					.setPositiveButton("Exit",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();

		} else {
			// Connected - Start parsing
			new AsyncLoadXMLFeed().execute();

		}

	}

	private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			// Obtain feed
			DOMParser myParser = new DOMParser();
			feed = myParser.parseXml(RSSFEEDURL);
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Bundle bundle = new Bundle();
			bundle.putSerializable("feed", feed);

			// launch List activity
			Intent intent = new Intent(SplashActivity.this, ListActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);

			// kill this activity
			finish();
		}

	}

}

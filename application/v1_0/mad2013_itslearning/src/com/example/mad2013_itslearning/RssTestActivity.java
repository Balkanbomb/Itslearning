package com.example.mad2013_itslearning;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RssTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rss_test, menu);
		return true;
	}

}

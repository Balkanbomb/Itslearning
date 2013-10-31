package com.example.mad2013_itslearning;

import java.util.Date;
import android.content.Context;
import android.content.SharedPreferences;

public class Util
{
	private static final String PREFS_NAME = "data_storage.dat";

	public static Date getLatestUpdate(Context context)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Date latestUpdate = new Date(settings.getLong("latestUpdate", 0));
		latestUpdate = new Date(1382932800000L);
		return latestUpdate; 
	}
	
	public static void setLatestUpdate(Context context, Date value)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("latestUpdate", value.getTime());
		editor.commit();
	}
}

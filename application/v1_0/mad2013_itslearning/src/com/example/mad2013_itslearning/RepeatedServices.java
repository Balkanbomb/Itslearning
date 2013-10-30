package com.example.mad2013_itslearning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RepeatedServices extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.e("AlarmReciever", "Trying to start service TimeAlarm");
		/*
		Intent i = new Intent(context, TimeAlarm.class);
		context.startService(i);
		*/
	}

}

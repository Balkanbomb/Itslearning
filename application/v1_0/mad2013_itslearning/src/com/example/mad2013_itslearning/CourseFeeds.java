package com.example.mad2013_itslearning;

import java.util.Calendar;

public class CourseFeeds {
	private int days;
	private Feed changesFeed;
	private Feed buletinFeed;
	private Calendar lastDisplayDate;
	

	public void setDays(int days) {
		this.days = days;
	}


	public Calendar setLastDisplayDate(int days){
		Calendar currentDate = Calendar.getInstance();
		lastDisplayDate.setTimeInMillis(currentDate.getTimeInMillis()-(days*24*60*60*1000));
		return lastDisplayDate;
	}
}

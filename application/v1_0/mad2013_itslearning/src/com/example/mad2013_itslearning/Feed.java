package com.example.mad2013_itslearning;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;


public abstract class Feed {
	private URL rssLink;
	private Boolean notify;
	private ArrayList<Article> articleList;
	private Boolean filter;
	private Course course;
	
	
	public void setFilter(Boolean filter) {
		this.filter = filter;
	}
	
	public ArrayList<Article> getArticleList(Calendar lastDisplayDate) {
		ArrayList<Article> articleListDisplayed = new ArrayList<Article>();
		if (filter){
			int length=articleList.size();
			for (int i=0;i<length;i++){
				if(articleList.get(i).getDate().after(lastDisplayDate.getTime())){
					articleListDisplayed.add(articleList.get(i));
				}
			}
		}
		return articleListDisplayed;
		
	}

	public void setRssLink (URL rssLink){
		this.rssLink=rssLink;
	}
	public void setRssLink (URL rssLink, String value){
		//URL.s
		//this.rssLink=URLEncoder.encode(value+rssLink.toString(), "utf-8");
	}
}

package com.example.mad2013_itslearning;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


public abstract class Feed {
	private URL rssLink;
	private Boolean notify;
	private ArrayList<Article> articleList;
	
	
	public Boolean getNotify() {
		return notify;
	}

	public void setNotify(Boolean notify) {
		this.notify = notify;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setArticleList(ArrayList<Article> articleList) {
		this.articleList = articleList;
	}

	
	public ArrayList<Article> getArticleList(Calendar lastDisplayDate, Boolean filter) {
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
	public void setRssLink (URL rssLink, String value) throws MalformedURLException{
		URL url = new URL(value+rssLink.toString());
		this.rssLink=url;
	}

	public URL getRssLink() {
		return rssLink;
	}
	
}

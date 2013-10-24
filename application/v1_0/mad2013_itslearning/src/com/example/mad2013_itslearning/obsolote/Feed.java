package com.example.mad2013_itslearning.obsolote;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;



public abstract class Feed {
	private URL rssLink;
	private Boolean notify;
	private ArrayList<ArticleOld> articleList;
	
	
	public Boolean getNotify() {
		return notify;
	}

	public void setNotify(Boolean notify) {
		this.notify = notify;
	}

	public void setArticleList(ArrayList<ArticleOld> articleList) {
		this.articleList = articleList;
	}

	
	public ArrayList<ArticleOld> getArticleList(Calendar lastDisplayDate, Boolean filter) {
		ArrayList<ArticleOld> articleListDisplayed = new ArrayList<ArticleOld>();
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

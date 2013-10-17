package com.example.mad2013_itslearning;

import java.util.Date;

import org.mcsoxford.rss.RSSItem;

public class Article implements Comparable<Article> {
	private String articleCourseCode;
	private RSSItem rssItem;
	private boolean textVisible;
	
	public Article(RSSItem item) {
		super();
		this.rssItem = item;
		this.textVisible = true;
	}
	
	@Override
	public int compareTo(Article another) {
		return this.rssItem.getPubDate().compareTo(another.getArticleDate());
	}

	public String getArticleHeader() {
		return this.rssItem.getTitle();
	}
	
	public Date getArticleDate() {
		return this.rssItem.getPubDate();
	}

	public String getArticleText() {
		return android.text.Html.fromHtml(this.rssItem.getDescription()).toString();
	}
	
	public boolean isTextVisible() {
		return textVisible;
	}
	
	public void setTextVisible(boolean textVisible) {
		this.textVisible = textVisible;
	}
	
	public String getArticleCourseCode() {
		return articleCourseCode;
	}
	
	public void setArticleCourseCode(String articleCourseCode) {
		this.articleCourseCode = articleCourseCode;
	}
	
	public String toString() {
		return this.getArticleHeader() + "\n-----------\n" + this.getArticleDate().toString() + "\n-----------\n" + this.getArticleText();
	}
}

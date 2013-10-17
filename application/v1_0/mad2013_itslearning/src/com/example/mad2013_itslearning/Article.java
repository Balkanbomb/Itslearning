package com.example.mad2013_itslearning;

import java.util.Date;

import org.mcsoxford.rss.RSSItem;

public class Article implements Comparable<Article> {
	private String articleCourseCode;
	private RSSItem rssItem;
	private boolean textVisible;

	String articleHeader;
	String articleDate;
	String articleText;
	
	public Article(RSSItem item) {
		super();
		this.rssItem = item;
		this.textVisible = true;
	}
	
	public Article(String articleHeader, String articleDate, String articleText, String courseCode) {
		super();
		this.articleHeader = articleHeader;
		this.articleDate = articleDate;
		this.articleText = articleText;
		this.articleCourseCode = courseCode;
		this.textVisible = true;
		this.rssItem = null;
	}

	@Override
	public int compareTo(Article another) {
		//return this.rssItem.getPubDate().compareTo(another.getArticleDate());
		return 0;
	}

	public String getArticleHeader() {
		if (this.rssItem == null)
			return this.articleHeader;
		else
			return this.rssItem.getTitle();
	}
	
	public String getArticleDate() {
		if (this.rssItem == null)
			return this.articleDate;
		else
			return this.rssItem.getPubDate().toString();
	}

	public String getArticleText() {
		if (this.rssItem == null)
			return this.articleText;
		else
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

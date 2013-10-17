package com.example.mad2013_itslearning;

import java.util.Date;

public class Article {
	String articleHeader;
	String articleDate;
	String articleText;
	String articleCourseCode;
	boolean textVisible;
	//RSSItem item;
	
	public Article(String articleHeader, String articleDate, String articleText) {
		super();
		this.articleHeader = articleHeader;
		this.articleDate = articleDate;
		this.articleText = articleText;
		this.textVisible = true;
	}
	public String getArticleHeader() {
		return articleHeader;
	}
	public void setArticleHeader(String articleHeader) {
		this.articleHeader = articleHeader;
	}
	public String getArticleDate() {
		return articleDate;
	}
	public void setArticleDate(String articleDate) {
		this.articleDate = articleDate;
	}
	public String getArticleText() {
		return articleText;
	}
	public void setArticleText(String articleText) {
		this.articleText = articleText;
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
	
	

}

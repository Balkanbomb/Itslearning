package com.example.mad2013_itslearning.obsolote;

import java.util.Date;

import org.mcsoxford.rss.RSSItem;

/* @author asampe
 * @author marcusmansson
 * 
 * this is a wrapper class for RSSItem, adding features such as making the 
 * articles sortable, and method for getting a summary text for large content 
 * 
 * 
 */
public class Article implements Comparable<Article>
{
	private String articleCourseCode;
	private RSSItem rssItem;
	private boolean textVisible;
	private static final int maxSummaryLength = 80;
	private  String articleHeader;
	private String articleDate;
	private String articleText;

	/*
	 * use this constructor
	 */
	public Article(RSSItem item)
	{
		super();
		this.rssItem = item;
		this.articleCourseCode = "T3ST";
		this.textVisible = true;
	}

	/*
	 * deprecated test constructor
	 */
	public Article(String articleHeader, String articleDate, String articleText, String courseCode)
	{
		super();
		this.articleHeader = articleHeader;
		this.articleDate = articleDate;
		this.articleText = articleText;
		this.articleCourseCode = courseCode;
		this.textVisible = true;
		this.rssItem = null;
	}

	@Override
	public int compareTo(Article another)
	{
		return another.getArticlePubDate().compareTo(rssItem.getPubDate());
	}

	public String getArticleHeader()
	{
		if (this.rssItem == null)
			return this.articleHeader;
		else
			return this.rssItem.getTitle();
	}

	public String getArticleDate()
	{
		if (this.rssItem == null)
			return this.articleDate;
		else
			return this.rssItem.getPubDate().toString();
	}

	public Date getArticlePubDate()
	{
		return this.rssItem.getPubDate();
	}

	public String getArticleText()
	{
		if (this.rssItem == null)
			return this.articleText;
		else
			return android.text.Html.fromHtml(this.rssItem.getDescription()).toString();
	}

	public  String getArticleSummary()
	{
		String summary = this.getArticleText();
		
		if (summary.length() > maxSummaryLength)
		{
			return summary.substring(0, maxSummaryLength) + "...";
		}
		else
		{
			return summary;
		}
	}

	public boolean isTextVisible()
	{
		return textVisible;
	}

	public void setTextVisible(boolean textVisible)
	{
		this.textVisible = textVisible;
	}

	public String getArticleCourseCode()
	{
		return articleCourseCode;
	}

	public void setArticleCourseCode(String articleCourseCode)
	{
		this.articleCourseCode = articleCourseCode;
	}

	public String toString()
	{
		return this.getArticleHeader() + "\n-----------\n" + this.getArticleDate().toString() + "\n-----------\n" + this.getArticleSummary();
	}
}

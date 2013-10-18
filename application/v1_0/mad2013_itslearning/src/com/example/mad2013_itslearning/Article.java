package com.example.mad2013_itslearning;

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

	/*
	 * use this constructor
	 */
	public Article(RSSItem item)
	{
		super();
		rssItem = item;
		articleCourseCode = "TEST";
		textVisible = true;
	}

	@Override
	public int compareTo(Article another)
	{
		return another.getArticlePubDate().compareTo(rssItem.getPubDate());
	}

	public String getArticleHeader()
	{
		return rssItem.getTitle();
	}

	public String getArticleDate()
	{
		return rssItem.getPubDate().toString();
	}

	public Date getArticlePubDate()
	{
		return rssItem.getPubDate();
	}

	public String getArticleText()
	{
		return android.text.Html.fromHtml(rssItem.getDescription()).toString();
	}

	public String getArticleSummary()
	{
		String summary = getArticleText();
		
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

	public void setTextVisible(boolean visible)
	{
		textVisible = visible;
	}

	public String getArticleCourseCode()
	{
		return articleCourseCode;
	}

	public void setArticleCourseCode(String courseCode)
	{
		articleCourseCode = courseCode;
	}

	public String toString()
	{
		return getArticleHeader();
	}
}

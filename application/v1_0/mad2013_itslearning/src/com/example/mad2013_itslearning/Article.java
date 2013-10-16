package com.example.mad2013_itslearning;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Article implements Comparable<Article>{
	private String title;
	private URL link;
	private String description;
	private Date date;
	private String author;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public URL getLink() {
		return link;
	}
	public void setLink(String link) {
		try {
			this.link = new URL(link);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	@Override
	public int compareTo(Article another) {
		return this.date.compareTo(another.date);
	}
	
	public String toString() {
		return title + "\n-----------\n" + date.toString() + "\n-----------\n" + description;
	}
}

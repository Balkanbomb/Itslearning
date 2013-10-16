package com.example.mad2013_itslearning;

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
	public void setLink(URL link) {
		this.link = link;
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
}

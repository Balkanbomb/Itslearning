package com.example.mad2013_itslearning;

import android.graphics.Color;

public class Course {
	private Color color;
	private String id;
	private Boolean filter;
	
	public Boolean getFilter() {
		return filter;
	}
	public void setFilter(Boolean filter) {
		this.filter = filter;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}

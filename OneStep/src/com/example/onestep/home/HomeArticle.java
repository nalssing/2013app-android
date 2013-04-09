package com.example.onestep.home;

public class HomeArticle {
	public static enum Type {
		SECTION_HEADER,
		SINGLE_LINE,
		SECONDARY_LINE
	}
	private Type type;
	private String title;
	private String time;
	private String writer;
	private int hit;
	public HomeArticle(Type type, String title, String time, String writer,
			int hit) {
		super();
		this.type = type;
		this.title = title;
		this.time = time;
		this.writer = writer;
		this.hit = hit;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	
}

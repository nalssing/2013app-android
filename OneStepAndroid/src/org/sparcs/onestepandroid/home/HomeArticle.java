package org.sparcs.onestepandroid.home;

public class HomeArticle {
	
	public static enum Type {
		SECTION_HEADER,
		SINGLE_LINE,
		POLICY_LINE,
		NOTICE_LINE,
		EVENT_LINE
	}
	private Type type;
	private String title;
	private String time;
	private String writer;
	private int hit;
	private int articleid;
	
	public HomeArticle(Type type, String title, String time, String writer,
			int hit, int id) {
		super();
		this.type = type;
		this.title = title;
		this.time = time;
		this.writer = writer;
		this.hit = hit;
		this.articleid = id;
	}
	public int getArticleid() {
		return articleid;
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

package com.example.onestep.noti;

public class NotiItem {
	private String title;
	
	private String message;
	private String boardname;
	private int articleID;
	private int id;
	private String type;
	private String when;
	private int checked;
	public NotiItem(int id, String title, String message, String boardname, int articleID,
			String type, String when, int checked) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
		this.boardname = boardname;
		this.articleID = articleID;
		this.type = type;
		this.when = when;
		this.checked = checked;
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBoardname() {
		return boardname;
	}
	public void setBoardname(String boardname) {
		this.boardname = boardname;
	}
	public int getArticleID() {
		return articleID;
	}
	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	
}

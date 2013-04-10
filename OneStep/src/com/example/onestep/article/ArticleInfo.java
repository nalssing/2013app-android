package com.example.onestep.article;

public class ArticleInfo {
	private int id;
	private String title;
	private String writer;
	private Boolean read;
	private String board;
	private String content;
	private int vote_up;
	private int vote_down;
	private String time;
	private int read_count;
	private int referer;
	public enum Type {
		ARTICLE,
		REPLY
	};
	private Type type;
	public ArticleInfo() {
		this.id = 0;
		this.title = "";
		this.writer = "";
		this.read = false;
		this.board = "";
		this.content ="";
		this.vote_up = 0;
		this.vote_down = 0;
		this.time = "";
		this.read_count = 0;
		this.referer = 0;
	}

	public ArticleInfo(int id, String title, String writer, String board, String content,
			String time, int read_count) {
		super();
		this.id = id;
		this.title = title;
		this.writer = writer;
		this.time = time;
		this.read_count = read_count;
		this.board = board;
		this.content=content;
	}
	public ArticleInfo(int id, String title, String writer,Boolean read, String board, String content,
			int vote_up, int vote_down, String time, int read_count) {
		super();
		this.id = id;
		this.title = title;
		this.writer = writer;
		this.time = time;
		this.read_count = read_count;
		this.read = read;
		this.board = board;
		this.content=content;
		this.vote_up = vote_up;
		this.vote_down = vote_down;
	}
	public ArticleInfo(int id, String title, String writer,Boolean read, String board, String content,
			int vote_up, int vote_down, String time, int read_count,int referer) {
		super();
		this.id = id;
		this.title = title;
		this.writer = writer;
		this.time = time;
		this.read_count = read_count;
		this.read = read;
		this.board = board;
		this.content=content;
		this.vote_up = vote_up;
		this.vote_down = vote_down;
		this.referer = referer;
	}
	public boolean getRead() {
		return this.read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getReadCount() {
		return read_count;
	}
	public void setReadCount(int read_count) {
		this.read_count = read_count;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBoard() {
		return this.board;
	}
	public void setBoard(String board) {
		this.board = board;
	}
	public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getVoteUp() {
		return this.vote_up;
	}
	public void setVoteUp(int vote_up) {
		this.vote_up = vote_up;
	}
	public int getVoteDown() {
		return this.vote_down;
	}
	public void setVoteDown(int vote_down) {
		this.vote_down = vote_down;
	}
	public int getReferer() {
		return this.referer;
	}
	public void setReferer(int referer) {
		this.referer = referer;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}

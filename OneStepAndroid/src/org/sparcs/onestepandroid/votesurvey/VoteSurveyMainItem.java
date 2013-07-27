package org.sparcs.onestepandroid.votesurvey;

public class VoteSurveyMainItem {
	private int id;
	private String title;
	private String type;
	private Boolean is_done;
	private Boolean is_closed;
	private String create_time;
	private String expire_time;
	
	public VoteSurveyMainItem() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIs_done() {
		return is_done;
	}

	public void setIs_done(Boolean is_done) {
		this.is_done = is_done;
	}

	public Boolean getIs_closed() {
		return is_closed;
	}

	public void setIs_closed(Boolean is_closed) {
		this.is_closed = is_closed;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}

	
}

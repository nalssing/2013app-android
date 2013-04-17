package org.sparcs.onestepandroid.article;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleListInfo implements Parcelable{
	private String title;
	private String writer;
	private String time;
	private int hit;
	private int id;
	private boolean isEndNoti;

	public ArticleListInfo() {
		this.title = "";
		this.writer = "";
		this.time = "";
		this.hit = 0;
		this.id = 0;
		this.isEndNoti = true;
	}
	public ArticleListInfo(int id, String title, String writer, String time, int hit,
			boolean isEndNoti) {
		super();
		this.title = title;
		this.writer = writer;
		this.time = time;
		this.hit = hit;
		this.id = id;
		this.isEndNoti = isEndNoti;
	}
	public ArticleListInfo(int id, String title, String writer, String time, int hit) {
		super();
		this.title = title;
		this.writer = writer;
		this.time = time;
		this.hit = hit;
		this.id = id;
		this.isEndNoti = false;
	}
	public ArticleListInfo(Parcel in) {
		this.title = in.readString();
		this.writer = in.readString();
		this.time = in.readString();
		this.hit = in.readInt();
		this.id = in.readInt();
		boolean[] a = new boolean[1];
		in.readBooleanArray(a);
		this.isEndNoti = a[0];

	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isEndNoti() {
		return isEndNoti;
	}
	public void setEndNoti(boolean isEndNoti) {
		this.isEndNoti = isEndNoti;
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
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeString(title);
		out.writeString(writer);
		out.writeString(time);
		out.writeInt(hit);
		out.writeInt(id);
		out.writeBooleanArray(new boolean[] {isEndNoti});


	}
	public static final Parcelable.Creator<ArticleListInfo> CREATOR
	= new Parcelable.Creator<ArticleListInfo>() {
		@Override
		public ArticleListInfo createFromParcel(Parcel in) {
			return new ArticleListInfo(in);
		}

		@Override
		public ArticleListInfo[] newArray(int size) {
			return new ArticleListInfo[size];
		}
	};
}

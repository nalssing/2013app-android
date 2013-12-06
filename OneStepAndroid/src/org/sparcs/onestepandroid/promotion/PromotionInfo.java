package org.sparcs.onestepandroid.promotion;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;

public class PromotionInfo{

	public ArrayList<View> getBody() {
		return body;
	}


	public void setBody(ArrayList<View> body) {
		this.body = body;
	}

	private int id;
	private String title;
	private ArrayList<View> body;
	//private String content;
	private int progress;
	private Bitmap picture;
	
	public PromotionInfo() {
		this.title="";
		this.id=0;
		this.body=null;
		this.progress=0;
	}


	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public int getId() {
		return id;
	}


	public int getProgress() {
		return progress;
	}


	public void setProgress(int progress) {
		this.progress = progress;
	}

	public PromotionInfo(int id, String title,  ArrayList<View> body, int progress) {
		this.title = title;
		this.id = id;
		this.body = body;
		this.progress = progress;
	}
}

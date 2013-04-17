package org.sparcs.onestepandroid.menu;

import java.util.ArrayList;

import android.widget.ListView;

public class MyMenu {
	public static enum Type {
		SECTION_HEADER,
		SINGLE_LINE,
		EXPANDABLE_SET,
		EXPANDABLE_LINE
	}
	private Type type;
	private String tag;
	private String title;
	private int icon;
	private ArrayList<MyMenu> childs;
	private boolean isExpanded = false;
	private ListView listview;
	public boolean isExpanded() {
		return isExpanded;
	}
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	public ListView getListview() {
		return listview;
	}
	public void setListview(ListView listview) {
		this.listview = listview;
	}
	public MyMenu(Type type, String title, String tag, int icon) {
		this.type = type;
		this.title = title;
		this.tag = tag;
		this.icon = icon;
		this.childs = null;
	}
	public MyMenu(Type type, String title, String tag) {
		this.type = type;
		this.title = title;
		this.tag = tag;
		this.icon = 0;
		this.childs = null;
	}
	public MyMenu(Type type, String title, String tag, ArrayList<MyMenu> childs) {
		this.type = type;
		this.title = title;
		this.tag = tag;
		this.icon = 0;
		this.childs = childs;
		this.childs.add(new MyMenu(MyMenu.Type.EXPANDABLE_LINE, title, tag));
	}
	public int getIcon() {
		return icon;
	}
	public ArrayList<MyMenu> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<MyMenu> childs) {
		this.childs = childs;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getTag() {
		return tag;
	}
	

	public void setTag(String tag) {
		this.tag = tag;
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
	
}

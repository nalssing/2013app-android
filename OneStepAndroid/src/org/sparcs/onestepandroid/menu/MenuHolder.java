package org.sparcs.onestepandroid.menu;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.sparcs.onestepandroid.R;
import org.sparcs.onestepandroid.util.Values;

public class MenuHolder {
	public static ArrayList<MyMenu> menus;
	
	public MenuHolder() {
		menus = new ArrayList<MyMenu>();
		MyMenu menu;
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "홈", "home", R.drawable.home);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "공지", "notice/student", R.drawable.notice);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SINGLE_LINE, "학생공지사항", "notice/student");
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SINGLE_LINE, "총학생회 공지", "notice/usc");
		menus.add(menu);
		
		NameValuePair[] portal_boards = Values.INSTANCE.portalBoards;
		ArrayList<MyMenu> subMenus = new ArrayList<MyMenu>();
		for (NameValuePair portal : portal_boards) {
			menu = new MyMenu(MyMenu.Type.SINGLE_LINE, portal.getName(), "notice/" + portal.getValue());
			subMenus.add(menu);
		}
		menu = new MyMenu(MyMenu.Type.EXPANDABLE_SET, "더 보기", "notice/more", subMenus);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "일정", "schedule", R.drawable.schedule);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "학우제안정책","suggestion/main", R.drawable.student_suggestion);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SINGLE_LINE, "BEST", "suggestion/best");
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SINGLE_LINE, "검토중", "suggestion/exam");
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SINGLE_LINE, "진행중","suggestion/proc");
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SINGLE_LINE, "전체 제안 보기", "suggestion/all");
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "사업홍보", "promotion", R.drawable.promotion);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "주요사이트 링크", "link", R.drawable.external_link);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "Q&A", "q&a", R.drawable.q_and_a);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "Vote/Survey", "vote", R.drawable.survey);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "설정", "setting", R.drawable.settings);
		menus.add(menu);
		menu = new MyMenu(MyMenu.Type.SECTION_HEADER, "총학생회 소개", "about", R.drawable.introduction);
		menus.add(menu);
	}
}

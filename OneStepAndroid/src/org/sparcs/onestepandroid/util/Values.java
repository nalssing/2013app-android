package org.sparcs.onestepandroid.util;

import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public enum Values {
	INSTANCE;
	public String host = "https://143.248.234.170/2013App";
	public NameValuePair[] portalBoards = new NameValuePair[] {
		new BasicNameValuePair("수강/학적/논문","lecture-academic-paper"),
		new BasicNameValuePair("리더십/인턴/상담","leadership-intern-counseling"),
		new BasicNameValuePair("생활관/장학/복지","dormitory-scholarship-welfare"),
		new BasicNameValuePair("교과과정 신설/변경","academic-courses"),
		new BasicNameValuePair("취업","recruiting"),
		new BasicNameValuePair("학생동아리소개","student-club"),
		new BasicNameValuePair("전문연구요원","researcher-on-military-duty"),
	};
	public HashMap<String, String> portalBoardsMap = new HashMap<String, String>();
	public String preferenceKey = "OneStep";
	public String tag = "com.example.onestep";
	private Values() {
		portalBoardsMap.put("student-notice", "학생공지사항");
		portalBoardsMap.put("gsc-usc-notice", "총학생회 공지");
		portalBoardsMap.put("lecture-academic-paper", "수강/학적/논문");
		portalBoardsMap.put("leadership-intern-counseling", "리더십/인턴/상담");
		portalBoardsMap.put("dormitory-scholarship-welfare", "생활관/장학/복지");
		portalBoardsMap.put("academic-courses", "교과과정 신설/변경");
		portalBoardsMap.put("recruiting", "취업");
		portalBoardsMap.put("student-club", "학생동아리소개");
		portalBoardsMap.put("researcher-on-military-duty", "전문연구요원");
	}
}

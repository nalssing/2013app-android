package com.example.onestep.util;

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
	public String preferenceKey = "OneStep";
}

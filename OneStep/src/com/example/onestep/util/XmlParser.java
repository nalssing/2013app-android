package com.example.onestep.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.onestep.article.ArticleInfo;
import com.example.onestep.noticeBoard.ArticleListInfo;
import com.example.onestep.sitesuggestion.SiteSuggestionInfo;

public class XmlParser {
	XmlPullParser parser = null;
	XmlPullParserFactory factory = null;
	public XmlParser() {
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<ArticleListInfo> parseArticleListInfo(String input) {
		ArrayList<ArticleListInfo> list = new ArrayList<ArticleListInfo>();
		try {

			parser.setInput(new ByteArrayInputStream(input.getBytes()), null);
			int eventType = parser.getEventType();
			ArticleListInfo info;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG && parser.getName().equals("article")) {
					info = new ArticleListInfo();
					eventType = parser.next();
					while (eventType != XmlPullParser.END_TAG || !parser.getName().equals("article")) {
						if(eventType == XmlPullParser.START_TAG) {
							if(parser.getName().equals("id"))
								info.setId(Integer.parseInt(parser.nextText()));
							if(parser.getName().equals("title"))
								info.setTitle(parser.nextText());
							if(parser.getName().equals("writer"))
								info.setWriter(parser.nextText());
							if(parser.getName().equals("time")) {
								String timeStamp = parser.nextText();
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeZone(TimeZone.getDefault());
								calendar.setTimeInMillis(Long.parseLong(timeStamp));
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
								info.setTime(formatter.format(calendar.getTime()));
							}
							if(parser.getName().equals("read_count"))
								info.setHit(Integer.parseInt(parser.nextText()));
						}
						eventType = parser.next();
					}
					list.add(info);
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public ArrayList<ArticleInfo> parseArticleInfo(String input) {
		ArrayList<ArticleInfo> list = new ArrayList<ArticleInfo>();

		try {
			parser.setInput(new ByteArrayInputStream(input.getBytes()), null);
			int eventType = parser.getEventType();
			ArticleInfo rootArticle = new ArticleInfo();
			while((eventType = parser.next()) != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_TAG) {
					if(parser.getName().equals("id"))
						rootArticle.setId(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("title"))
						rootArticle.setTitle(parser.nextText());
					if(parser.getName().equals("writer"))
						rootArticle.setWriter(parser.nextText());
					if(parser.getName().equals("read"))
						rootArticle.setRead(Boolean.getBoolean(parser.nextText()));
					if(parser.getName().equals("board"))
						rootArticle.setBoard(parser.nextText());
					if(parser.getName().equals("content"))
						rootArticle.setContent(parser.nextText());
					if(parser.getName().equals("vote_up"))
						rootArticle.setVoteUp(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("vote_down"))
						rootArticle.setVoteDown(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("time"))
						rootArticle.setTime(parser.nextText());
					if(parser.getName().equals("read_count"))
						rootArticle.setReadCount(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("sub_articles"))
						break;
				}
			}
			list.add(rootArticle);

			ArticleInfo subArticle = new ArticleInfo();
			while((eventType = parser.next()) != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_TAG) {
					if(parser.getName().equals("id"))
						subArticle.setId(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("title"))
						subArticle.setTitle(parser.nextText());
					if(parser.getName().equals("writer"))
						subArticle.setWriter(parser.nextText());
					if(parser.getName().equals("read"))
						subArticle.setRead(Boolean.getBoolean(parser.nextText()));
					if(parser.getName().equals("board"))
						subArticle.setBoard(parser.nextText());
					if(parser.getName().equals("content"))
						subArticle.setContent(parser.nextText());
					if(parser.getName().equals("vote_up"))
						subArticle.setVoteUp(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("vote_down"))
						subArticle.setVoteDown(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("time"))
						subArticle.setTime(parser.nextText());
					if(parser.getName().equals("read_count"))
						subArticle.setReadCount(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("referer"))
						subArticle.setReferer(Integer.parseInt(parser.nextText()));
				}
				if(eventType == XmlPullParser.END_TAG && parser.getName().equals("article"))
				{
					list.add(subArticle);
					subArticle = new ArticleInfo();
				}

				if(eventType == XmlPullParser.END_TAG && parser.getName().equals("sub_articles"))
				{
					break;
				}
			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public ArrayList<SiteSuggestionInfo> parseSiteSuggestionInfo(String input)
	{
		ArrayList<SiteSuggestionInfo> list = new ArrayList<SiteSuggestionInfo>();

		try {
			parser.setInput(new ByteArrayInputStream(input.getBytes()), null);
			int eventType = parser.getEventType();
			SiteSuggestionInfo info;
			while((eventType = parser.next()) != XmlPullParser.END_DOCUMENT)
			{
				info = new SiteSuggestionInfo();
				if (eventType == XmlPullParser.START_TAG && parser.getName().equals("site"))
				{
					while (!((eventType = parser.next()) == XmlPullParser.END_TAG && parser.getName().equals("site")))
						if (eventType == XmlPullParser.START_TAG)
						{
							if(parser.getName().equals("name"))
								info.setName(parser.nextText());
							if(parser.getName().equals("url"))
								info.setUrl(parser.nextText());
						}
				}
				list.add(info);
			}

		}
		catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

}

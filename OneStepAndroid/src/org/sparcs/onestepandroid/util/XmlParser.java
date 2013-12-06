package org.sparcs.onestepandroid.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.sparcs.onestepandroid.article.ArticleInfo;
import org.sparcs.onestepandroid.article.ArticleListInfo;
import org.sparcs.onestepandroid.calendar.EventInfo;
import org.sparcs.onestepandroid.sitesuggestion.SiteSuggestionInfo;
import org.sparcs.onestepandroid.votesurvey.VoteSurveyMainItem;
import org.sparcs.onestepandroid.votesurvey.VoteSurveyQuestion;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

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
							if(parser.getName().equals("reply"))
								info.setNumReply(Integer.parseInt(parser.nextText()));
							if(parser.getName().equals("vote_up"))
								info.setVoteUp(Integer.parseInt(parser.nextText()));
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
					if(parser.getName().equals("time")) {
						String timeStamp = parser.nextText();
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeZone(TimeZone.getDefault());
						calendar.setTimeInMillis(Long.parseLong(timeStamp));
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
						rootArticle.setTime(formatter.format(calendar.getTime()));
					}
					if(parser.getName().equals("read_count"))
						rootArticle.setReadCount(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("sub_articles"))
						break;
				}
			}
			rootArticle.setType(ArticleInfo.Type.ARTICLE);
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
					if(parser.getName().equals("time")) {
						String timeStamp = parser.nextText();
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeZone(TimeZone.getDefault());
						calendar.setTimeInMillis(Long.parseLong(timeStamp));
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
						subArticle.setTime(formatter.format(calendar.getTime()));
					}
					if(parser.getName().equals("read_count"))
						subArticle.setReadCount(Integer.parseInt(parser.nextText()));
					if(parser.getName().equals("referer"))
						subArticle.setReferer(Integer.parseInt(parser.nextText()));
				}
				if(eventType == XmlPullParser.END_TAG && parser.getName().equals("article"))
				{
					subArticle.setType(ArticleInfo.Type.REPLY);
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
			SiteSuggestionInfo item;
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				if (eventType == XmlPullParser.START_TAG && parser.getName().equals("site"))
				{
					item = new SiteSuggestionInfo();
					while ( eventType != XmlPullParser.END_TAG || !parser.getName().equals("site"))
					{
						if (eventType == XmlPullParser.START_TAG)
						{
							if (parser.getName().equals("name"))
								item.setName(parser.nextText());
							if (parser.getName().equals("url"))
								item.setUrl(parser.nextText());
						}
						eventType = parser.next();
					}
					list.add(item);
				}
				eventType = parser.next();
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
	
	public ArrayList<VoteSurveyMainItem> parseVoteSurveyMainItem(String input) {
		ArrayList<VoteSurveyMainItem> list = new ArrayList<VoteSurveyMainItem>();
		try {
			parser.setInput(new ByteArrayInputStream(input.getBytes()), null);
			int eventType = parser.getEventType();
			VoteSurveyMainItem item;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG && parser.getName().equals("survey")) {
					item = new VoteSurveyMainItem();
					eventType = parser.next();
					while (eventType != XmlPullParser.END_TAG || !parser.getName().equals("survey")) {
						if(eventType == XmlPullParser.START_TAG) {
							if(parser.getName().equals("id"))
								item.setId(Integer.parseInt(parser.nextText()));
							if(parser.getName().equals("title"))
								item.setTitle(parser.nextText());
							if(parser.getName().equals("done"))
								item.setIs_done(Boolean.parseBoolean(parser.nextText()));
							if(parser.getName().equals("is_closed"))
								item.setIs_closed(Boolean.parseBoolean(parser.nextText()));
							if(parser.getName().equals("type"))
								item.setType(parser.nextText());
							if(parser.getName().equals("create_time")) {
								String timeStamp = parser.nextText();
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeZone(TimeZone.getDefault());
								calendar.setTimeInMillis(Long.parseLong(timeStamp));
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
								item.setCreate_time(formatter.format(calendar.getTime()));
							}
							if(parser.getName().equals("expire_time")) {
								String timeStamp = parser.nextText();
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeZone(TimeZone.getDefault());
								calendar.setTimeInMillis(Long.parseLong(timeStamp));
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
								item.setExpire_time(formatter.format(calendar.getTime()));
							}
						}
						eventType = parser.next();
					}
					list.add(item);
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
	
	public List<VoteSurveyQuestion> parseVoteSurveyForm(String input) {
		List<VoteSurveyQuestion> list = new LinkedList<VoteSurveyQuestion>();
		try {
			parser.setInput(new ByteArrayInputStream(input.getBytes()), null);
			int eventType = parser.getEventType();
			VoteSurveyQuestion item;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG && parser.getName().equals("text")) {
					item = new VoteSurveyQuestion();
					item.setTitle(parser.getAttributeValue(null, "title"));
					item.setIs_essay(true);
					list.add(item);
				}
				if(eventType == XmlPullParser.START_TAG && parser.getName().equals("multiple_choice")) {
					item = new VoteSurveyQuestion();
					item.setIs_essay(false);
					item.setTitle(parser.getAttributeValue(null, "title"));
					item.setMax(Integer.parseInt(parser.getAttributeValue(null, "max")));
					item.setMin(Integer.parseInt(parser.getAttributeValue(null, "min")));
					item.setChoices(new LinkedList<String>());
					eventType = parser.next();
					while (eventType != XmlPullParser.END_TAG || !parser.getName().equals("multiple_choice")) {
						if(eventType == XmlPullParser.START_TAG) {
							if(parser.getName().equals("multiple_choice_item"))
								item.getChoices().add(parser.nextText());
						}
						eventType = parser.next();
					}
					list.add(item);
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
	public List<String> parsePushStatus(String input) {
		List<String> list = new LinkedList<String>();
		try {
			parser.setInput(new ByteArrayInputStream(input.getBytes()), null);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG && parser.getName().equals("block")) {
					list.add(parser.nextText());
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
	
	public ArrayList<EventInfo> parseEvent(String input) {
		ArrayList<EventInfo> list = new ArrayList<EventInfo>();

		try {
			parser.setInput(new ByteArrayInputStream(input.getBytes()), null);
			int eventType = parser.getEventType();
			EventInfo item;
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				if (eventType == XmlPullParser.START_TAG && parser.getName().equals("event"))
				{
					item = new EventInfo();
					while ( eventType != XmlPullParser.END_TAG || !parser.getName().equals("event"))
					{
						if (eventType == XmlPullParser.START_TAG)
						{
							if (parser.getName().equals("title"))
								item.setTitle(parser.nextText());
							if (parser.getName().equals("content"))
								item.setContent(parser.nextText());
							if (parser.getName().equals("time"))
							{
								//Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S",Locale.KOREA).format(Long.parseLong(parser.nextText()));
								Calendar cal = Calendar.getInstance();
								cal.setTimeInMillis(Long.parseLong(parser.nextText()));
								item.setDate(cal.getTime());
							}
						}
						eventType = parser.next();
					}
					list.add(item);
				}
				eventType = parser.next();
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

package com.example.onestep.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public enum NetworkRequestManager {
	INSTANCE;
	DefaultHttpClient httpClient;
	NetworkRequestManager() {
		HttpParams httpParameters = new BasicHttpParams();
		SchemeRegistry registry = new SchemeRegistry();
		TrustAllSSLSocketFactory socketFactory;
		try {
			socketFactory = new TrustAllSSLSocketFactory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		registry.register(new Scheme("https", socketFactory, 443));
		ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(httpParameters, registry);
		httpClient = new DefaultHttpClient(mgr, httpParameters);
	}
	
	public  synchronized HttpResponse login(String username, String password) {
		String url = new String(Values.INSTANCE.host);
		url += "/user/Login";
		final HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		HttpResponse httpResponse = null;
		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			httpRequest.setEntity(ent);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					httpRequest.abort();
				}
			}, 5000);
			synchronized (httpClient) {
				httpResponse = httpClient.execute(httpRequest);
			} 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResponse;
	}
	
	public synchronized HttpResponse registerDevice(String regId) {
		Log.i("sync","register start");
		String url = new String(Values.INSTANCE.host);
		url += "/push/RegisterPush";
		final HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("device", "GOOGLE_GCM"));
		params.add(new BasicNameValuePair("deviceID", regId));
		HttpResponse httpResponse = null;
		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			httpRequest.setEntity(ent);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					httpRequest.abort();
				}
			}, 5000);
			synchronized (httpClient) {
				httpResponse = httpClient.execute(httpRequest);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("sync","register end");
		return httpResponse;
	}

	public synchronized HttpResponse getArticleList(String boardName, int from, int count, String type) {
		String url = new String(Values.INSTANCE.host);
		url += "/board/ListArticle";
		url += "?";
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("board", boardName));
		param.add(new BasicNameValuePair("from", String.valueOf(from)));
		param.add(new BasicNameValuePair("count", String.valueOf(count)));
		param.add(new BasicNameValuePair("type", String.valueOf(type)));
		url += URLEncodedUtils.format(param, HTTP.UTF_8);
		Log.i("",url);
		HttpResponse httpResponse = null;
		final HttpGet httpRequest = new HttpGet(url);
		try {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					httpRequest.abort();
				}
			}, 10000);
			synchronized (httpClient) {
			httpResponse = httpClient.execute(httpRequest);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (httpResponse == null) 
			Log.i("status","response : null");
		else
			Log.i("status", "response" + String.valueOf(httpResponse.getStatusLine().getStatusCode()));
		return httpResponse;
	}
	
	public synchronized HttpResponse getArticleList(String boardName, int from, int count, String type, int retry) {
		Log.i("sync","getArticle start");
		HttpResponse response = getArticleList(boardName, from, count, type);
		if (response == null ||response.getStatusLine().getStatusCode() == 500) {
			if (retry > 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response = getArticleList(boardName, from, count, type, retry - 1);
			}
		}
		Log.i("sync","getArticle end");
		return response;
	}
}

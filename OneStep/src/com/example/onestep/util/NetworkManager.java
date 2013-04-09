package com.example.onestep.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public enum NetworkManager {
	INSTANCE;
	private final HostnameVerifier DO_NOT_VERIFY;
	private CookieManager cookieManager;
	String cookie;
	private SharedPreferences preference;
	NetworkManager() {
		cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
		/**
		 * Trust every server - dont check for any certificate
		 */
		// always verify the host - dont check for certificate
		DO_NOT_VERIFY = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
			.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void registerSharedPreference(Context context) {
		preference = PreferenceManager.getDefaultSharedPreferences(context);
	}
	public NetworkReturning login(String id, String passwd) {
		URL url = null;
		NetworkReturning returning;
		HttpsURLConnection https = null;
		try {
			StringBuilder builder = new StringBuilder();
			String urlString = builder
					.append(Values.INSTANCE.host)
					.append("/user/Login")
					.toString();
			url = new URL(urlString);
			https = (HttpsURLConnection) url.openConnection();
			https.setHostnameVerifier(DO_NOT_VERIFY);
			https.setDoOutput(true);
			https.setDoInput(true);
			https.setChunkedStreamingMode(0);
			https.setRequestMethod("POST");

			OutputStream out = new BufferedOutputStream(https.getOutputStream());
			builder = new StringBuilder();
			String param = 
					builder
					.append("username=")
					.append(id)
					.append("&password=")
					.append(passwd)
					.toString();
			out.write(param.getBytes());
			out.flush();

			returning = new NetworkReturning(https.getResponseCode(), null);
			if (returning.getStatus() == 200) {
				preference.edit().putString("username", id);
				preference.edit().putString("password", passwd);
				//preference.edit().putString("deviceID", Secure.ANDROID_ID );
			}
			https.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			returning = new NetworkReturning(500, null);
			if (https != null)
				https.disconnect();

		}
		Log.i("manager", "login : " + String.valueOf(returning.getStatus()));
		return returning;
	}

	public NetworkReturning postExample(String boardname, int from, int count, String type) {
		URL url = null;
		NetworkReturning returning;
		HttpsURLConnection https = null;
		try {
			StringBuilder builder = new StringBuilder();
			String urlString = builder
					.append(Values.INSTANCE.host)
					.append("/board/ListArticle")
					.toString();
			url = new URL(urlString);
			https = (HttpsURLConnection) url.openConnection();
			https.addRequestProperty("Cookie", cookie);
			https.setHostnameVerifier(DO_NOT_VERIFY);
			https.setDoOutput(true);
			https.setChunkedStreamingMode(0);
			https.setRequestMethod("POST");
			OutputStream out = new BufferedOutputStream(https.getOutputStream());
			builder = new StringBuilder();
			String param = 
					builder
					.append("board=")
					.append(URLEncoder.encode(boardname, "UTF-8"))
					.append("&from=")
					.append(URLEncoder.encode(String.valueOf(from), "UTF-8"))
					.append("&count=")
					.append(URLEncoder.encode(String.valueOf(count), "UTF-8"))
					.append("&type=")
					.append(URLEncoder.encode(type, "UTF-8"))
					.toString();
			out.write(param.getBytes());

			returning = new NetworkReturning(https.getResponseCode(), "");
			https.disconnect();
		} catch (Exception e) {
			returning = new NetworkReturning(500, "");
			if (https != null)
				https.disconnect();
		}
		return returning;
	}

	public NetworkReturning getArticleList(String boardname, int from, int count, String type) {
		URL url = null;
		NetworkReturning returning;
		HttpsURLConnection https = null;
		try {
			StringBuilder builder = new StringBuilder();
			String urlString = builder
					.append(Values.INSTANCE.host)
					.append("/board/ListArticle")
					.append("?board=")
					.append(URLEncoder.encode(boardname, "UTF-8"))
					.append("&from=")
					.append(URLEncoder.encode(String.valueOf(from), "UTF-8"))
					.append("&count=")
					.append(URLEncoder.encode(String.valueOf(count), "UTF-8"))
					.append("&type=")
					.append(URLEncoder.encode(type, "UTF-8"))
					.toString();
			url = new URL(urlString);
			https = (HttpsURLConnection) url.openConnection();
			https.setHostnameVerifier(DO_NOT_VERIFY);
			https.setChunkedStreamingMode(0);
			https.setDoInput(true);
			InputStream in = new BufferedInputStream(https.getInputStream());
			byte[] buffer = new byte[4096];
			builder = new StringBuilder();
			int n;
			while ((n = in.read(buffer)) != -1) {
				builder.append(new String(buffer, 0, n));
			}
			returning = new NetworkReturning(https.getResponseCode(), builder.toString());
			https.disconnect();
		} catch (Exception e) {
			returning = new NetworkReturning(500, null);
			if (https != null)
				https.disconnect();
		}
		Log.i("manager", "getArticle : " + String.valueOf(returning.getStatus()));
		return returning;
	}

	public NetworkReturning registerPush(String deviceId) {
		URL url = null;
		NetworkReturning returning;
		HttpsURLConnection https = null;
		try {
			StringBuilder builder = new StringBuilder();
			String urlString = builder
					.append(Values.INSTANCE.host)
					.append("/push/RegisterPush")
					.toString();
			url = new URL(urlString);
			https = (HttpsURLConnection) url.openConnection();
			https.setHostnameVerifier(DO_NOT_VERIFY);
			https.setDoOutput(true);
			https.setDoInput(true);
			https.setChunkedStreamingMode(0);
			https.setRequestMethod("POST");

			OutputStream out = new BufferedOutputStream(https.getOutputStream());
			builder = new StringBuilder();
			String param = 
					builder
					.append("device=")
					.append("GOOGLE_GCM")
					.append("&deviceID=")
					.append(deviceId)
					.toString();
			out.write(param.getBytes());
			out.flush();

			returning = new NetworkReturning(https.getResponseCode(), null);
			https.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			returning = new NetworkReturning(500, null);
			if (https != null)
				https.disconnect();

		}
		Log.i("manager", "register : " + String.valueOf(returning.getStatus()));
		return returning;
	}
	public NetworkReturning getArticleInfo(int articleid, String boardname)
	{
		URL url = null;
		NetworkReturning returning;
		HttpsURLConnection https = null;
		try {
			StringBuilder builder = new StringBuilder();
			builder
			.append(Values.INSTANCE.host)
			.append("/board/ArticleInfo")
			.append("?id=")
			.append(URLEncoder.encode(String.valueOf(articleid), "UTF-8"));
			if (boardname != null) {
				builder
				.append("&portal_board=")
				.append(boardname);
			}
			String urlString = builder.toString();
			url = new URL(urlString);
			https = (HttpsURLConnection) url.openConnection();
			https.setHostnameVerifier(DO_NOT_VERIFY);
			https.setChunkedStreamingMode(0);
			https.setDoInput(true);
			InputStream in = new BufferedInputStream(https.getInputStream());
			byte[] buffer = new byte[4096];
			builder = new StringBuilder();
			int n;
			while ((n = in.read(buffer)) != -1) {
				builder.append(new String(buffer, 0, n));
			}
			returning = new NetworkReturning(https.getResponseCode(), builder.toString());
			https.disconnect();
		} catch (Exception e) {
			returning = new NetworkReturning(500, null);
			if (https != null)
				https.disconnect();
		}
		Log.i("manager", "getArticleInfo : " + String.valueOf(returning.getStatus()));
		return returning;
	}

	public NetworkReturning writeArticle(String board, String title, String content, int reply, String type)
	{
		URL url = null;
		NetworkReturning returning;
		HttpsURLConnection https = null;
		try {
			StringBuilder builder = new StringBuilder();
			String urlString = builder
					.append(Values.INSTANCE.host)
					.append("/board/WriteArticle")
					.toString();
			url = new URL(urlString);
			https = (HttpsURLConnection) url.openConnection();
			https.addRequestProperty("Cookie", cookie);
			https.setHostnameVerifier(DO_NOT_VERIFY);
			https.setDoOutput(true);
			https.setChunkedStreamingMode(0);
			https.setRequestMethod("POST");
			OutputStream out = new BufferedOutputStream(https.getOutputStream());
			builder = new StringBuilder();
			builder
			.append("board=")
			.append(URLEncoder.encode(board, "UTF-8"))
			.append("&title=")
			.append(URLEncoder.encode(title, "UTF-8"))
			.append("&content=")
			.append(URLEncoder.encode(content, "UTF-8"));
			if (reply>=0)
				builder
				.append("&reply=")
				.append(URLEncoder.encode(String.valueOf(reply), "UTF-8"));
			if (type!=null)
				builder
				.append("&reply=")
				.append(URLEncoder.encode(type, "UTF-8"));
			String param = builder.toString();
			out.write(param.getBytes());

			returning = new NetworkReturning(https.getResponseCode(), "");
			https.disconnect();
		} catch (Exception e) {
			returning = new NetworkReturning(500, "");
			if (https != null)
				https.disconnect();
		}
		return returning;
	}
	
	public NetworkReturning getSiteSuggestion()
	{
		URL url = null;
		NetworkReturning returning;
		HttpsURLConnection https = null;
		try {
			StringBuilder builder = new StringBuilder();
			builder
			.append(Values.INSTANCE.host)
			.append("/dataprovider/SiteSuggestion");
			String urlString = builder.toString();
			url = new URL(urlString);
			https = (HttpsURLConnection) url.openConnection();
			https.setHostnameVerifier(DO_NOT_VERIFY);
			https.setChunkedStreamingMode(0);
			https.setDoInput(true);
			InputStream in = new BufferedInputStream(https.getInputStream());
			byte[] buffer = new byte[4096];
			builder = new StringBuilder();
			int n;
			while ((n = in.read(buffer)) != -1) {
				builder.append(new String(buffer, 0, n));
			}
			returning = new NetworkReturning(https.getResponseCode(), builder.toString());
			https.disconnect();
		} catch (Exception e) {
			returning = new NetworkReturning(500, null);
			if (https != null)
				https.disconnect();
		}
		Log.i("manager", "getSiteSuggestion : " + String.valueOf(returning.getStatus()));
		return returning;
	}
}

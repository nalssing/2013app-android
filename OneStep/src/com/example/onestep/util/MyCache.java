package com.example.onestep.util;

import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;

public enum MyCache {
	INSTANCE;
	private LruCache<String, Fragment> cache;
	MyCache() {
		int cacheSize = 4 * 1024 * 1024;
		cache = new LruCache<String, Fragment>(cacheSize);
	}
	public LruCache<String, Fragment> getCache() {
		return cache;
	}
}

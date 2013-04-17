package org.sparcs.onestepandroid.util;

import android.app.Fragment;
import android.util.LruCache;

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

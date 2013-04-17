package org.sparcs.onestepandroid.util;

import java.util.ArrayList;

import org.sparcs.onestepandroid.noti.NotiItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public enum DBHelper  {
	INSTANCE;
	private static final String DATABASE_NAME = "onestep.db";
	private static final int DATABASE_VERSION = 9;
	private DBOpenHelper openHelper;
	public static final class CreateDB implements BaseColumns{
		public static final String TITLE = "title";
		public static final String MESSAGE = "message";
		public static final String TYPE = "type";
		public static final String BOARD_NAME = "board_name";
		public static final String ARTICLE_ID = "article_id";
		public static final String TIME = "time";
		public static final String CHECKED = "checked";
		public static final String _TABLENAME = "notifications";
		public static final String _CREATE = 
				"create table "+_TABLENAME+"("
						+_ID+" integer primary key autoincrement, "   
						+TITLE+" text not null, "
						+MESSAGE+" text not null, "
						+TYPE+" text not null , "
						+BOARD_NAME+" text , "
						+ARTICLE_ID+" integer , "
						+TIME+" text not null , "
						+CHECKED+" integer not null );";
		private CreateDB() {
			
		}
	}
	private class DBOpenHelper extends SQLiteOpenHelper {
		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CreateDB._CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+CreateDB._TABLENAME);
            onCreate(db);
		}
	}

	DBHelper(){
	}
	public void initialize(Context context) {
		if (openHelper == null)
			openHelper = new DBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	synchronized public void insertColumn(String title, String message, String type, String boardName, int articleID, String time) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CreateDB.TITLE, title);
		values.put(CreateDB.MESSAGE, message);
		values.put(CreateDB.TYPE, type);
		values.put(CreateDB.BOARD_NAME, boardName);
		values.put(CreateDB.ARTICLE_ID, articleID);
		values.put(CreateDB.TIME, time);
		values.put(CreateDB.CHECKED, 0);
		db.insert(CreateDB._TABLENAME, null, values);
		String table = DBHelper.CreateDB._TABLENAME;
		String[] columns = {DBHelper.CreateDB._ID};
		Cursor result = db.query(table, columns, null, null, null, null, null);
		if (result.getCount() > 30) {
			result.moveToFirst();
			db.delete(table, "_id=?", new String[]{result.getString(0)});
		}
		result.close();
		db.close();
	}
	
	synchronized public ArrayList<NotiItem> getNotiItems() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ArrayList<NotiItem> items = new ArrayList<NotiItem>();
		String table = DBHelper.CreateDB._TABLENAME;
		String[] columns = {DBHelper.CreateDB._ID, DBHelper.CreateDB.TITLE, DBHelper.CreateDB.MESSAGE, DBHelper.CreateDB.BOARD_NAME,
				DBHelper.CreateDB.ARTICLE_ID, DBHelper.CreateDB.TYPE, DBHelper.CreateDB.TIME, DBHelper.CreateDB.CHECKED
		};
		Cursor result = db.query(table, columns, null, null, null, null, "CHECKED ASC, _ID DESC");
		result.moveToFirst();
		while (!result.isAfterLast()) {
			items.add(new NotiItem(result.getInt(0), result.getString(1), result.getString(2), 
					result.getString(3), result.getInt(4), result.getString(5), result.getString(6), result.getInt(7)));
			result.moveToNext();
		}
		result.close();
		db.close();
		return items;
		
	}
	
	synchronized public void check(int _id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CreateDB.CHECKED, 1);
		db.update(DBHelper.CreateDB._TABLENAME, values, DBHelper.CreateDB._ID+"=? ", new String[]{String.valueOf(_id)});
		db.close();
	}
	
	synchronized public int unCheckedCount() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String table = DBHelper.CreateDB._TABLENAME;
		String[] columns = {DBHelper.CreateDB.CHECKED};
		Cursor result = db.query(table, columns, DBHelper.CreateDB.CHECKED+"=0", null, null, null, null);
		int n = result.getCount();
		result.close();
		db.close();
		return n;
	}

}

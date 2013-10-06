package cn.edu.sjtu.se.jtlifehacker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {

	static final int DB_VERSION = 1;
	static final String DB_NAME = "database.db";
	static final String C_KEY = "KEY";
	static final String C_TITLE = "TITLE";
	static final String C_DATE = "DATE";
	static final String C_CONTENT = "CONTENT";

	
	public void writeEntries(Context context,String table_name,List<NewsFetcher.Entry> entries) {
		OpenHelper openhelper = new OpenHelper(context,table_name);
		SQLiteDatabase db = openhelper.getWritableDatabase();
		for(NewsFetcher.Entry entry : entries) {
			ContentValues values = new ContentValues();
			values.put(C_KEY, entry.key);
			values.put(C_TITLE, entry.title);
			values.put(C_DATE, entry.date);
			values.put(C_CONTENT, entry.content);
			try {
				db.insertOrThrow(table_name, null, values);
			} catch (SQLiteConstraintException e) {
				// just ignore it
			}
		}
		db.close();
	}
	
	public List<NewsFetcher.Entry> getEntries(Context context,String table_name) {
		OpenHelper openhelper = new OpenHelper(context,table_name);
		SQLiteDatabase db = openhelper.getReadableDatabase();
		Cursor cursor = db.query(table_name, null, null, null, null, null, null, "10");
		List<NewsFetcher.Entry> list = new ArrayList<NewsFetcher.Entry>();
		while (cursor.moveToNext()) {
			String title = cursor.getString(cursor.getColumnIndex(C_TITLE));
			String date = cursor.getString(cursor.getColumnIndex(C_DATE));
			String content = cursor.getString(cursor.getColumnIndex(C_CONTENT));
			NewsFetcher.Entry entry = new NewsFetcher.Entry(title, date, content);
			list.add(entry);
		}
		return list;
	}
	
	private class OpenHelper extends SQLiteOpenHelper {
		private final String CREATE_TABLE;

		public OpenHelper(Context context,String table_name) {
			super(context, DB_NAME, null, DB_VERSION); // specify no cursor factory here
			CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + " (" +
	                C_KEY + " INT PRIMARY KEY, " +
	                C_TITLE + " TEXT, " +
	                C_DATE + " TEXT, " +
	                C_CONTENT + " TEXT);";
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}
		
		@Override
		public void onConfigure(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
	}
	
}

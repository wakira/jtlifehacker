package cn.edu.sjtu.se.jtlifehacker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	static final String DB_NAME = "database.db";
	static final int DB_VERSION = 1;
	static final String C_KEY = "KEY";
	static final String C_TITLE = "TITLE";
	static final String C_DATE = "DATE";
	static final String C_CONTENT = "CONTENT";

	private List<String> mSqlCreateDB;
	
	private void addTableCreationSql(String tab_name) {
		mSqlCreateDB.add("CREATE TABLE IF NOT EXISTS " + tab_name + " (" +
                C_KEY + " INT PRIMARY KEY, " +
                C_TITLE + " TEXT, " +
                C_DATE + " TEXT, " +
                C_CONTENT + " TEXT);");
	}
	
	public DatabaseHelper(Context context,List<NewsFetcher> data_sources) {
		super(context, DB_NAME, null, DB_VERSION); // specify no cursor factory here
		
		mSqlCreateDB = new ArrayList<String>();
		for(NewsFetcher fetcher : data_sources) {
			addTableCreationSql(fetcher.table_name());
		}
	}

	@Override
	public void onConfigure(SQLiteDatabase db) {
		for (String sql : mSqlCreateDB)
			db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql : mSqlCreateDB)
			db.execSQL(sql);
	}

}

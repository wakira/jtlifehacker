package cn.edu.sjtu.se.jtlifehacker;

import java.util.List;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class UpdaterService extends Service {
	static boolean isRunning = false;
	
	private boolean working = false;
	private Updater mUpdater;

	private class Updater extends Thread {
		
		public Updater() {
			super("JTLifeHacker-Updater");
		}
		
		@Override
		public void run() {
			while(UpdaterService.this.working) try {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UpdaterService.this);
				long time = Long.parseLong(prefs.getString("sync_frequency", "30"))*1000*60; // TODO remove magic number here
					
				List<NewsFetcher> fetchers = ((JTLifeHackerApplication)getApplication()).mFetchers;
				System.out.println(fetchers);
				DatabaseHelper dbhelper = new DatabaseHelper(UpdaterService.this,fetchers);
				SQLiteDatabase db = dbhelper.getWritableDatabase();
				for(NewsFetcher fetcher : fetchers) {
					for(NewsFetcher.Entry entry : fetcher.getLatestEntries()) {
						ContentValues values = new ContentValues();
						values.put(DatabaseHelper.C_KEY, entry.key);
						values.put(DatabaseHelper.C_TITLE, entry.title);
						values.put(DatabaseHelper.C_DATE, entry.date);
						values.put(DatabaseHelper.C_CONTENT, entry.content);
						db.insertOrThrow(fetcher.table_name(), null, values);
					}
				}
				db.close();
				sleep(time);
			} catch (InterruptedException e) {
				UpdaterService.this.working = false;
			}
		}
		
	}
	
	@Override
	public void onCreate() {
		mUpdater = new Updater();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO ... might bind service to activity someday
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isRunning = true;
		
		if (!working) {
			working = true;
			mUpdater.start();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		isRunning = false;
		working = false;
		mUpdater.interrupt();
		mUpdater = null;
		super.onDestroy();
	}

}

package cn.edu.sjtu.se.jtlifehacker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class JTLifeHackerApplication extends Application 
	implements OnSharedPreferenceChangeListener{
	
	public static final String PACKAGE_NAME = "cn.edu.sjtu.se.jtlifehacker";
	
	public ArrayList<NewsFetcher> mFetchers;

	@Override
	public void onCreate() {
		Set<String> empty = new HashSet<String>();
		Set<String> selected_news = PreferenceManager.getDefaultSharedPreferences(this).getStringSet("news_selection", empty);
		updateFetchers(selected_news);
		super.onCreate();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// Update drawer_list from selected news fetcher
		Set<String> selected_news = sharedPreferences.getStringSet("news_selection", null);
		updateFetchers(selected_news);

		// Toggle background updater service
		if(sharedPreferences.getBoolean("background_service", true) == true) {
			// if background service is set to on
			if (!UpdaterService.isRunning)
				startService(new Intent(this,UpdaterService.class));
		} else {
			// if background service is set to off
			if (UpdaterService.isRunning)
				stopService(new Intent(this,UpdaterService.class));
		}
	}
	
	private void updateFetchers(Set<String> selected_news) {
		Log.d("DevDEBUG", "updateFetchers(...)");
		mFetchers = new ArrayList<NewsFetcher>();
		for (String str : selected_news) try {
			Log.d("DevDEBUG", "  " + str);
			mFetchers.add( (NewsFetcher) Class.forName(PACKAGE_NAME + "." + str).newInstance());
		} catch (Throwable e) {
			Log.wtf(this.toString(),e.toString());
		}
	}
	
}

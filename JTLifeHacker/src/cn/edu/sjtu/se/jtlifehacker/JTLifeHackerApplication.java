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

/**
 * JTLifeHackerApplication
 * @author kickkill
 * Holds global constants like PACKAGE_NAME
 * and provides fetcher information
 *
 */
public class JTLifeHackerApplication extends Application 
	implements OnSharedPreferenceChangeListener{
	
	public static final String PACKAGE_NAME = "cn.edu.sjtu.se.jtlifehacker";
	
	public ArrayList<NewsFetcher> mAllFetcher;
	public ArrayList<NewsFetcher> mFetcherForService;

	/**
	 * On application's first run,
	 * 1. all NewsFetcher should be registered to mAllFetcher
	 * 2. mFetcherForService should also be set for service to run
	 * 
	 */
	@Override
	public void onCreate() {
		mAllFetcher = new ArrayList<NewsFetcher>();
		// Register all fetcher here
		registerFetcher(ElearningFetcher.class);
		
		// Read selected fetcher for service from preference and add them to mFetcherForService
		updateFetcherForService();
		super.onCreate();
		
		SharedPreferences shared_preference = PreferenceManager.getDefaultSharedPreferences(this);
		shared_preference.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// Update drawer_list from selected news fetcher
		if (key == "news_selection")
			updateFetcherForService();

		// Toggle background updater service
		if (key == "background_service") {
			if(sharedPreferences.getBoolean("background_service", true) == true) {
				// if background service is set to on but the service is not running
				if (!UpdaterService.isRunning)
					startService(new Intent(this,UpdaterService.class));
			} else {
				// if background service is set to off and the service is running
				if (UpdaterService.isRunning)
					stopService(new Intent(this,UpdaterService.class));
			}
		}
	}
	
	private void registerFetcher(Class<?> fetcher_class) {
		try {
			mAllFetcher.add( (NewsFetcher) fetcher_class.newInstance());
		} catch (Throwable e) {
			Log.wtf(this.toString(),e.toString());
		}
	}
	
	private void updateFetcherForService() {
		Set<String> empty = new HashSet<String>();
		Set<String> selected_news = PreferenceManager.
				getDefaultSharedPreferences(this).getStringSet("news_selection", empty);
		mFetcherForService = new ArrayList<NewsFetcher>();
		for (String str : selected_news) try {
			mFetcherForService.add( (NewsFetcher) Class.forName(PACKAGE_NAME + "." + str).newInstance());
		} catch (Throwable e) {
			Log.wtf(this.toString(),e.toString());
		}
	}
	
}

package cn.edu.sjtu.se.jtlifehacker;

import java.util.List;

/**
 * 
 * TITLE: the title
 * DATE: a string of date (optional)
 * CONTENT: content
 * After implementing a NewsFetcher, register it in JTLifeHackerAPplication.onCreate
 * or it will not be used
 * 
 */

public interface NewsFetcher {

	String table_name(); // unique name in database
	String display_name();
	String pref_username_id();
	String pref_password_id();
	boolean requiresLogin();
	
	public final class Entry {
		public Entry(String title,String date,String content) {
			this.title = title;
			this.date = date;
			this.content = content;
			key = content.hashCode();
		}
		
		public final int key;
		public final String title;
		public final String date;
		public final String content;
	}
	
	List<Entry> getLatestEntries(String username, String password);
}
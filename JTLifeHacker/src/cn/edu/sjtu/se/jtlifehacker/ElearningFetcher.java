package cn.edu.sjtu.se.jtlifehacker;

import java.util.ArrayList;
import java.util.List;

public class ElearningFetcher implements NewsFetcher {

	@Override
	public List<Entry> getLatestEntries() {
		// TODO replace it with actual code
		ArrayList<Entry> test_result = new ArrayList<NewsFetcher.Entry>();
		test_result.add(new Entry("title1","2013/10/05","content1"));
		test_result.add(new Entry("title2","2013/10/05","content2"));
		return test_result;
	}

	@Override
	public String display_name() {
		return "Elearning";
	}

	@Override
	public String pref_username_id() {
		return "elearning_username";
	}

	@Override
	public String pref_password_id() {
		return "elearning_password";
	}

	@Override
	public boolean requiresLogin() {
		return true;
	}

	@Override
	public String table_name() {
		return "elearning";
	}

}

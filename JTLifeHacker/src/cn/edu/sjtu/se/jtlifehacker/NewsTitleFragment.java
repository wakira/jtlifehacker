package cn.edu.sjtu.se.jtlifehacker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NewsTitleFragment extends Fragment {
	public static final String NEWS_SOURCE = JTLifeHackerApplication.PACKAGE_NAME + ".NEWS_SOURCE";
	private ListView mTitleList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_titleview_layout, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		int pos = bundle.getInt(NEWS_SOURCE);
		System.out.println("Get user click on pos " + pos); // DEBUG USE
		
		mTitleList = (ListView) getView().findViewById(R.id.title_list);
		
		super.onActivityCreated(savedInstanceState);
	}

}

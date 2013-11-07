package cn.edu.sjtu.se.jtlifehacker;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsTitleFragment extends Fragment {
	public static final String NEWS_SOURCE = JTLifeHackerApplication.PACKAGE_NAME + ".NEWS_SOURCE";
	private ListView mTitleList;
	private List<NewsFetcher.Entry> mEntries;

	/**
	 * After view is created, inflate the layout
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_titleview_layout, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		int pos = bundle.getInt(NEWS_SOURCE);

		NewsFetcher fetcher = ((JTLifeHackerApplication)getActivity().getApplication()).mAllFetcher.get(pos);
		
		mTitleList = (ListView) getView().findViewById(R.id.title_list);
		DatabaseHelper dbhelper = new DatabaseHelper();
		mEntries = dbhelper.getEntries(getActivity(), fetcher.table_name());
		List<String> title_list = new ArrayList<String>();
		for (NewsFetcher.Entry entry : mEntries) {
			title_list.add(entry.title);
		}
		
		mTitleList.setAdapter(new ArrayAdapter<String>(
				getActivity() ,
				android.R.layout.simple_list_item_1,
				title_list));
		
		mTitleList.setOnItemClickListener(new OnNewsTitleClickListener());
		super.onActivityCreated(savedInstanceState);
	}
	
	private class OnNewsTitleClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectNewsTitle(arg2);
		}
		
	}
	
	private void selectNewsTitle(int pos) {
		Fragment new_fragment = new NewsContentFragment();
		// Take everything that should be shown to the new Fragment
		Bundle bundle = new Bundle();
		bundle.putString(NewsContentFragment.NEWS_CONTENT, mEntries.get(pos).content);
		bundle.putString(NewsContentFragment.NEWS_TITLE, mEntries.get(pos).title);
		bundle.putString(NewsContentFragment.NEWS_DATE, mEntries.get(pos).date);
		new_fragment.setArguments(bundle);
		FragmentManager fragment_manager = getFragmentManager();
		FragmentTransaction transaction = fragment_manager.beginTransaction();
		transaction.replace(R.id.content_frame, new_fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}

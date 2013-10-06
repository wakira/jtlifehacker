package cn.edu.sjtu.se.jtlifehacker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class NewsContentFragment extends Fragment {
	public static final String NEWS_CONTENT = JTLifeHackerApplication.PACKAGE_NAME + ".NEWS_CONTENT";
	public static final String NEWS_TITLE = JTLifeHackerApplication.PACKAGE_NAME + ".NEWS_TITLE";
	public static final String NEWS_DATE = JTLifeHackerApplication.PACKAGE_NAME + ".NEWS_DATE";
	
	private TextView mTitle;
	private WebView mContent;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		String content = bundle.getString(NEWS_CONTENT);
		String date = bundle.getString(NEWS_DATE);
		String title = bundle.getString(NEWS_TITLE);
		
		mTitle = (TextView) getView().findViewById(R.id.newscontent_title);
		mContent = (WebView) getView().findViewById(R.id.newscontent);
		
		mTitle.setText(title);
		mContent.loadData(content, "text/html", null);
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_contentview_layout, container, false);
	}

}

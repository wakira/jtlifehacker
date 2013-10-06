package cn.edu.sjtu.se.jtlifehacker;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mLeftDrawer;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onResume() {
		// Update left drawer lists according to selected news fetchers
		Log.d("DevDEBUG","onResume()");
		ArrayList<String> drawer_items = new ArrayList<String>();
		for(NewsFetcher fetcher : ((JTLifeHackerApplication)getApplication()).mFetchers) {
			drawer_items.add(fetcher.display_name());
		}
		mLeftDrawer.setAdapter(new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1, // TODO replace it with customized layout to chagne font and etc.
				drawer_items));
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				) {
			// override any methods if necessary
		};
		mLeftDrawer = (ListView) findViewById(R.id.left_drawer);

		mLeftDrawer.setOnItemClickListener(new OnDrawerItemClickListener());
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.content_view, menu);
		return true;
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        
        switch (item.getItemId()) {
        case R.id.action_settings:
        	startActivity(new Intent(this,SettingsActivity.class));
        	break;
        }
        
        return super.onOptionsItemSelected(item);
	}
	
	private class OnDrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mDrawerLayout.closeDrawer(mLeftDrawer);
			if (arg2 <  ((JTLifeHackerApplication)getApplication()).mFetchers.size()) {
				// user has selected a news type
				selectNewsSource(arg2);
			} else {
				// TODO otherwise...
			}
		}
	}
		
	private void selectNewsSource(int pos) {
		// TODO do fragment transaction correspondingly
		Fragment new_fragment = new NewsTitleFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(NewsTitleFragment.NEWS_SOURCE, pos);
		new_fragment.setArguments(bundle);
		FragmentManager fragment_manager = getFragmentManager();
		FragmentTransaction transaction = fragment_manager.beginTransaction();
		transaction.replace(R.id.content_frame, new_fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}

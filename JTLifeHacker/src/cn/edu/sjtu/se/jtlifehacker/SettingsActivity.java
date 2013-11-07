package cn.edu.sjtu.se.jtlifehacker;

import android.app.Activity;
import android.os.Bundle;

/**
 * SettingsActivity
 * @author kickkill
 * The simple activity for settings which contains only one preference fragment
 */
public class  SettingsActivity extends Activity {
	private SettingsFragment mSettingsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		// android.R.id.content means the root element of this view
		mSettingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, mSettingsFragment)
                .commit();
	}
}

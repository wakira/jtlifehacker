package cn.edu.sjtu.se.jtlifehacker;

import java.util.HashSet;
import java.util.Set;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.InputType;
import android.text.TextUtils;

public class SettingsFragment extends PreferenceFragment {
	private Set<String> mPopulatedUserId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		// Add 'general' preferences.
		addPreferencesFromResource(R.xml.pref_general);

		// Add 'notifications' preferences, and a corresponding header.
		PreferenceCategory fakeHeader = new PreferenceCategory((SettingsActivity)getActivity());
		fakeHeader.setTitle(R.string.pref_header_notifications);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_notification);

		// Add 'data and sync' preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory((SettingsActivity)getActivity());
		fakeHeader.setTitle(R.string.pref_header_data_sync);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_data_sync);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences to
		// their values. When their values change, their summaries are updated
		// to reflect the new value, per the Android Design guidelines.
		bindPreferenceSummaryToValue(findPreference("notifications_news_ringtone"));
		bindPreferenceSummaryToValue(findPreference("sync_frequency"));
		
		populatePasswordSetting();
    }
    
    /**
     * When password setting is clicked, open a new fragment
     */
    private void populatePasswordSetting() {
    	mPopulatedUserId = new HashSet<String>();
		PreferenceScreen screen = (PreferenceScreen)findPreference("password_setting");
		JTLifeHackerApplication application = (JTLifeHackerApplication) getActivity().getApplication();
		for (NewsFetcher fetcher : application.mAllFetcher) {
			if (fetcher.requiresLogin()) {
				if (mPopulatedUserId.contains(fetcher.pref_username_id()))
					continue;
				mPopulatedUserId.add(fetcher.pref_username_id());

				EditTextPreference user_pref = new EditTextPreference(getActivity());
				user_pref.setKey(fetcher.pref_username_id());
				user_pref.setTitle(fetcher.display_name() + " Username"); // TODO use R.string instead

				EncryptedEditTextPreference passwd_pref = new EncryptedEditTextPreference(getActivity());
				passwd_pref.setKey(fetcher.pref_password_id());
				passwd_pref.setTitle(fetcher.display_name() + " Password"); // TODO use R.string instead
				passwd_pref.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

				screen.addPreference(user_pref);
				screen.addPreference(passwd_pref);
			}
		}
    }

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 */
	private void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(mBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		mBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private Preference.OnPreferenceChangeListener mBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

			} else if (preference instanceof RingtonePreference) {
				// For ringtone preferences, look up the correct display value
				// using RingtoneManager.
				if (TextUtils.isEmpty(stringValue)) {
					// Empty values correspond to 'silent' (no ringtone).
					preference.setSummary(R.string.pref_ringtone_silent);

				} else {
					Ringtone ringtone = RingtoneManager.getRingtone(
							preference.getContext(), Uri.parse(stringValue));

					if (ringtone == null) {
						// Clear the summary if there was a lookup error.
						preference.setSummary(null);
					} else {
						// Set the summary to reflect the new ringtone display
						// name.
						String name = ringtone
								.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}

			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};
}

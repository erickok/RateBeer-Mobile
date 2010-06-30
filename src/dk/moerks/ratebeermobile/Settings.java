/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.inputmethod.EditorInfo;
import dk.moerks.ratebeermobile.receivers.BootReceiver;

public class Settings extends PreferenceActivity {
	//private static final String LOGTAG = "Settings";
	public static final String PREFERENCETAG = "RBMOBILE";
	
	private CheckBoxPreference enable, twitStatus, twitRating;
	private ListPreference interval;
	private EditTextPreference twitPass, twitUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up preferences
        getPreferenceManager().setSharedPreferencesName(PREFERENCETAG);
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        setPreferenceScreen(root);
        SharedPreferences prefs = getSharedPreferences(PREFERENCETAG, 0);
        
        // RateBeer user
        PreferenceCategory user = new PreferenceCategory(this);
        user.setTitle(R.string.settings_user);
        root.addPreference(user);

        EditTextPreference username = new EditTextPreference(this);
        username.setKey("rb_username");
        username.setTitle(R.string.settings_user_username);
        username.setDialogTitle(R.string.settings_user_username);
        username.getEditText().setSingleLine();
        user.addPreference(username);

        EditTextPreference password = new EditTextPreference(this);
        password.setKey("rb_password");
        password.setTitle(R.string.settings_user_password);
        password.setDialogTitle(R.string.settings_user_password);
        password.getEditText().setSingleLine();
        password.getEditText().setInputType(password.getEditText().getInputType() | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        user.addPreference(password);
        
        // Notifications
        PreferenceCategory notify = new PreferenceCategory(this);
        notify.setTitle(R.string.settings_notifications);
        root.addPreference(notify);

        enable = new CheckBoxPreference(this);
        enable.setKey("rb_notifications");
        enable.setTitle(R.string.settings_notifications_enable);
        enable.setSummary(R.string.settings_notifications_enable_summary);
        enable.setOnPreferenceChangeListener(notificationsChanged);
        notify.addPreference(enable);

        interval = new ListPreference(this);
        interval.setKey("rb_notifications_interval");
        interval.setTitle(R.string.settings_notifications_interval);
        interval.setSummary(R.string.settings_notifications_interval_summary);
        interval.setDialogTitle(R.string.settings_notifications_interval);
        interval.setEntries(R.array.notification_interval);
        interval.setEntryValues(R.array.notification_interval_values);
        interval.setOnPreferenceChangeListener(notificationsChanged);
        interval.setEnabled(prefs.getBoolean("rb_notifications", false));
        notify.addPreference(interval);
        
        // Twitter updates
        PreferenceCategory twitter = new PreferenceCategory(this);
        twitter.setTitle(R.string.settings_twitter);
        root.addPreference(twitter);

        twitStatus = new CheckBoxPreference(this);
        twitStatus.setKey("rb_twitter_updates");
        twitStatus.setTitle(R.string.settings_twitter_drinking);
        twitStatus.setOnPreferenceChangeListener(twitChanged);
        twitter.addPreference(twitStatus);

        twitRating = new CheckBoxPreference(this);
        twitRating.setKey("rb_twitter_ratings");
        twitRating.setTitle(R.string.settings_twitter_newrating);
        twitRating.setOnPreferenceChangeListener(twitChanged);
        twitter.addPreference(twitRating);

        twitUser = new EditTextPreference(this);
        twitUser.setKey("rb_twitter_username");
        twitUser.setTitle(R.string.settings_twitter_username);
        twitUser.setDialogTitle(R.string.settings_twitter_username);
        twitUser.getEditText().setSingleLine();
        twitUser.setEnabled(prefs.getBoolean("rb_twitter_updates", false) ||
        		prefs.getBoolean("rb_twitter_ratings", false));
        twitter.addPreference(twitUser);

        twitPass = new EditTextPreference(this);
        twitPass.setKey("rb_twitter_password");
        twitPass.setTitle(R.string.settings_twitter_password);
        twitPass.setDialogTitle(R.string.settings_twitter_password);
        twitPass.getEditText().setSingleLine();
        twitPass.getEditText().setInputType(password.getEditText().getInputType() | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        twitPass.setEnabled(prefs.getBoolean("rb_twitter_updates", false) ||
        		prefs.getBoolean("rb_twitter_ratings", false));
        twitter.addPreference(twitPass);
            	
	}
	
	private OnPreferenceChangeListener notificationsChanged = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			
			// Maintain preference dependency manually
			SharedPreferences prefs = getSharedPreferences(PREFERENCETAG, 0);
			if (preference.equals(enable)) {
				Editor editor = prefs.edit();
				editor.putBoolean("rb_notifications", (Boolean) newValue);
				editor.commit();
			}
			boolean isEnabled = prefs.getBoolean("rb_notifications", false);
			interval.setEnabled(isEnabled);
			
			// First cancel the alarm
			BootReceiver.cancelAlarm();
			// Set it up again, if needed
			if (isEnabled) {
				BootReceiver.startAlarm(getApplicationContext());
			}
			
			return true;
		}
	};

	private OnPreferenceChangeListener twitChanged = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			
			// Maintain preference dependency manully
			// Use the newValue, since both the Preference widget and stored preference itself have not yet been updated here
			boolean usingTwitter = (Boolean) newValue;
			if (preference.equals(twitStatus)) {
				usingTwitter |= twitRating.isChecked();
			} else if (preference.equals(twitRating)) {
				usingTwitter |= twitStatus.isChecked();
			}
			twitUser.setEnabled(usingTwitter);
			twitPass.setEnabled(usingTwitter);
			
			return true;
		}
	};

	public static String getUserId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Settings.PREFERENCETAG, 0);
		return prefs.getString("rb_userid", null);
	}

}

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
package dk.moerks.ratebeermobile.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	private static final String LOGTAG = "BootReceiver";

	public static final String PREFERENCETAG = "RBMOBILE";

	private static AlarmManager mgr = null;
	private static PendingIntent pi = null;
	private static SharedPreferences settings = null;
	private static long PERIOD = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOGTAG, "Starting AlarmManager");
		startAlarm(context);
	}

	public static void cancelAlarm(){
		if(mgr != null){
			mgr.cancel(pi);
		}
	}
	
	public static void startAlarm(Context context){
		if(settings == null){
			settings = context.getSharedPreferences(PREFERENCETAG, 0);
		}
		
		if(settings.getBoolean("rb_notifications", true)){
			PERIOD = getUpdateInterval(settings.getString("rb_notification_interval", "15 min"));
			
			mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			
			Intent i = new Intent(context, BeerMailServiceReceiver.class);
			
			pi = PendingIntent.getBroadcast(context, 0, i, 0);
		}
		mgr.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 20000, PERIOD, pi);
	}
	
	private static  long getUpdateInterval(String value) {
		if(value.equalsIgnoreCase("15 min")){
			return 900000;
		}
		if(value.equalsIgnoreCase("30 min")){
			return 1800000;
		}
		if(value.equalsIgnoreCase("60 min")){
			return 3600000;
		}
		return -1;
	}
}

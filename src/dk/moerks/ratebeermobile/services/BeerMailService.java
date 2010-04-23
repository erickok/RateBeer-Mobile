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
package dk.moerks.ratebeermobile.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import dk.moerks.ratebeermobile.BeerMail;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.Settings;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;

public class BeerMailService extends Service {
	private static final String LOGTAG = "BeerMailService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		SharedPreferences settings = getSharedPreferences(Settings.PREFERENCETAG, 0);

		if(settings.getBoolean("rb_notifications", true)){
			
			Thread notifyThread = new Thread(){
	    		public void run(){
					Log.d(LOGTAG, "Checking for messages");
					
					try {
						String responseString = NetBroker.doRBGet(getApplicationContext(), "http://ratebeer.com/user/messages/");
						
						boolean hasMessages = RBParser.parseNewMail(responseString);
					
						if(hasMessages){
							NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							Notification notification = new Notification(R.drawable.rbfavicon, "New Beer Mail", System.currentTimeMillis());
							notification.flags = Notification.FLAG_AUTO_CANCEL;
							Intent beermailIntent = new Intent(BeerMailService.this, BeerMail.class);
				        	notification.setLatestEventInfo(BeerMailService.this, "RateBeer Mobile","You have unread messages", PendingIntent.getActivity(getApplicationContext(), 0, beermailIntent, PendingIntent.FLAG_CANCEL_CURRENT));
					        notificationManager.notify(0, notification);
						}
					} catch(NetworkException e){
					} catch(LoginException e){
					}
	    		}
			};
			notifyThread.start();
		}
		
		stopSelf();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
}

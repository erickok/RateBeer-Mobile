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
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;

public class BeerMailService extends Service {
	private static final String LOGTAG = "BeerMailService";
	
	public static final String PREFERENCETAG = "RBMOBILE";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		SharedPreferences settings = getSharedPreferences(PREFERENCETAG, 0);

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

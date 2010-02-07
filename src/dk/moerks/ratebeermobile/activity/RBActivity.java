package dk.moerks.ratebeermobile.activity;

import android.app.ListActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class RBActivity extends ListActivity {
	private static final String LOGTAG = "RBActivity";
	
	private static boolean inProgress = false;

	public final Handler threadHandler = new Handler();
    public final Runnable update = new Runnable() {
        public void run() {
        	update();
        }
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(LOGTAG, "CONFIGURATION CHANGED (Orientation)");
	}
	
	public void indeterminateStart(String progressMessage){
		setTitle(progressMessage);
		setProgressBarIndeterminateVisibility(true);
		setInProgress(true);
	}
	
	public void indeterminateStop(){
		setTitle("RateBeer Mobile");
		setProgressBarIndeterminateVisibility(false);
		setInProgress(false);
	}
	
	public void alertUser(String message){
		Looper.prepare();
       	Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
   		toast.show();
   		threadHandler.post(update);
		Looper.loop();
	}
	
	protected void update(){
		indeterminateStop();
	}

	//Accessors
	public static void setInProgress(boolean inProgress) {
		RBActivity.inProgress = inProgress;
	}

	public static boolean isInProgress() {
		return inProgress;
	}
}

package dk.moerks.ratebeermobile.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class RBActivity extends ListActivity {
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
	
	public void indeterminateStart(String progressMessage){
		setTitle(progressMessage);
		setProgressBarIndeterminateVisibility(true);
	}
	
	public void indeterminateStop(){
		setTitle("RateBeer Mobile");
		setProgressBarIndeterminateVisibility(false);
	}
	
	public void alertUser(String message){
		//Looper.prepare();
       	Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
   		toast.show();
   		//threadHandler.post(update);
		//Looper.loop();
	}
	
	protected void update(){
		indeterminateStop();
	}
}

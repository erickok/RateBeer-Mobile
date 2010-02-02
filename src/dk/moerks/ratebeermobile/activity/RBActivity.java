package dk.moerks.ratebeermobile.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

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
	
	protected void update(){
		indeterminateStop();
	}
}

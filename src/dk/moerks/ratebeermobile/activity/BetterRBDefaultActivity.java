package dk.moerks.ratebeermobile.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.github.droidfu.activities.BetterDefaultActivity;

import dk.moerks.ratebeermobile.exceptions.RBException;

public class BetterRBDefaultActivity extends BetterDefaultActivity implements BetterRBActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}
	
	public void reportError(RBException e) {

		// Show the error as Toast popup
		Toast.makeText(this, e.getAlertMessage(), Toast.LENGTH_LONG).show();
		
	}

	public Context getContext() {
		return this;
	}
	
	public void setTitle(String message) {
		super.setTitle(message);
	}

}

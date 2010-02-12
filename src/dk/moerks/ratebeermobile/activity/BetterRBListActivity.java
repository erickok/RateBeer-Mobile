package dk.moerks.ratebeermobile.activity;

import android.R;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidfu.activities.BetterListActivity;

import dk.moerks.ratebeermobile.exceptions.RBException;

public class BetterRBListActivity extends BetterListActivity implements BetterRBActivity {
	private static String userId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}
	
	public void reportError(RBException e) {
		
		// Show the error as Toast popup
		Toast.makeText(this, e.getAlertMessage(), Toast.LENGTH_LONG).show();
		
		if (getListAdapter().getCount() <= 0) {
			// No items shown: it's safe to also display the error message as the list view's empty text
			((TextView)findViewById(R.id.empty)).setText(e.getAlertMessage());
		}
		
	}

	public Context getContext() {
		return this;
	}
	
	public void setTitle(String message) {
		super.setTitle(message);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userid) {
		BetterRBListActivity.userId = userid;
	}
}

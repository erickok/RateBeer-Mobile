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
	
	private static final String EXTRA_HAS_TASK = "has_running_task";
	private static String userId;
	private boolean hasTask;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_HAS_TASK, hasRunningTask());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hasRunningTask(savedInstanceState.getBoolean(EXTRA_HAS_TASK));
        this.setProgressBarIndeterminateVisibility(hasRunningTask());
    }

	public void reportError(RBException e) {
		
		// Show the error as Toast popup
		Toast.makeText(this, e.getAlertMessage(), Toast.LENGTH_LONG).show();
		
		if (getListAdapter() == null || getListAdapter().getCount() <= 0) {
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

	public void hasRunningTask(boolean hasTask) {
		this.hasTask = hasTask;
	}

	public boolean hasRunningTask() {
		return this.hasTask;
	}
	
}

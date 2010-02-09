package dk.moerks.ratebeermobile.activity;

import android.content.Context;

import com.github.droidfu.activities.BetterActivity;

import dk.moerks.ratebeermobile.exceptions.RBException;

public interface BetterRBActivity extends BetterActivity {

	/**
	 * Should show an appropriate error message to the end user.
	 * @param e The exception that was thrown
	 */
	public abstract void reportError(RBException e);
	
	/**
	 * Should return the implementing activity's context
	 * @return The activity context, viz. itself
	 */
	public abstract Context getContext();

	/**
	 * Should set the activity's title
	 * @param message The message to show in the title bar
	 */
	public abstract void setTitle(String message);
	
}

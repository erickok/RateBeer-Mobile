package dk.moerks.ratebeermobile.task;

import android.content.Context;

import com.github.droidfu.concurrent.BetterAsyncTask;

import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.exceptions.RBException;

public abstract class BetterRBTask<ParameterT, ReturnT> extends BetterAsyncTask<ParameterT, Void, ReturnT> {

	private BetterRBActivity activity;
	private String progressMessage;
	
	public BetterRBTask(BetterRBActivity activity, String progressMessage) {
		super(activity.getContext());
		this.activity = activity;
		this.progressMessage = progressMessage;
	}

	@Override
	protected final void before(Context context) {
		// Show a progress indicator text in the application title bar
		activity.setTitle(progressMessage);
	}
	
	@Override
	protected final void handleError(Context context, Exception error) {
		// Always let the activity report any errors
		activity.reportError((RBException) error);
	}

	@Override
	protected final void after(Context context, ReturnT result) {
		// Show a progress indicator text in the application title bar
		activity.setTitle(context.getText(R.string.app_name).toString());
		afterTask(activity, result);
	}

	/**
	 * Should implement the actual showing of the task results, such as showing the 
	 * returned/parsed list of search results. 
	 * @param activity The original RB activity that started the tast
	 * @param result The task results
	 */
	protected abstract void afterTask(BetterRBActivity activity, ReturnT result);
	
}

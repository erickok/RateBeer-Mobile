package dk.moerks.ratebeermobile.task;

import android.content.Context;
import android.graphics.drawable.Drawable;
import dk.moerks.ratebeermobile.BeerView;
import dk.moerks.ratebeermobile.activity.BetterRBActivity;
import dk.moerks.ratebeermobile.io.NetBroker;

public class RetrieveBeerImageTask extends BetterRBTask<String, Drawable> {

	public RetrieveBeerImageTask(BetterRBActivity activity) {
		super(activity, "Fetching Beer Image");
	}

	@Override
	protected Drawable doCheckedInBackground(Context context, String... params) throws Exception {
		// Get a specific beer image
		Drawable responseDrawable = NetBroker.doGetImage(context, null, "http://www.ratebeer.com/beerimages/"+params[0]+".jpg");
		return responseDrawable;
	}
	
	@Override
	protected void afterTask(BetterRBActivity activity, Drawable result) {
		((BeerView)activity).onImageRetrieved(result);
	}

}

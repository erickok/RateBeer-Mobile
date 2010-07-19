package dk.moerks.ratebeermobile;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.ReviewAdapter;
import dk.moerks.ratebeermobile.task.RetrieveBeerImageTask;
import dk.moerks.ratebeermobile.task.RetrieveBeerInfoTask;
import dk.moerks.ratebeermobile.task.RetrieveBeerReviewsTask;
import dk.moerks.ratebeermobile.task.SetDrinkingBeerIdTask;
import dk.moerks.ratebeermobile.util.StringUtils;
import dk.moerks.ratebeermobile.vo.BeerInfo;
import dk.moerks.ratebeermobile.vo.Review;

public class BeerView extends BetterRBListActivity {
    private static final String LOGTAG = "BeerView";
	String beerId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.beerview);
    	
        final String beername;
        final boolean isRated;
        final String rateCount;
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	beername = extras.getString("BEERNAME");
        	beerId = extras.getString("BEERID");
        	isRated = extras.getBoolean("ISRATED");
        	rateCount = extras.getString("RATINGS");
        } else {
        	beername = null;
        	beerId = null;
        	isRated = false;
        	rateCount = null;
        }

        Button rateButton = (Button) findViewById(R.id.viewRateButton);
        if(isRated){
        	rateButton.setText(R.string.view_rate_button_rated);
        }
        
        rateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent rateIntent = null;
				if(isRated){
					rateIntent = new Intent(BeerView.this, Rating.class);
				} else {
					rateIntent = new Intent(BeerView.this, Rate.class);
				}
	        	rateIntent.putExtra("BEERNAME", beername);
	        	rateIntent.putExtra("BEERID", beerId);
	        	startActivity(rateIntent);  
			}
		});

        Button drinkingButton = (Button) findViewById(R.id.viewDrinkingButton);
        drinkingButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
	        	new SetDrinkingBeerIdTask(BeerView.this).execute(beername);
			}
		});
        
        TextView beernameText = (TextView) findViewById(R.id.beerview_value_beername);
        beernameText.setText(beername);

        TextView ratingsText = (TextView) findViewById(R.id.beerview_value_ratings);
        ratingsText.setText(rateCount);

        new RetrieveBeerInfoTask(this).execute(beerId);
        new RetrieveBeerReviewsTask(this).execute(beerId);
        new RetrieveBeerImageTask(this).execute(beerId);
    }

	public void onResultsRetrieved(List<Review> results) {
    	if (results != null) {
    		setListAdapter(new ReviewAdapter(this, results));
			if (results.size() <= 0) {
	    		// If no beers were found, show this in a text message
				((TextView)findViewById(android.R.id.empty)).setText(R.string.view_no_reviews_found);
	    	}
    	}
	}

	public void onInfoRetrieved(BeerInfo result) {

		TextView brewerView = (TextView)findViewById(R.id.beerview_value_brewer);
		brewerView.setText(getText(R.string.view_brewer) + " " + result.getBrewerName());
		
		TextView styleView = (TextView)findViewById(R.id.beerview_value_style);
		styleView.setText(getText(R.string.view_style) + " " + StringUtils.cleanHtml(result.getBeerStyleName()));
		
		
		TextView pctlView = (TextView)findViewById(R.id.beerview_value_overallpctl);
		pctlView.setText(""+ (int)result.getOverallPctl());
		
		TextView abvView = (TextView)findViewById(R.id.beerview_value_abv);
		abvView.setText(result.getAbv() + "%");
	}

	public void onDrinkingStatusUpdated(String result) {
		Intent homeIntent = new Intent(BeerView.this, Home.class);
		startActivity(homeIntent);
	}

	public void onImageRetrieved(Drawable result) {
		Log.d(LOGTAG, "Setting Beer Image");
		ImageView imageView = (ImageView)findViewById(R.id.beerview_image);
		imageView.setImageDrawable(result);
	}
}

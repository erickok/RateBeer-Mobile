package dk.moerks.ratebeermobile;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.ReviewAdapter;
import dk.moerks.ratebeermobile.task.RetrieveBeerReviewsTask;
import dk.moerks.ratebeermobile.vo.Review;

public class BeerView extends BetterRBListActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.beerview);
    	
        final String beername;
        final String beerId;
        final boolean isRated;
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	beername = extras.getString("BEERNAME");
        	beerId = extras.getString("BEERID");
        	isRated = extras.getBoolean("ISRATED");
        } else {
        	beername = null;
        	beerId = null;
        	isRated = false;
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

        
        TextView beernameText = (TextView) findViewById(R.id.beerview_value_beername);
        beernameText.setText(beername);

        new RetrieveBeerReviewsTask(this).execute(beerId);
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
}

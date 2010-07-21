/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private String beerId;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.beerview);
    	
    	// Determine beer to show from the given intent
        final String beername;
        final boolean isRated;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	beername = extras.getString("BEERNAME");
        	beerId = extras.getString("BEERID");
        	isRated = extras.getBoolean("ISRATED");
        } else {
        	beername = null;
        	beerId = null;
        	isRated = false;
        }

		// Inflate the beer details layout and add it to the list view as top item
		LinearLayout details = (LinearLayout) getLayoutInflater().inflate(R.layout.beerdetails, null);
		getListView().addHeaderView(details);
        
        // Assign button listeners
        Button rateButton = (Button) getListView().findViewById(R.id.view_rate_button);
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
        Button drinkingButton = (Button) getListView().findViewById(R.id.view_drinking_button);
        drinkingButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
	        	new SetDrinkingBeerIdTask(BeerView.this).execute(beername);
			}
		});
        
        // Set the title and 'loading reviews' text
        TextView beernameText = (TextView) findViewById(R.id.beerview_value_beername);
        beernameText.setText(beername);
        TextView emptyText = (TextView) findViewById(android.R.id.empty);
        emptyText.setText(R.string.task_loadreviews);

        // Start retrieve tasks
        new RetrieveBeerInfoTask(this).execute(beerId);
        new RetrieveBeerImageTask(this).execute(beerId);
        new RetrieveBeerReviewsTask(this).execute(beerId);
        
    }

	public void onReviewsRetrieved(List<Review> results) {
    	if (results != null) {
    		setListAdapter(new ReviewAdapter(this, results));
			if (results.size() <= 0) {
	    		// If no beers were found, show this in a text message
				((TextView)findViewById(android.R.id.empty)).setText(R.string.view_no_reviews_found);
	    	}
    	}
	}

	public void onInfoRetrieved(BeerInfo result) {

		// Show score card, which is in the listview's first item (the header)
		View card = getListView().findViewById(R.id.beerview_scorecard);
		card.setVisibility(View.VISIBLE);
		
		// Fill in the retrieved details
		TextView brewerView = (TextView)findViewById(R.id.beerview_value_brewer);
		brewerView.setText(getText(R.string.view_brewer) + " " + result.getBrewerName());		
		TextView styleView = (TextView)getListView().findViewById(R.id.beerview_value_style);
		styleView.setText(getText(R.string.view_style) + " " + StringUtils.cleanHtml(result.getBeerStyleName()));
		TextView descView = (TextView)getListView().findViewById(R.id.beerview_value_description);
		descView.setText(StringUtils.cleanHtml(result.getBeerStyleName()));

		TextView pctlView = (TextView)getListView().findViewById(R.id.beerview_value_overallpctl);
		pctlView.setText(""+ (int)result.getOverallPctl());
		TextView pctlRatings = (TextView)getListView().findViewById(R.id.beerview_value_ratings);
		pctlRatings.setText(getIntent().getStringExtra("RATINGS")); // This wasn't in the info, but rather given by the Search activity
		TextView abvView = (TextView)getListView().findViewById(R.id.beerview_value_abv);
		abvView.setText(result.getAbv() + "%");
		
	}

	public void onDrinkingStatusUpdated(String result) {
		Intent homeIntent = new Intent(BeerView.this, Home.class);
		startActivity(homeIntent);
	}

	public void onImageRetrieved(Drawable result) {
		// Show the retrieved beer image in the listview's first item (the header)
		Log.d(LOGTAG, "Setting Beer Image");
		ImageView imageView = (ImageView)getListView().findViewById(R.id.beerview_image);
		imageView.setImageDrawable(result);
	}
	
}

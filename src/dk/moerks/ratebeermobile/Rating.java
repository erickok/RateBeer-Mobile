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

import android.os.Bundle;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;
import dk.moerks.ratebeermobile.task.RetrieveRatingTask;
import dk.moerks.ratebeermobile.vo.RatingData;

public class Rating extends BetterRBDefaultActivity {
	//private static final String LOGTAG = "Rating";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);

        String beername = null;
        final String beerId;
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	beername = extras.getString("BEERNAME");
        	beerId = extras.getString("BEERID");
        } else {
        	beerId = null;
        }
        
        TextView beernameText = (TextView) findViewById(R.id.rating_label_beername);
        beernameText.setText(beername);
        
        new RetrieveRatingTask(this).execute(beerId);
	}
	
	public void onRatingRetrieved(RatingData rating) {
        TextView aromaText = (TextView) findViewById(R.id.rating_value_aroma);
        TextView appearanceText = (TextView) findViewById(R.id.rating_value_appearance);
        TextView flavorText = (TextView) findViewById(R.id.rating_value_flavor);
        TextView palateText = (TextView) findViewById(R.id.rating_value_palate);
        TextView overallText = (TextView) findViewById(R.id.rating_value_overall);
        TextView totalscoreText = (TextView) findViewById(R.id.rating_value_totalscore);
        TextView commentText = (TextView) findViewById(R.id.rating_value_comment);
        
        aromaText.setText(rating.getAroma());
        appearanceText.setText(rating.getAppearance());
        flavorText.setText(rating.getFlavor());
        palateText.setText(rating.getPalate());
        overallText.setText(rating.getOverall());
        totalscoreText.setText(rating.getTotalscore());
        commentText.setText(rating.getComment());
	}
}

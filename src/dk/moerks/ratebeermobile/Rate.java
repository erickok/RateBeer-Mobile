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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;
import dk.moerks.ratebeermobile.task.PostTwitterStatusTask;
import dk.moerks.ratebeermobile.task.SaveRatingTask;

public class Rate extends BetterRBDefaultActivity {
	@SuppressWarnings("unused")
	private static final String LOGTAG = "Rate";
    String beername =  null;
    String beerid =  null;
	
    private TextView rateCharleftText = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate);
        
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	beername = extras.getString("BEERNAME");
        	beerid = extras.getString("BEERID");
        }
        
        rateCharleftText = (TextView) findViewById(R.id.rate_label_charleft);
        rateCharleftText.setText(getText(R.string.rate_charleft) + " 75");
        EditText rateComment = (EditText) findViewById(R.id.rate_value_comments);
        rateComment.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int charNumber = s.length();
				int resultNumber = 75 - charNumber;
				if(resultNumber > 0){
					rateCharleftText.setText(getText(R.string.rate_charleft) + " " + resultNumber);
				} else {
					rateCharleftText.setText("");
				}
			}
        });
        
        TextView beernameText = (TextView) findViewById(R.id.rate_label_beername);
        beernameText.setText(beername);
        
        Button rateButton = (Button) findViewById(R.id.rate_button);
        rateButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                
            	EditText comment = (EditText) findViewById(R.id.rate_value_comments);
            	final String commentString = comment.getText().toString();
                
    			if(commentString.length() > 74){

                	Spinner aromaText = (Spinner) findViewById(R.id.rate_value_aroma);
                	Spinner appearanceText = (Spinner) findViewById(R.id.rate_value_appearance);
                	Spinner flavorText = (Spinner) findViewById(R.id.rate_value_flavor);
                	Spinner palateText = (Spinner) findViewById(R.id.rate_value_palate);
                	Spinner overallText = (Spinner) findViewById(R.id.rate_value_overall);
                	
                	final String aromaString = (String)aromaText.getSelectedItem();
                	final String appearanceString = (String)appearanceText.getSelectedItem();
                	final String flavorString = (String)flavorText.getSelectedItem();
                	final String palateString = (String)palateText.getSelectedItem();
                	final String overallString = (String)overallText.getSelectedItem();
                	String totalScore = calculateTotalScore(aromaString, appearanceString, flavorString, palateString, overallString);
                	
	    			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
	    			parameters.add(new BasicNameValuePair("BeerID", beerid));  
	    			parameters.add(new BasicNameValuePair("aroma", aromaString));  
	    			parameters.add(new BasicNameValuePair("appearance", appearanceString));  
	    			parameters.add(new BasicNameValuePair("flavor", flavorString));  
	    			parameters.add(new BasicNameValuePair("palate", palateString));  
	    			parameters.add(new BasicNameValuePair("overall", overallString));
	    			parameters.add(new BasicNameValuePair("totalscore", totalScore));
	    			parameters.add(new BasicNameValuePair("Comments", commentString));
	    			
	    			new SaveRatingTask(Rate.this).execute(parameters.toArray(new NameValuePair[] {}));
	    			
	    			SharedPreferences prefs = getSharedPreferences(Settings.PREFERENCETAG, 0);
	    			if (prefs.getBoolean("rb_twitter_ratings", false)) {
	    				new PostTwitterStatusTask(Rate.this).execute(buildTwitterMessage(totalScore));
	    			}
	    			
	    			finish();
	    			
    			} else {
    				Toast.makeText(Rate.this, R.string.toast_minimum_length, Toast.LENGTH_LONG).show();
    			}
            }
        	
        	private String buildTwitterMessage(String score) {
        		return getString(R.string.twitter_rating_message, beername, score, getUserId());
        	}

			private String calculateTotalScore(String aromaString, String appearanceString, String flavorString, String palateString, String overallString) {
				int aroma = Integer.parseInt(aromaString);
				int appearance = Integer.parseInt(appearanceString);
				int flavor = Integer.parseInt(flavorString);
				int palate = Integer.parseInt(palateString);
				int overall = Integer.parseInt(overallString);
				
				int total = (aroma + appearance + flavor + palate + overall);

				float totalscore =  ((float)total) / 10;
				String result = "" + totalscore;
				return result;
			}
        });
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
		TextView beernameText = (TextView) findViewById(R.id.rate_label_beername);
		beernameText.setFocusable(true);
		beernameText.setFocusableInTouchMode(true);
		beernameText.requestFocus();

    }

	public void onRatingSaved() {
		Toast.makeText(getApplicationContext(), getText(R.string.toast_rate_success), Toast.LENGTH_SHORT).show();
	}
	
}

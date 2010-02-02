package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.io.NetBroker;

public class Rate extends Activity {
	@SuppressWarnings("unused")
	private static final String LOGTAG = "Rate";
    String beerid =  null;
	
    private TextView rateCharleftText = null;
    
    final Handler threadHandler = new Handler();
    // Create runnable for posting
    final Runnable clearIndeterminateProgress = new Runnable() {
        public void run() {
        	clearIndeterminateProgress();
        }
    };

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.rate);
        
        String beername = null;
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
                setProgressBarIndeterminateVisibility(true);

            	Spinner aromaText = (Spinner) findViewById(R.id.rate_value_aroma);
            	Spinner appearanceText = (Spinner) findViewById(R.id.rate_value_appearance);
            	Spinner flavorText = (Spinner) findViewById(R.id.rate_value_flavor);
            	Spinner palateText = (Spinner) findViewById(R.id.rate_value_palate);
            	Spinner overallText = (Spinner) findViewById(R.id.rate_value_overall);
            	EditText comment = (EditText) findViewById(R.id.rate_value_comments);
            	
            	final String aromaString = (String)aromaText.getSelectedItem();
            	final String appearanceString = (String)appearanceText.getSelectedItem();
            	final String flavorString = (String)flavorText.getSelectedItem();
            	final String palateString = (String)palateText.getSelectedItem();
            	final String overallString = (String)overallText.getSelectedItem();
            	final String commentString = comment.getText().toString();
                
            	Thread sendThread = new Thread(){
            		public void run(){
            			Looper.prepare();
		            	
		    			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		    			parameters.add(new BasicNameValuePair("BeerID", beerid));  
		    			parameters.add(new BasicNameValuePair("aroma", aromaString));  
		    			parameters.add(new BasicNameValuePair("appearance", appearanceString));  
		    			parameters.add(new BasicNameValuePair("flavor", flavorString));  
		    			parameters.add(new BasicNameValuePair("palate", palateString));  
		    			parameters.add(new BasicNameValuePair("overall", overallString));
		    			parameters.add(new BasicNameValuePair("totalscore", calculateTotalScore(aromaString, appearanceString, flavorString, palateString, overallString)));
		    			parameters.add(new BasicNameValuePair("Comments", commentString));
		    			
		    			if(commentString.length() > 74){
		    				String response = NetBroker.doRBPost(getApplicationContext(), "http://www.ratebeer.com/saverating.asp", parameters);
		   				
			   				if(response != null){
			   					threadHandler.post(clearIndeterminateProgress);
			   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_rate_success), Toast.LENGTH_SHORT);
			   					toast.show();
			   	            	Intent homeIntent = new Intent(Rate.this, Home.class);  
			   	            	startActivity(homeIntent);  
			   				} else {
			   					threadHandler.post(clearIndeterminateProgress);
			   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_rate_failure), Toast.LENGTH_LONG);
			   					toast.show();
			   				}
		    			} else {
		    				threadHandler.post(clearIndeterminateProgress);
		   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_rate_shortcomment), Toast.LENGTH_LONG);
		   					toast.show();
		    			}
		    			Looper.loop();
            		}
            	};
                sendThread.start();
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

    private void clearIndeterminateProgress() {
		setProgressBarIndeterminateVisibility(false);
	}
}

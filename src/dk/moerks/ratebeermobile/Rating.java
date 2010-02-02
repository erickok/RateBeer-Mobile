package dk.moerks.ratebeermobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.RBActivity;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.RatingData;

public class Rating extends RBActivity {
	private static final String LOGTAG = "Rating";
	private RatingData rating = null;
	
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
        
        indeterminateStart("Retrieving Rating...");    	
    	Thread ratingThread = new Thread(){
    		public void run(){
    			Log.d(LOGTAG, "ID: " + beerId);
    			try {
        			String responseString = NetBroker.doRBGet(getApplicationContext(), "http://www.ratebeer.com/beer/rate/" + beerId + "/");
    				rating = RBParser.parseRating(responseString);
    			} catch(RBParserException e){
    			} catch(NetworkException e){
    			} catch(LoginException e){
    				alertUser(e.getAlertMessage());
    			}
    			threadHandler.post(update);
    		}
    	};
    	ratingThread.start();
	}
	
	protected void update(){
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
        indeterminateStop();
	}
}

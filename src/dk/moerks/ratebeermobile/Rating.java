package dk.moerks.ratebeermobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.RatingData;

public class Rating extends Activity {
	private static final String LOGTAG = "Rating";
	private ProgressDialog ratingDialog = null;
	private RatingData rating = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);

        String beername = null;
        final String beerUrl;
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	beername = extras.getString("BEERNAME");
        	beerUrl = extras.getString("BEERURL");
        } else {
        	beerUrl = null;
        }
        
        TextView beernameText = (TextView) findViewById(R.id.rating_label_beername);
        beernameText.setText(beername);
        
    	ratingDialog = ProgressDialog.show(Rating.this, getText(R.string.rating_retrieving), getText(R.string.rating_retrieving_text));    	
    	ratingDialog.setOnDismissListener(new ProgressDialog.OnDismissListener(){
    		public void onDismiss(DialogInterface dialog){
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
    	});

    	Thread ratingThread = new Thread(){
    		public void run(){
    			Log.d(LOGTAG, "URL: " + beerUrl);
    			String responseString = NetBroker.doGet(getApplicationContext(), "http://www.ratebeer.com" + beerUrl);
    			try {
    				rating = RBParser.parseRating(responseString);
    			} catch(RBParserException e){
					Log.e(LOGTAG, "There was an error parsing rating data");
    				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_parse_error), Toast.LENGTH_LONG);
   					toast.show();
    			}
    			ratingDialog.dismiss();
    		}
    	};
    	ratingThread.start();


	}
}

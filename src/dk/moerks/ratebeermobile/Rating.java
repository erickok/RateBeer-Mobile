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

package dk.moerks.ratebeermobile.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.vo.Review;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class ReviewAdapter extends ArrayAdapter<Review> {
	Activity context;
	List<Review> results;
	
	public ReviewAdapter(Activity context, List<Review> results) {
		super(context, R.layout.beerview_row, results);
		
		this.context = context;
		this.results = results;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		
		try {
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(this.context);
				row = inflater.inflate(R.layout.beerview_row, null);
			}
			
			//Total Score
			TextView totalScoreView = (TextView)row.findViewById(R.id.beerview_row_value_total);
			totalScoreView.setText(results.get(position).getTotalScore());
			
			//Aroma
			TextView aromaView = (TextView)row.findViewById(R.id.beerview_row_value_aroma);
			aromaView.setText(results.get(position).getAroma());

			//Appearance
			TextView appearanceView = (TextView)row.findViewById(R.id.beerview_row_value_appearance);
			appearanceView.setText(results.get(position).getAppearance());
			
			//Taste
			TextView tasteView = (TextView)row.findViewById(R.id.beerview_row_value_taste);
			tasteView.setText(results.get(position).getFlavor());
			
			//Palate
			TextView palateView = (TextView)row.findViewById(R.id.beerview_row_value_palate);
			palateView.setText(results.get(position).getMouthfeel());
			
			//Overall
			TextView overallView = (TextView)row.findViewById(R.id.beerview_row_value_overall);
			overallView.setText(results.get(position).getOverall());
			
			//Username (RATINGS), CITY, COUNTRY
			TextView usernameView = (TextView)row.findViewById(R.id.beerview_row_value_username);
			usernameView.setText(results.get(position).getUserName() + " (" + results.get(position).getRateCount() + "), " + results.get(position).getCity() + ", " + results.get(position).getCountry());

			//Comments
			TextView commentsView = (TextView)row.findViewById(R.id.beerview_row_value_comments);
			commentsView.setText(results.get(position).getComments());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}
}

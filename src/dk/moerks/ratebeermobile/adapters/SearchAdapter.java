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
import dk.moerks.ratebeermobile.vo.SearchResult;

public class SearchAdapter extends ArrayAdapter<SearchResult> {
	Activity context;
	List<SearchResult> results;
	
	public SearchAdapter(Activity context, List<SearchResult> results){
		super(context, R.layout.search_row, results);
		
		this.context = context;
		this.results = results;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		
		try {
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(this.context);
				row = inflater.inflate(R.layout.search_row, null);
			}
			
			ImageView icon = (ImageView) row.findViewById(R.id.search_row_icon);
			if(results.get(position).isRated()){
				icon.setImageResource(R.drawable.rated);
			} else {
				icon.setImageResource(R.drawable.notrated);
			}
			
			TextView name = (TextView)row.findViewById(R.id.search_row_name);
			name.setText(results.get(position).getBeerName());
			
			TextView percentile = (TextView)row.findViewById(R.id.search_row_percentile);
			if(results.get(position).getBeerPercentile().equalsIgnoreCase("")){
				percentile.setText("Score\nN/A");
			} else {
				percentile.setText("Score\n" + results.get(position).getBeerPercentile());
			}
			
			TextView ratings = (TextView)row.findViewById(R.id.search_row_ratings);
			ratings.setText("Ratings\n" + results.get(position).getBeerRatings());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}
}

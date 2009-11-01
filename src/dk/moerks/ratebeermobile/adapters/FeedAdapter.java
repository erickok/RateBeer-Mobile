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
import dk.moerks.ratebeermobile.vo.Feed;

public class FeedAdapter  extends ArrayAdapter<Feed>{
	Activity context;
	List<Feed> feeds;

	public FeedAdapter(Activity context, List<Feed> feeds) {
		super(context, R.layout.feed_row, feeds);

		this.context = context;
		this.feeds = feeds;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		
		try {
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(this.context);
				row = inflater.inflate(R.layout.feed_row, null);
			}
			
			//TODO: Finish setting the icon according to the right type. Types will have to be defined in the Feed VO
			ImageView icon = (ImageView) row.findViewById(R.id.feed_row_icon);
			if(feeds.get(position).getType().equalsIgnoreCase(Feed.ADD_BEER_TYPE)){
				icon.setImageResource(R.drawable.rated);
			} else {
				icon.setImageResource(R.drawable.notrated);
			}

			
			TextView text = (TextView)row.findViewById(R.id.feed_row_text);
			text.setText(feeds.get(position).getValue());

			TextView date = (TextView)row.findViewById(R.id.feed_row_date);
			date.setText(feeds.get(position).getDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}

}

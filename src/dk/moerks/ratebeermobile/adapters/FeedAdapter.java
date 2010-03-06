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
package dk.moerks.ratebeermobile.adapters;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.vo.Feed;

public class FeedAdapter  extends ArrayAdapter<Feed>{
	private static final String LOGTAG = "FeedAdapter";
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
			TextView date = (TextView) row.findViewById(R.id.feed_row_date);
			TextView text = (TextView) row.findViewById(R.id.feed_row_text);
			
			Feed feed = feeds.get(position);
			
			Log.d(LOGTAG, "FEED TYPE: " + feed.getType());
			
			//Added Beer
			if(feed.getType().equalsIgnoreCase(Feed.ADD_BEER_TYPE)){
				icon.setImageResource(R.drawable.added_beer);

				String textString = feed.getFriend() + " added " + feed.getBeer(); 
				text.setText(textString);

				date.setText(feed.getDate() + " (" + feed.getActivityTime() + ")");
			}
			
			//Rated Beer
			if(feed.getType().equalsIgnoreCase(Feed.RATED_BEER_TYPE)){
				icon.setImageResource(R.drawable.rated_pen);
				String textString = feed.getFriend() + " rated " + feed.getBeer() + " (" + feed.getScore() + ")"; 
				text.setText(textString);

				date.setText(feed.getDate() + " (" + feed.getActivityTime() + ")");
			}
			
			//Milestone
			if(feed.getType().equalsIgnoreCase(Feed.MILESTONE_REACHED_TYPE)){
				icon.setImageResource(R.drawable.star);

				String textString = feed.getFriend() + " reached " + feed.getRatings() + " ratings!"; 
				text.setText(textString);

				date.setText(feed.getDate() + " (" + feed.getActivityTime() + ")");
			}
			
			//Place Review
			if(feed.getType().equalsIgnoreCase(Feed.REVIEWED_PLACE_TYPE)){
				icon.setImageResource(R.drawable.reviewed);

				String textString = feed.getFriend() + " reviewed " + feed.getPlace() + " (" + feed.getScore() + ")"; 
				text.setText(textString);

				date.setText(feed.getDate() + " (" + feed.getActivityTime() + ")");
			}

			//Bio Update
			if(feed.getType().equalsIgnoreCase(Feed.UPDATED_BIO_TYPE)){
				icon.setImageResource(R.drawable.action_check);

				String textString = feed.getFriend() + " updated his user bio"; 
				text.setText(textString);

				date.setText(feed.getDate() + " (" + feed.getActivityTime() + ")");
			}

			//Attending
			if(feed.getType().equalsIgnoreCase(Feed.ATTENDING_TYPE)){
				icon.setImageResource(R.drawable.event);

				String textString = feed.getFriend() + " is attending " + feed.getEvent(); 
				text.setText(textString);

				date.setText(feed.getDate() + " (" + feed.getActivityTime() + ")");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}

}

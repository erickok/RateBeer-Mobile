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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.util.StringUtils;
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class PlacesAdapter extends ArrayAdapter<PlacesInfo> {
	Activity context;
	List<PlacesInfo> places;

	public PlacesAdapter(Activity context, List<PlacesInfo> places) {
		super(context, R.layout.places_row, places);

		this.context = context;
		this.places = places;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		
		try {
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(this.context);
				row = inflater.inflate(R.layout.places_row, null);
			}
			
			//Place Name
			TextView name = (TextView)row.findViewById(R.id.place_name);
			name.setText(places.get(position).getPlaceName());
			
			//Place Rating
			TextView rating = (TextView)row.findViewById(R.id.place_rating);
			String avgRating = places.get(position).getAvgRating();
			if(avgRating != null && !avgRating.contains("null")){
				rating.setText(context.getText(R.string.place_avgrating)+ " " + StringUtils.roundNumberString(avgRating));
			} else {
				rating.setText(R.string.no_place_ratings);
			}

			//Place Distance
			TextView distance = (TextView)row.findViewById(R.id.place_distance);
			distance.setText(StringUtils.roundNumberString(places.get(position).getDistance())+"mi");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}
	
}

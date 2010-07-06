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
import android.widget.ImageView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.util.StringUtils;
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
			name.setText(StringUtils.cleanHtml(results.get(position).getBeerName()));
			
			TextView percentile = (TextView)row.findViewById(R.id.search_row_percentile);
			if(results.get(position).getBeerPercentile().equalsIgnoreCase("")){
				percentile.setText("Score\nN/A");
			} else {
				percentile.setText("Score\n" + results.get(position).getBeerPercentile());
			}
			
			TextView ratings = (TextView)row.findViewById(R.id.search_row_ratings);
			ratings.setText("Ratings\n" + results.get(position).getBeerRatings());
			
			TextView additional = (TextView)row.findViewById(R.id.search_row_additional);
			if(results.get(position).isAlias() && results.get(position).isRetired()){
				additional.setText("Information\nRet. & Alias");
			}
			if(results.get(position).isAlias() && !results.get(position).isRetired()){
				additional.setText("Information\nAlias");
			}
			if(!results.get(position).isAlias() && results.get(position).isRetired()){
				additional.setText("Information\nRetired");
			}
			if(!results.get(position).isAlias() && !results.get(position).isRetired()){
				additional.setText("Information\nAvailable");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}
}

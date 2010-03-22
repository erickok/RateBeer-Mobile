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
package dk.moerks.ratebeermobile;

import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.SearchAdapter;
import dk.moerks.ratebeermobile.services.BeerSearchHistoryProvider;
import dk.moerks.ratebeermobile.task.SearchTask;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class Search extends BetterRBListActivity {
	private static final String LOGTAG = "Search";

	private SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, 
			BeerSearchHistoryProvider.AUTHORITY, BeerSearchHistoryProvider.MODE);
	private TextView empty;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.search);

        empty = (TextView) findViewById(android.R.id.empty);
        empty.setText(R.string.search_searching);

        handleIntent(getIntent());
        
    }

    /** Called when a new intent is delivered */
    @Override
    public void onNewIntent(final Intent newIntent) {
        super.onNewIntent(newIntent);
        
        handleIntent(newIntent);
    }

    private void handleIntent(Intent intent) {

		// Extract string from intent
		String query = intent.getStringExtra(SearchManager.QUERY);
		
        if (query == null || query.length() <= 0 || !Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	// No (valid) query provided
        	empty.setText(R.string.search_no_query);
        	// Provide search input
        	onSearchRequested();
        	return;
        }

    	// Remember this search query to later show as a suggestion
    	suggestions.saveRecentQuery(query, null);

    	// Start search task
        new SearchTask(Search.this).execute(query, getUserId());
        
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	SearchResult item = (SearchResult) getListView().getItemAtPosition(position);
    	Log.d(LOGTAG, "ITEM NAME: " + item.getBeerName());
    	if(item.isRated()){
        	Intent ratingIntent = new Intent(Search.this, Rating.class);  
        	ratingIntent.putExtra("BEERNAME", item.getBeerName());
        	ratingIntent.putExtra("BEERID", item.getBeerId());
        	startActivity(ratingIntent);  
    	} else {
        	Intent rateIntent = new Intent(Search.this, Rate.class);  
        	rateIntent.putExtra("BEERNAME", item.getBeerName());
        	rateIntent.putExtra("BEERID", item.getBeerId());
        	startActivity(rateIntent);  
    	}
    }

	public void onResultsRetrieved(List<SearchResult> results) {
    	if (results != null) {
    		setListAdapter(new SearchAdapter(this, results));
			if (results.size() <= 0) {
	    		// If no beers were found, show this in a text message
				((TextView)findViewById(android.R.id.empty)).setText(R.string.toast_search_empty);
	    	}
    	}
	}

}

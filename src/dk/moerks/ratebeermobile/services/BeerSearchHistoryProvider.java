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
package dk.moerks.ratebeermobile.services;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

/**
 * Provides a wrapper for the SearchRecentSuggestionsProvider to show the latest 
 * beer searches the user performed.
 *
 */
public class BeerSearchHistoryProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "dk.moers.ratebeermobile.BeerSearchHistoryProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public BeerSearchHistoryProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }
    
    public static void clearHistory(Context context) {
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(context, 
				BeerSearchHistoryProvider.AUTHORITY, BeerSearchHistoryProvider.MODE);
        suggestions.clearHistory();
    }
}

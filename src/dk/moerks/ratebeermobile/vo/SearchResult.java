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
package dk.moerks.ratebeermobile.vo;

public class SearchResult {
	private String beerName;
	//private String beerUrl;
	private String beerId;
	private String beerPercentile;
	private String beerRatings;
	private boolean rated;
	private boolean alias;
	private boolean retired;
	
	public String getBeerName() {
		return beerName;
	}
	public void setBeerName(String beerName) {
		this.beerName = beerName;
	}
	/*
	public String getBeerUrl() {
		return beerUrl;
	}
	public void setBeerUrl(String beerUrl) {
		this.beerUrl = beerUrl;
	}
	*/
	public String getBeerId() {
		return beerId;
	}
	public void setBeerId(String beerId) {
		this.beerId = beerId;
	}
	public void setBeerPercentile(String beerPercentile) {
		this.beerPercentile = beerPercentile;
	}
	public String getBeerPercentile() {
		if(beerPercentile.contains(".")){
			return beerPercentile.substring(0, beerPercentile.indexOf(".")+2);
		}
		return beerPercentile;
	}
	public void setBeerRatings(String beerRatings) {
		this.beerRatings = beerRatings;
	}
	public String getBeerRatings() {
		return beerRatings;
	}
	public boolean isRated() {
		return rated;
	}
	public void setRated(boolean rated) {
		this.rated = rated;
	}
	public void setAlias(boolean alias) {
		this.alias = alias;
	}
	public boolean isAlias() {
		return alias;
	}
	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	public boolean isRetired() {
		return retired;
	}
}

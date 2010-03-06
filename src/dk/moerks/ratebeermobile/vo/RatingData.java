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

public class RatingData {
	private String aroma;
	private String appearance;
	private String flavor;
	private String palate;
	private String overall;
	@SuppressWarnings("unused")
	private String totalscore;
	private String comment;
	
	public String getAroma() {
		return aroma;
	}
	public void setAroma(String aroma) {
		this.aroma = aroma;
	}
	public String getAppearance() {
		return appearance;
	}
	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}
	public String getFlavor() {
		return flavor;
	}
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	public String getPalate() {
		return palate;
	}
	public void setPalate(String palate) {
		this.palate = palate;
	}
	public String getOverall() {
		return overall;
	}
	public void setOverall(String overall) {
		this.overall = overall;
	}
	public String getTotalscore() {
		int aroma = Integer.parseInt(this.aroma);
		int appearance = Integer.parseInt(this.appearance);
		int flavor = Integer.parseInt(this.flavor);
		int palate = Integer.parseInt(this.palate);
		int overall = Integer.parseInt(this.overall);
		
		int total = (aroma + appearance + flavor + palate + overall);

		float totalscore =  ((float)total) / 10;
		String result = "" + totalscore;
		return result;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}

package dk.moerks.ratebeermobile.vo;

public class SearchResult {
	private String beerName;
	private String beerUrl;
	private String beerId;
	private String beerPercentile;
	private String beerRatings;
	private boolean rated;
	
	public String getBeerName() {
		return beerName;
	}
	public void setBeerName(String beerName) {
		this.beerName = beerName;
	}
	public String getBeerUrl() {
		return beerUrl;
	}
	public void setBeerUrl(String beerUrl) {
		this.beerUrl = beerUrl;
	}
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
}

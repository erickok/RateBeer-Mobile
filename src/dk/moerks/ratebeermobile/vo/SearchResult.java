package dk.moerks.ratebeermobile.vo;

public class SearchResult {
	private String beerName;
	private String beerUrl;
	private String beerId;
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
	public boolean isRated() {
		return rated;
	}
	public void setRated(boolean rated) {
		this.rated = rated;
	}
}

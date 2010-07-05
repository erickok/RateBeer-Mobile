package dk.moerks.ratebeermobile.vo;

public class BeerInfo {
	private String beerId;
	private String beerName;
	private String brewerId;
	private String brewerName;
	private long overallPctl;
	private String beerStyleName;
	private String abv;
	private String description;
	
	//Accessors
	public String getBeerId() {
		return beerId;
	}
	public void setBeerId(String beerId) {
		this.beerId = beerId;
	}
	public String getBeerName() {
		return beerName;
	}
	public void setBeerName(String beerName) {
		this.beerName = beerName;
	}
	public String getBrewerId() {
		return brewerId;
	}
	public void setBrewerId(String brewerId) {
		this.brewerId = brewerId;
	}
	public String getBrewerName() {
		return brewerName;
	}
	public void setBrewerName(String brewerName) {
		this.brewerName = brewerName;
	}
	public long getOverallPctl() {
		return overallPctl;
	}
	public void setOverallPctl(long overallPctl) {
		this.overallPctl = overallPctl;
	}
	public String getBeerStyleName() {
		return beerStyleName;
	}
	public void setBeerStyleName(String beerStyleName) {
		this.beerStyleName = beerStyleName;
	}
	public String getAbv() {
		return abv;
	}
	public void setAbv(String abv) {
		this.abv = abv;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

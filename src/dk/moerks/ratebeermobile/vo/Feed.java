package dk.moerks.ratebeermobile.vo;

public class Feed {
	public static final String ADD_BEER_TYPE = "ADD_BEER_TYPE";
	
	private String type;
	private String value;
	private String date;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}

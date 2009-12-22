package dk.moerks.ratebeermobile.vo;

public class Feed {
	public static final String ADD_BEER_TYPE = "ADD_BEER_TYPE";
	public static final String RATED_BEER_TYPE = "RATED_BEER_TYPE";
	public static final String MILESTONE_REACHED_TYPE = "MILESTONE_REACHED_TYPE";
	public static final String REVIEWED_PLACE_TYPE = "REVIEWED_PLACE_TYPE";
	public static final String UPDATED_BIO_TYPE = "UPDATED_BIO_TYPE";
	public static final String ATTENDING_TYPE = "ATTENDING_TYPE";
	
	private String type;
	private String friend;
	private String beer;
	private String score;
	private String ratings;
	private String activityTime;
	private String place;
	private String event;
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	private String date;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getBeer() {
		return beer;
	}
	public void setBeer(String beer) {
		this.beer = beer;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}
	public String getRatings() {
		return ratings;
	}
	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}
	public String getActivityTime() {
		return activityTime;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getPlace() {
		return place;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEvent() {
		return event;
	}
}

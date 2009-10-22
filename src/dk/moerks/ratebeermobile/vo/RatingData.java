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

package dk.moerks.ratebeermobile.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import dk.moerks.ratebeermobile.vo.Feed;
import dk.moerks.ratebeermobile.vo.Message;
import dk.moerks.ratebeermobile.vo.MessageHeader;
import dk.moerks.ratebeermobile.vo.RatingData;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class RBParser {
	private static final String LOGTAG = "RBParser";
	
	public static List<SearchResult> parseSearch(String responseString){
		List<SearchResult> result = new ArrayList<SearchResult>();
		int contentBegin = responseString.indexOf("<!-- Content begins -->")+23;
		int contentEnd = responseString.indexOf("<!-- Content ends -->");
		String content = responseString.substring(contentBegin, contentEnd);
		
		String[] beers = content.split("<TD class=\"beer\" width=\"330\" align=left>");
		
		for (int i = 1; i < beers.length; i++) {
			SearchResult searchResult = new SearchResult();
			searchResult.setRated(beers[i].contains("checkbox2.gif"));
			Log.d(LOGTAG, "RATED: " + searchResult.isRated());
			
			int idBegin = beers[i].indexOf("<a href=\"/beer/distribution/")+28;
			int idEnd = beers[i].indexOf("/\">", idBegin);
			searchResult.setBeerId(beers[i].substring(idBegin, idEnd));
			Log.d(LOGTAG, "ID: " + searchResult.getBeerId());
			
			int urlBegin = beers[i].indexOf("<A HREF=\"/beer/")+9;
			int urlEnd = beers[i].indexOf("\">", urlBegin);
			searchResult.setBeerUrl(beers[i].substring(urlBegin, urlEnd));
			Log.d(LOGTAG, "URL: " + searchResult.getBeerUrl());
			
			int nameBegin = urlEnd+2;
			int nameEnd = beers[i].indexOf("</A>&nbsp;", nameBegin);
			searchResult.setBeerName(beers[i].substring(nameBegin, nameEnd));
			Log.d(LOGTAG, "NAME: " + searchResult.getBeerName());
			
			result.add(searchResult);
		}
		
		return result;
	}

	public static String parseDrink(String responseString) {
		int drinkBegin = responseString.indexOf("is drinking ")+12;
		int drinkEnd = responseString.indexOf(" <span style", drinkBegin);
		String drink = responseString.substring(drinkBegin, drinkEnd);
		
		if(drink != null && drink.length() > 0){
			return drink;
		} else {
			return null;
		}
	}
	
	public static RatingData parseRating(String responseString) {
		RatingData rating = new RatingData();
		int contentBegin = responseString.indexOf("<!-- Content begins -->")+23;
		int contentEnd = responseString.indexOf("<!-- Content ends -->");
		String content = responseString.substring(contentBegin, contentEnd);
		
		//Aroma
		int aromaBegin = content.indexOf("SELECTED")-3;
		int aromaEnd = aromaBegin + 3;
		String aromaTemp = content.substring(aromaBegin, aromaEnd);
		rating.setAroma(cleanValue(aromaTemp));

		//Appearance
		int appearanceBegin = content.indexOf("SELECTED", aromaEnd+1)-3;
		int appearanceEnd = appearanceBegin + 3;
		String appearanceTemp = content.substring(appearanceBegin, appearanceEnd);
		rating.setAppearance(cleanValue(appearanceTemp));

		//Flavor
		int flavorBegin = content.indexOf("SELECTED", appearanceEnd+1)-3;
		int flavorEnd = flavorBegin + 3;
		String flavorTemp = content.substring(flavorBegin, flavorEnd);
		rating.setFlavor(cleanValue(flavorTemp));

		//Palate
		int palateBegin = content.indexOf("SELECTED", flavorEnd+1)-3;
		int palateEnd = palateBegin + 3;
		String palateTemp = content.substring(palateBegin, palateEnd);
		rating.setPalate(cleanValue(palateTemp));
		
		//Overall
		int overallBegin = content.indexOf("SELECTED", palateEnd+1)-3;
		int overallEnd = overallBegin + 3;
		String overallTemp = content.substring(overallBegin, overallEnd);
		rating.setOverall(cleanValue(overallTemp));
		
		//Comment
		int commentBegin = content.indexOf("class=\"normBack\">") + 17;
		int commentEnd = content.indexOf("</TEXTAREA>", commentBegin);
		String comment = content.substring(commentBegin, commentEnd);
		rating.setComment(comment);
		
		return rating;
	}
	
	public static List<MessageHeader> parseBeermail(String responseString){
		List<MessageHeader> result = new ArrayList<MessageHeader>();
		
		int contentBegin = responseString.indexOf("<!-- Content begins -->")+23;
		int contentEnd = responseString.indexOf("<!-- Content ends -->");
		String content = responseString.substring(contentBegin, contentEnd);
	
		String[] mailLines = content.split("<FONT SIZE=1 color=");
		
		for (int i = 1; i < mailLines.length; i++) {
			MessageHeader header = new MessageHeader();
			
			//Status
			int statusBegin = mailLines[i].indexOf("\">")+2;
			int statusEnd = mailLines[i].indexOf("</FONT>");
			header.setStatus(stripItalic(mailLines[i].substring(statusBegin, statusEnd)));
			
			//MessageId
			int messageIdBegin = mailLines[i].indexOf("<a href=\"/showmessage/")+22;
			int messageIdEnd = mailLines[i].indexOf("/\">", messageIdBegin);
			header.setMessageId(mailLines[i].substring(messageIdBegin, messageIdEnd));
			
			int subjectBegin = messageIdEnd+3;
			int subjectEnd = mailLines[i].indexOf("</a></TD>", subjectBegin);
			header.setSubject(mailLines[i].substring(subjectBegin, subjectEnd));
			
			int senderIdBegin = mailLines[i].indexOf("<a href=\"/user/")+15;
			int senderIdEnd = mailLines[i].indexOf("/\">", senderIdBegin);
			header.setSenderId(mailLines[i].substring(senderIdBegin, senderIdEnd));
			
			int senderBegin = senderIdEnd+3;
			int senderEnd = mailLines[i].indexOf("</a></TD>", senderBegin);
			header.setSender(mailLines[i].substring(senderBegin, senderEnd));
			
			int dateBegin = mailLines[i].indexOf("5px;\">", senderEnd)+6;
			int dateEnd = mailLines[i].indexOf("</TD><TD", dateBegin);
			header.setDate(mailLines[i].substring(dateBegin, dateEnd));
			
			result.add(header);
		}
		
		return result;
	}

	public static Message parseMessage(String responseString){
		Message message = new Message();
		
		int contentBegin = responseString.indexOf("<!-- Content begins -->")+23;
		int contentEnd = responseString.indexOf("<!-- Content ends -->");
		String content = responseString.substring(contentBegin, contentEnd);

		int messageBegin = content.indexOf("</h1>")+5;
		int messageEnd = content.indexOf("<FORM NAME=Message>", messageBegin);
		message.setMessage(cleanHtml(content.substring(messageBegin, messageEnd)));
		Log.d(LOGTAG, message.getMessage());
		
		int timeBegin = content.indexOf("</a><br>")+8;
		int timeEnd = content.indexOf("</div>", timeBegin);
		message.setTime(cleanHtml(content.substring(timeBegin, timeEnd)));
		
		return message;
	}
	
	private static String cleanValue(String value){
		String result = null;
		if(value.charAt(0) == '='){
			result = value.substring(1);
		} else {
			result = value;
		}
		return result.trim();
	}
	
	private static String stripItalic(String value){
		String result = value.replaceAll("<i>", "");
		return result.replaceAll("</i>", "");
	}
	
	private static String cleanHtml(String value){
		String result = value.replaceAll("&nbsp;", " ");
		result = result.replaceAll("\n", "");
		result = result.replaceAll("<br>", "\n");
		result = result.replaceAll("<BR>", "\n");
		result = result.replaceAll("&#41;", ")");
		return result.trim();
	}

	public static String parseUserId(String responseString) {
		int userIdBegin = responseString.indexOf("<a href=\"/user/")+15;
		int userIdEnd = responseString.indexOf("/\">profile</a><br>");
		return responseString.substring(userIdBegin, userIdEnd);
	}

	public static boolean parseNewMail(String responseString) {
		return responseString.contains("You have unread messages");
	}

	public static List<Feed> parseFeed(String responseString) {
		ArrayList<Feed> result = new ArrayList<Feed>();
		int contentBegin = responseString.indexOf("<!-- Content begins -->")+23;
		int contentEnd = responseString.indexOf("<!-- Content ends -->");
		String content = responseString.substring(contentBegin, contentEnd);
		
		String[] feedChunks = content.split("<span class=\"userheader\">");
		
		for (int i = 1; i < feedChunks.length; i++) {
			int timeEnd = feedChunks[i].indexOf("</span></div>");
			String time = feedChunks[i].substring(0, timeEnd);
			
			String[] actions = feedChunks[i].split("<div class=\"friendsStatus\">");
			
			for (int j = 1; j < actions.length; j++) {
				Feed feed = new Feed();
				feed.setDate(time);
				
				System.out.println(actions[j]);
				int activityTimeStart = actions[j].indexOf("<span class=\"activityTime\">")+28;
				int activityTimeEnd = actions[j].indexOf("</span>", activityTimeStart);
				System.out.println("ACT START: " + activityTimeStart);
				System.out.println("ACT END: " + activityTimeEnd);
				System.out.println("Index: " + j);
				feed.setActivityTime(actions[j].substring(activityTimeStart, activityTimeEnd));

				//Rated Beer
				if(actions[j].contains("edit_grey.gif")){
					feed.setType(Feed.RATED_BEER_TYPE);
					
					//Friend
					int friendStart = actions[j].indexOf("ratings/\"><b>")+13;
					int friendEnd = actions[j].indexOf("</b>", friendStart);
					feed.setFriend(actions[j].substring(friendStart, friendEnd));
					
					//Beer
					int beerStart = actions[j].indexOf("\">", friendEnd)+2;
					int beerEnd = actions[j].indexOf("</a>", beerStart);
					feed.setBeer(actions[j].substring(beerStart, beerEnd));
					
					//Score
					int scoreStart = actions[j].indexOf("(<b>")+4;
					int scoreEnd = actions[j].indexOf("</b>)", scoreStart);
					feed.setScore(actions[j].substring(scoreStart, scoreEnd));
				}
				
				//Added Beer
				if(actions[j].contains("action_add.gif")){
					feed.setType(Feed.ADD_BEER_TYPE);

					int friendStart = actions[j].indexOf("\"><b>")+5;
					int friendEnd = actions[j].indexOf("</b></a>", friendStart);
					feed.setFriend(actions[j].substring(friendStart, friendEnd));
					
					int beerStart = actions[j].indexOf("\"><b>", friendEnd)+5;
					int beerEnd = actions[j].indexOf("</b></a>", beerStart);
					feed.setBeer(actions[j].substring(beerStart, beerEnd));
				}
	
				//Milestone Reached
				if(actions[j].contains("14.png")){
					feed.setType(Feed.MILESTONE_REACHED_TYPE);
					
					int friendStart = actions[j].indexOf("\"><b>")+5;
					int friendEnd = actions[j].indexOf("</b></a>", friendStart);
					feed.setFriend(actions[j].substring(friendStart, friendEnd));

					int ratingsStart = actions[j].indexOf("<b>", friendEnd)+3;
					int ratingsEnd = actions[j].indexOf("</b>", ratingsStart);
					feed.setRatings(actions[j].substring(ratingsStart, ratingsEnd));
				}
				
				result.add(feed);
			}
		}
		
		return result;
	}
}

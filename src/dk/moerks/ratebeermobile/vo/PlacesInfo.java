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

public class PlacesInfo {
	private String PlaceID;
	private String PlaceName;
	private String PlaceType;
	private String Address;
	private String City;
	private String StateID;
	private String CountryID;
	private String PostalCode;
	private String PhoneNumber;
	private String AvgRating;
	private String PhoneAC;
	private String Latitude;
	private String Longitude;
	private String Distance;
	public String getPlaceID() {
		return PlaceID;
	}
	public void setPlaceID(String placeID) {
		PlaceID = placeID;
	}
	public String getPlaceName() {
		return PlaceName;
	}
	public void setPlaceName(String placeName) {
		PlaceName = placeName;
	}
	public String getPlaceType() {
		return PlaceType;
	}
	public void setPlaceType(String placeType) {
		PlaceType = placeType;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getStateID() {
		return StateID;
	}
	public void setStateID(String stateID) {
		StateID = stateID;
	}
	public String getCountryID() {
		return CountryID;
	}
	public void setCountryID(String countryID) {
		CountryID = countryID;
	}
	public String getPostalCode() {
		return PostalCode;
	}
	public void setPostalCode(String postalCode) {
		PostalCode = postalCode;
	}
	public String getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getAvgRating() {
		return AvgRating;
	}
	public void setAvgRating(String avgRating) {
		AvgRating = avgRating;
	}
	public String getPhoneAC() {
		return PhoneAC;
	}
	public void setPhoneAC(String phoneAC) {
		PhoneAC = phoneAC;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getDistance() {
		return Distance;
	}
	public void setDistance(String distance) {
		Distance = distance;
	}
}

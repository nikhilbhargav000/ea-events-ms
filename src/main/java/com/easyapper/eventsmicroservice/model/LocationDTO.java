package com.easyapper.eventsmicroservice.model;

import javax.validation.constraints.NotNull;

public class LocationDTO {
	private String longitude;
	private String latitude;
	private AddressDTO address;
	//Constructor
	public LocationDTO(String longitude, String latitude, AddressDTO address) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}
	//Getter and Setters
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public AddressDTO getAddress() {
		return address;
	}
	public void setAddress(AddressDTO address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "LocationDTO [longitude=" + longitude + ", latitude=" + latitude + ", address=" + address + "]";
	}
	
}

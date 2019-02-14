package com.easyapper.eventsmicroservice.entity;

public class LocationSubEntity {
	
	private String longitude;
	private String latitude;
	private AddressSubEntity address;
	//Constructor
	public LocationSubEntity(String longitude, String latitude, AddressSubEntity address) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}
	//Getters and Setters
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
	public AddressSubEntity getAddress() {
		return address;
	}
	public void setAddress(AddressSubEntity address) {
		this.address = address;
	}
}

package com.easyapper.eventsmicroservice.entity;

public class AddressSubEntity {
	private String id;
	private String city;
	private String street;
	private String pin;
	//Getter & Setter
	public AddressSubEntity(String id, String city, String street, String pin) {
		super();
		this.id = id;
		this.city = city;
		this.street = street;
		this.pin = pin;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
}

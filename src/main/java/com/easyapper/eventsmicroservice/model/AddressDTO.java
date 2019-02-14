package com.easyapper.eventsmicroservice.model;

public class AddressDTO {
	
	private String id;
	private String city;
	private String street;
	private String pin;
	//Constructor
	public AddressDTO(String id, String city, String street, String pin) {
		super();
		this.id = id;
		this.city = city;
		this.street = street;
		this.pin = pin;
	}
	//Getter & Setter
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
	
	@Override
	public String toString() {
		return "AddressDTO [id=" + id + ", city=" + city + ", street=" + street + ", pin=" + pin + "]";
	}
}

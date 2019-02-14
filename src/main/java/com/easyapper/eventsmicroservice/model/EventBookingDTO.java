package com.easyapper.eventsmicroservice.model;

public class EventBookingDTO {
	private String url;
	private String inquiry_url;
	
	public EventBookingDTO(String url, String inquiry_url) {
		super();
		this.url = url;
		this.inquiry_url = inquiry_url;
	}
	//Getter & Setter
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getInquiry_url() {
		return inquiry_url;
	}
	public void setInquiry_url(String inquiry_url) {
		this.inquiry_url = inquiry_url;
	}
	@Override
	public String toString() {
		return "EventBookingDTO [url=" + url + ", inquiry_url=" + inquiry_url + "]";
	}
	
}

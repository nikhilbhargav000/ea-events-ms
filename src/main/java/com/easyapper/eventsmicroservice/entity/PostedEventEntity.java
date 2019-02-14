package com.easyapper.eventsmicroservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.easyapper.eventsmicroservice.model.AddressDTO;
import com.easyapper.eventsmicroservice.model.LocationDTO;

@Document
public class PostedEventEntity {
	@Id
	private String _id;
	private String user_id;
	private String event_type;
	private String event_category;
	private String event_subcategory;
	private LocationSubEntity event_location;
	private String organizer_email;
	private String event_name;
	private String event_description;
	private String event_image_url;
	@DateTimeFormat
	private Date event_start_date;
	@DateTimeFormat
	private Date event_last_date;
	private String event_min_age;
	private String event_max_age;
	private String event_price;
	private EventBookingSubEntity event_booking;
	//Constructor 
	public String get_id() {
		return _id;
	}
	public PostedEventEntity(String _id, String user_id, String event_type, String event_category, String event_subcategory,
			LocationSubEntity event_location, String organizer_email, String event_name, String event_description,
			String event_image_url, Date event_start_date, Date event_last_date, String event_min_age,
			String event_max_age, String event_price, EventBookingSubEntity event_booking) {
		super();
		this._id = _id;
		this.user_id = user_id;
		this.event_type = event_type;
		this.event_category = event_category;
		this.event_subcategory = event_subcategory;
		this.event_location = event_location;
		this.organizer_email = organizer_email;
		this.event_name = event_name;
		this.event_description = event_description;
		this.event_image_url = event_image_url;
		this.event_start_date = event_start_date;
		this.event_last_date = event_last_date;
		this.event_min_age = event_min_age;
		this.event_max_age = event_max_age;
		this.event_price = event_price;
		this.event_booking = event_booking;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getEvent_category() {
		return event_category;
	}
	public void setEvent_category(String event_category) {
		this.event_category = event_category;
	}
	public String getEvent_subcategory() {
		return event_subcategory;
	}
	public void setEvent_subcategory(String event_subcategory) {
		this.event_subcategory = event_subcategory;
	}
	public LocationSubEntity getEvent_location() {
		return event_location;
	}
	public void setEvent_location(LocationSubEntity event_location) {
		this.event_location = event_location;
	}
	public String getOrganizer_email() {
		return organizer_email;
	}
	public void setOrganizer_email(String organizer_email) {
		this.organizer_email = organizer_email;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getEvent_description() {
		return event_description;
	}
	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}
	public String getEvent_image_url() {
		return event_image_url;
	}
	public void setEvent_image_url(String event_image_url) {
		this.event_image_url = event_image_url;
	}
	public Date getEvent_start_date() {
		return event_start_date;
	}
	public void setEvent_start_date(Date event_start_date) {
		this.event_start_date = event_start_date;
	}
	public Date getEvent_last_date() {
		return event_last_date;
	}
	public void setEvent_last_date(Date event_last_date) {
		this.event_last_date = event_last_date;
	}
	public String getEvent_min_age() {
		return event_min_age;
	}
	public void setEvent_min_age(String event_min_age) {
		this.event_min_age = event_min_age;
	}
	public String getEvent_max_age() {
		return event_max_age;
	}
	public void setEvent_max_age(String event_max_age) {
		this.event_max_age = event_max_age;
	}
	public String getEvent_price() {
		return event_price;
	}
	public void setEvent_price(String event_price) {
		this.event_price = event_price;
	}
	public EventBookingSubEntity getEvent_booking() {
		return event_booking;
	}
	public void setEvent_booking(EventBookingSubEntity event_booking) {
		this.event_booking = event_booking;
	}
	@Override
	public String toString() {
		return "EventEntity [_id=" + _id + ", user_id=" + user_id + ", event_type=" + event_type + ", event_category="
				+ event_category + ", event_subcategory=" + event_subcategory + ", event_location=" + event_location
				+ ", organizer_email=" + organizer_email + ", event_name=" + event_name + ", event_description="
				+ event_description + ", event_image_url=" + event_image_url + ", event_start_date=" + event_start_date
				+ ", event_last_date=" + event_last_date + ", event_min_age=" + event_min_age + ", event_max_age="
				+ event_max_age + ", event_price=" + event_price + ", event_booking=" + event_booking + "]";
	}
}

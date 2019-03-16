package com.easyapper.eventsmicroservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.easyapper.eventsmicroservice.model.originalevent.OrglEventDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter @Setter
@ToString
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
	private OrglEventDto original_event;
	private String event_approved;
	//Constructor 
	public PostedEventEntity(String _id, String user_id, String event_type, String event_category, String event_subcategory,
			LocationSubEntity event_location, String organizer_email, String event_name, String event_description,
			String event_image_url, Date event_start_date, Date event_last_date, String event_min_age,
			String event_max_age, String event_price, EventBookingSubEntity event_booking, 
			OrglEventDto original_event, String event_approved) {
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
		this.original_event = original_event;
		this.event_approved = event_approved;
	}
	
}

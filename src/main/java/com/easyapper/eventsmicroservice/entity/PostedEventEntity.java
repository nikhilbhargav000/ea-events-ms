package com.easyapper.eventsmicroservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.easyapper.eventsmicroservice.model.originalevent.OrglEventDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@AllArgsConstructor @NoArgsConstructor
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
//	@Indexed(name="event_last_date_index", expireAfterSeconds=10)
	private Date event_last_date;
	private String event_min_age;
	private String event_max_age;
	private String event_price;
	private EventBookingSubEntity event_booking;
	private OrglEventDto original_event;
	private String event_approved;
	@DateTimeFormat
	private Date event_start_time;
	@DateTimeFormat
	private Date event_end_time;
	
}

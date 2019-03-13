package com.easyapper.eventsmicroservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Document(collection="SubscribedEventCollection")
public class SubscribedEventEntity {
	
	@Id
	private String _id;
	private String user_id;
	private String post_event_id;
	private String event_type;
	@DateTimeFormat
	private Date event_start_date;
	@DateTimeFormat
	private Date event_last_date;
	@DateTimeFormat
	private Date event_start_time;
	@DateTimeFormat
	private Date event_end_time;
	
	//Constructor
	public SubscribedEventEntity(String _id, String user_id, String post_event_id, String event_type,
			Date event_start_date, Date event_last_date, Date event_start_time, Date event_end_time) {
		super();
		this._id = _id;
		this.user_id = user_id;
		this.post_event_id = post_event_id;
		this.event_type = event_type;
		this.event_start_date = event_start_date;
		this.event_last_date = event_last_date;
		this.event_start_time = event_start_time;
		this.event_end_time = event_end_time;
	}
	
}

package com.easyapper.eventsmicroservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

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
	public String get_id() {
		return _id;
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
	public String getPost_event_id() {
		return post_event_id;
	}
	public void setPost_event_id(String post_event_id) {
		this.post_event_id = post_event_id;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
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
	public Date getEvent_start_time() {
		return event_start_time;
	}
	public void setEvent_start_time(Date event_start_time) {
		this.event_start_time = event_start_time;
	}
	public Date getEvent_end_time() {
		return event_end_time;
	}
	public void setEvent_end_time(Date event_end_time) {
		this.event_end_time = event_end_time;
	}
	//To String
	@Override
	public String toString() {
		return "SubscribedEventEntity [_id=" + _id + ", user_id=" + user_id + ", post_event_id=" + post_event_id
				+ ", event_type=" + event_type + ", event_start_date=" + event_start_date + ", event_last_date="
				+ event_last_date + ", event_start_time=" + event_start_time + ", event_end_time=" + event_end_time
				+ "]";
	}
	
}

package com.easyapper.eventsmicroservice.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

public class SubscribedEventDto {
	
	@Null(message="_id should be null")
	private String _id;
	private String subscriber_user_id;	
	@Null(message="postedEventId should be null")
	private String posted_event_id;
	private String start_date_time;
	private String end_date_time;
	@NotBlank(message="event_type should not be blank or null")
	private String event_type;
	
	//Constructor
	public SubscribedEventDto(@Null(message = "_id should be null") String _id, String subscriber_user_id,
			@Null(message = "postedEventId should be null") String posted_event_id, String start_date_time,
			String end_date_time, @NotBlank(message = "event_type should not be blank or null") String event_type) {
		super();
		this._id = _id;
		this.subscriber_user_id = subscriber_user_id;
		this.posted_event_id = posted_event_id;
		this.start_date_time = start_date_time;
		this.end_date_time = end_date_time;
		this.event_type = event_type;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSubscriber_user_id() {
		return subscriber_user_id;
	}

	public void setSubscriber_user_id(String subscriber_user_id) {
		this.subscriber_user_id = subscriber_user_id;
	}

	public String getPosted_event_id() {
		return posted_event_id;
	}

	public void setPosted_event_id(String posted_event_id) {
		this.posted_event_id = posted_event_id;
	}

	public String getStart_date_time() {
		return start_date_time;
	}

	public void setStart_date_time(String start_date_time) {
		this.start_date_time = start_date_time;
	}

	public String getEnd_date_time() {
		return end_date_time;
	}

	public void setEnd_date_time(String end_date_time) {
		this.end_date_time = end_date_time;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	@Override
	public String toString() {
		return "SubscribedEventDTO [_id=" + _id + ", subscriber_user_id=" + subscriber_user_id + ", posted_event_id="
				+ posted_event_id + ", start_date_time=" + start_date_time + ", end_date_time=" + end_date_time
				+ ", event_type=" + event_type + "]";
	}
	
}

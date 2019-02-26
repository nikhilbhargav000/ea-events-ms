package com.easyapper.eventsmicroservice.model;

import java.util.List;

public class UserEventListsContainerDto {
	
	List<EventDto> subscribed ;
	List<EventDto> posted ;
	
	//Constructor
	public UserEventListsContainerDto(List<EventDto> subscribed, List<EventDto> posted) {
		super();
		this.subscribed = subscribed;
		this.posted = posted;
	}
	//Getter & Setter
	public List<EventDto> getSubscribed() {
		return subscribed;
	}
	public void setSubscribed(List<EventDto> subscribed) {
		this.subscribed = subscribed;
	}
	public List<EventDto> getPosted() {
		return posted;
	}
	public void setPosted(List<EventDto> posted) {
		this.posted = posted;
	}
	
}

package com.easyapper.eventsmicroservice.model;

import java.util.List;

public class UserEventListsContainerDTO {
	
	List<EventDTO> subscribed ;
	List<EventDTO> posted ;
	
	//Constructor
	public UserEventListsContainerDTO(List<EventDTO> subscribed, List<EventDTO> posted) {
		super();
		this.subscribed = subscribed;
		this.posted = posted;
	}
	//Getter & Setter
	public List<EventDTO> getSubscribed() {
		return subscribed;
	}
	public void setSubscribed(List<EventDTO> subscribed) {
		this.subscribed = subscribed;
	}
	public List<EventDTO> getPosted() {
		return posted;
	}
	public void setPosted(List<EventDTO> posted) {
		this.posted = posted;
	}
	
}

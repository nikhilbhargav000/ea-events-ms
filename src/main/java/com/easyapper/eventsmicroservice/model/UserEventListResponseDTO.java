package com.easyapper.eventsmicroservice.model;

public class UserEventListResponseDTO {
	UserEventListsContainerDTO events;
	//Constructor
	public UserEventListResponseDTO(UserEventListsContainerDTO events) {
		super();
		this.events = events;
	}
	//Getter & Setter
	public UserEventListsContainerDTO getEvents() {
		return events;
	}
	public void setEvents(UserEventListsContainerDTO events) {
		this.events = events;
	}
	@Override
	public String toString() {
		return "UserEventListResponseDTO [events=" + events + "]";
	}
}

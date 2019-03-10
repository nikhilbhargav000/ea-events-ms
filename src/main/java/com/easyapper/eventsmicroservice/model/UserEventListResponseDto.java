package com.easyapper.eventsmicroservice.model;

public class UserEventListResponseDto {
	UserEventListsContainerDto events;
	//Constructor
	public UserEventListResponseDto(UserEventListsContainerDto events) {
		super();
		this.events = events;
	}
	//Getter & Setter
	public UserEventListsContainerDto getEvents() {
		return events;
	}
	public void setEvents(UserEventListsContainerDto events) {
		this.events = events;
	}
	@Override
	public String toString() {
		return "UserEventListResponseDTO [events=" + events + "]";
	}
}

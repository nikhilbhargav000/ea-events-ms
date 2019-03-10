package com.easyapper.eventsmicroservice.entity;

import com.easyapper.eventsmicroservice.model.EventDto;

import java.util.List;

public class EventListWrapper {
    private List<EventDto> events;

    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }
}

package com.easyapper.eventsmicroservice.dao;

import java.util.List;

import com.easyapper.eventsmicroservice.model.EventDto;

public class EventListWrapper {
    private List<EventDto> events;

    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }
}

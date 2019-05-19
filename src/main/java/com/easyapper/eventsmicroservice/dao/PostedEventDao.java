package com.easyapper.eventsmicroservice.dao;

import java.util.List;
import java.util.Map;

import com.easyapper.eventsmicroservice.entity.EventProviderEntity;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;

public interface PostedEventDao {

	public String insertEvent(PostedEventEntity eventEntity) 
			throws EasyApperDbException ;
	
	public PostedEventEntity getEvent(String userId, String eventId) 
			throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException ;
	
	public List<PostedEventEntity> getAllEvent(String userId, Map<String, String> paramMap, int page, int size, long skip)
			throws UserIdNotExistException, InvalidTimeFormatException, InvalidDateFormatException, EasyApperDbException ;
	
	public List<EventProviderEntity> getEventProviders(String userId, int page, int size, long skip) throws EasyApperDbException ;
	
	public void updateEvent(String userId, String eventId, PostedEventEntity updateEventEntity) 
			throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException ;
	
	public void deleteEvent(String userId, String eventId) 
			throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException ;
	
	public List<String> getAllEventCollectionName() 
			throws EasyApperDbException ;
	
	public long getEventsCount(String userId) 
			throws EasyApperDbException;
	
	public long getEventProvidersCount(String userId)
			throws EasyApperDbException ;
}

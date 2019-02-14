package com.easyapper.eventsmicroservice.dao;

import java.util.List;
import java.util.Map;

import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;

public interface PostedEventDao {

	public String insertEvent(PostedEventEntity eventEntity) throws EasyApperDbException ;
	public PostedEventEntity getEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException ;
	public List<PostedEventEntity> getAllEvent(String userId) throws UserIdNotExistException, EasyApperDbException ;
	public void updateEvent(String userId, String eventId, PostedEventEntity updateEventEntity) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException ;
	public void deleteEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException ;
	public List<String> getAllEventCollectionName() throws EasyApperDbException ;
}

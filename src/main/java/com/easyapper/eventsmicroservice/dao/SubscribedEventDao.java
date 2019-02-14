package com.easyapper.eventsmicroservice.dao;

import java.util.List;

import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.SubscribedEventNotFoundException;

public interface SubscribedEventDao {

	public String insertSubscribedEvent(SubscribedEventEntity subcEventEntity) throws EasyApperDbException ;
	public SubscribedEventEntity getEvent(String eventId, String userId) throws EasyApperDbException, SubscribedEventNotFoundException ;
	public void updateEvent(String eventId, SubscribedEventEntity updateSubcEventEntity) throws EventIdNotExistException, EasyApperDbException ;
	public void deleteEvent(String eventId, String userId) throws EasyApperDbException, SubscribedEventNotFoundException ;
	public List<SubscribedEventEntity> getAllEvent(String userId) throws EasyApperDbException ;
}

package com.easyapper.eventsmicroservice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.SubscribedEventNotFoundException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.utility.EALogger;

@Repository
public class SubscribedEventDaoImpl implements SubscribedEventDao {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	EALogger logger;
	
	public String insertSubscribedEvent(SubscribedEventEntity subcEventEntity) throws EasyApperDbException {
		try {
			mongoTemplate.insert(subcEventEntity);
		}catch(Exception e) {
			throw new EasyApperDbException();
		}
		return subcEventEntity.get_id();
	}
	
	public SubscribedEventEntity getEvent(String eventId, String userId) throws EasyApperDbException, SubscribedEventNotFoundException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(eventId));
		query.addCriteria(Criteria.where("user_id").is(userId));
		SubscribedEventEntity subcEventEntity = null;
		try {
			subcEventEntity = mongoTemplate.findOne(query, SubscribedEventEntity.class);
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
		if(subcEventEntity == null) {
			throw new SubscribedEventNotFoundException();
		}
		return subcEventEntity;
	}
	
	public void updateEvent(String eventId, SubscribedEventEntity updateSubcEventEntity) throws EventIdNotExistException, EasyApperDbException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(eventId));
		try {
			SubscribedEventEntity subcEventEntity = (SubscribedEventEntity) mongoTemplate.findOne(query, 
				SubscribedEventEntity.class);
			if(subcEventEntity == null) {
				throw new EventIdNotExistException();
			}
			updateSubcEventEntity.set_id(subcEventEntity.get_id());
			mongoTemplate.save(updateSubcEventEntity);
		}catch(Exception e) {
			if(e instanceof EventIdNotExistException) {
				throw new EventIdNotExistException();
			}
			throw new EasyApperDbException();
		}
	}
	
	public void deleteEvent(String eventId, String userId) throws EasyApperDbException, SubscribedEventNotFoundException {
		SubscribedEventEntity subcEventEntity = this.getEvent(eventId, userId);
		if(subcEventEntity != null) {
			mongoTemplate.remove(subcEventEntity);
		}
	}
	
	public List<SubscribedEventEntity> getAllEvent(String userId) throws EasyApperDbException{
		Query query = new Query();
		query.addCriteria(Criteria.where("user_id").is(userId));
		List<SubscribedEventEntity> eventList = null;
		try {
			eventList = mongoTemplate.find(query, SubscribedEventEntity.class);
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
		if(eventList == null || eventList.size() == 0) {
			return new ArrayList<>();
		}
		return eventList;
	}
	
}

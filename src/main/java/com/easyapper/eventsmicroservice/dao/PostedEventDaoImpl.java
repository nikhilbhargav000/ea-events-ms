package com.easyapper.eventsmicroservice.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.parser.Entity;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EALogger;
import com.easyapper.eventsmicroservice.utility.EAUtil;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;

@Repository
public class PostedEventDaoImpl implements PostedEventDao{
	
	@Autowired
	EALogger logger;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public String insertEvent(PostedEventEntity eventEntity) throws EasyApperDbException {
		String userId = eventEntity.getUser_id();
		String collectionName = EAUtil.getEventCollectionName(userId);
		try {
			if(mongoTemplate.getCollectionNames().contains(collectionName)) {
				mongoTemplate.insert(eventEntity, collectionName);
			}else {
				MongoCollection<Document> eventCollection = mongoTemplate.createCollection(
						collectionName);
				mongoTemplate.insert(eventEntity, collectionName);
			}
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
		return eventEntity.get_id();
	}
	
	@Override
	public PostedEventEntity getEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException {
		PostedEventEntity eventEntity = null;
		try {
			String collectionName = EAUtil.getEventCollectionName(userId);
			if(mongoTemplate.getCollectionNames().contains(collectionName)) {
				Query query = new Query();
				query.addCriteria(Criteria.where("user_id").is(userId));
				query.addCriteria(Criteria.where("_id").is(eventId));
				eventEntity = mongoTemplate.findOne(query, PostedEventEntity.class, collectionName);
			}else {
				throw new UserIdNotExistException();
			}
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			if(e instanceof UserIdNotExistException) {
				throw new UserIdNotExistException();
			}
			throw new EasyApperDbException();
		}
		if(eventEntity == null) {
			throw new EventIdNotExistException();
		}
		return eventEntity;
	}
	
	@Override
	public List<PostedEventEntity> getAllEvent(String userId, Map<String, String> paramMap, int page, int size, long skip)
			throws UserIdNotExistException, EasyApperDbException {
		List<PostedEventEntity> allEventList = new ArrayList<>();
		try {
			String collectionName = EAUtil.getEventCollectionName(userId);
			//Pagination
			Query query = new Query();
			EAUtil.setPaginationInQuery(query, page, size, skip);
			//Search
			
			if(mongoTemplate.getCollectionNames().contains(collectionName)) {
				List<PostedEventEntity> eventList = mongoTemplate.find(query, PostedEventEntity.class, collectionName);
//				List<PostedEventEntity> eventList = mongoTemplate.findAll(PostedEventEntity.class, collectionName);
				allEventList.addAll(eventList);
			}else {
				return new ArrayList<>();
			}
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
		return allEventList;
	}
	
	@Override
	public long getEventsCount(String userId) 
			throws EasyApperDbException{
		List<PostedEventEntity> allEventList = new ArrayList<>();
		long count = 0;
		try {
			String collectionName = EAUtil.getEventCollectionName(userId);
			if(mongoTemplate.getCollectionNames().contains(collectionName)) {
				Query query = new Query();
				mongoTemplate.count(query, collectionName);
			}
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
		return count;
	}
	
	@Override
	public void updateEvent(String userId, String eventId, 
			PostedEventEntity updateEventEntity) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException {
		String collectionName = EAUtil.getEventCollectionName(userId);
		try {
			if(mongoTemplate.getCollectionNames().contains(collectionName)) {
				Query query = new Query();
				query.addCriteria(Criteria.where("user_id").is(userId));
				query.addCriteria(Criteria.where("_id").is(eventId));
				PostedEventEntity eventEntity = mongoTemplate.findOne(query, PostedEventEntity.class, collectionName);
				if(eventEntity == null) {
					throw new EventIdNotExistException();
				}
				updateEventEntity.set_id(eventId);
				updateEventEntity.setUser_id(userId);
				mongoTemplate.save(updateEventEntity, collectionName);
			}else {
				throw new UserIdNotExistException();
			}
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			if(e instanceof UserIdNotExistException) {
				throw new UserIdNotExistException();
			}
			if(e instanceof EventIdNotExistException) {
				throw new EventIdNotExistException();
			}
			throw new EasyApperDbException();
		}
	}
	
	@Override
	public void deleteEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException {
		PostedEventEntity eventEntity = this.getEvent(userId, eventId);
		if(eventEntity != null) {
			String collectionName = EAUtil.getEventCollectionName(userId);
			mongoTemplate.remove(eventEntity, collectionName);
		}
	}
	
	@Override
	public List<String> getAllEventCollectionName() throws EasyApperDbException{
		List<String> eventCollectionList = new ArrayList<>();
		try {
			Set<String> collectionNameSet = mongoTemplate.getCollectionNames();
			for(String collectionName : collectionNameSet) {
				if(collectionName.startsWith(EAConstants.EVENT_COLLECTION_PREFIX) &&
						collectionName.endsWith(EAConstants.EVENT_COLLECTION_POSTFIX)) {
					eventCollectionList.add(collectionName);
				}
			}
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
		return eventCollectionList;
	}
	
}

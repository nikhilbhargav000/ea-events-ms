package com.easyapper.eventsmicroservice.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.SubscribedEventNotFoundException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.helper.DaoHepler;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EALogger;
import com.easyapper.eventsmicroservice.utility.EAUtil;

@Repository
public class SubscribedEventDaoImpl implements SubscribedEventDao {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	DaoHepler daoHelper;
	
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
	
	@Override
	public List<SubscribedEventEntity> getAllEvent(String userId, Map<String, String> paramMap, int page, int size, long skip) 
			throws EasyApperDbException, InvalidTimeFormatException, InvalidDateFormatException{
		Query query = new Query();
		query.addCriteria(Criteria.where("user_id").is(userId));
		//Pagination
		EAUtil.setPaginationInQuery(query, page, size, skip);
		//Search
		this.addSearchParams(query, paramMap);
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
	
	private void addSearchParams(Query query, final Map<String, String> paramMap) throws InvalidTimeFormatException, InvalidDateFormatException {
		daoHelper.addSearchCriteriaForString(EAConstants.EVENT_POSTED_EVENT_ID_KEY, "post_event_id", query, paramMap);
		daoHelper.addSearchCriteriaForString(EAConstants.EVENT_TYPE_KEY, "event_type", query, paramMap);
		daoHelper.addSearchCriteriaForDate(EAConstants.EVENT_START_DATE_FROM_KEY, EAConstants.EVENT_START_DATE_TO_KEY, "event_start_date", query, paramMap);
		daoHelper.addSearchCriteriaForDate(EAConstants.EVENT_LAST_DATE_FROM_KEY, EAConstants.EVENT_LAST_DATE_TO_KEY, "event_last_date", query, paramMap);
		daoHelper.addSearchCriteriaForTime(EAConstants.EVENT_START_TIME_FROM_KEY, EAConstants.EVENT_START_TIME_TO_KEY, "event_start_time", query, paramMap);
		daoHelper.addSearchCriteriaForTime(EAConstants.EVENT_END_TIME_FROM_KEY, EAConstants.EVENT_END_TIME_TO_KEY, "event_end_time", query, paramMap);
	}
	
}

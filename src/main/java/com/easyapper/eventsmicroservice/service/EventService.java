package com.easyapper.eventsmicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapper.eventsmicroservice.dao.PostedEventDao;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.model.EventDto;
import com.easyapper.eventsmicroservice.translator.EventsTranslator;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EAUtil;

@Service
public class EventService {
	
	@Autowired
	EventsTranslator eventsTranslator;
	@Autowired
	PostedEventDao postedEventDao;
	
	public List<EventDto> getAllPostedEvents(Map<String, String> paramMap, int page, int size) 
			throws EasyApperDbException, InvalidDateFormatException, InvalidTimeFormatException {
		List<String> eventCollectionNameList = postedEventDao.getAllEventCollectionName();
		List<String> userIdList = this.getUserIdList(eventCollectionNameList);
		List<EventDto> allPostedEventList = new ArrayList();
		long toSkip = (page-1) * size;
		int toPickSize = size;
		for(String userId : userIdList) {
			List<EventDto> curPostedEventList = this.getAllUserEvent(userId, paramMap, 1, toPickSize, toSkip);
			if(curPostedEventList.size() == 0) {
				toSkip -= (this.getPostedEventsCount(userId));
			}else if(curPostedEventList.size() > 0) {
				toSkip -= (this.getPostedEventsCount(userId));
				toPickSize -= curPostedEventList.size();
			}
			allPostedEventList.addAll(curPostedEventList);
			if(toPickSize <= 0) {
				break;
			}
		}
		return allPostedEventList;
	}
	
	public EventDto getPostedEvent(String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException {
		String userId = EAUtil.getUserId(eventId);
		if(userId == null) {
			throw new UserIdNotExistException();
		}
		EventDto eventDto = eventsTranslator.getEventDto(postedEventDao.getEvent(userId, eventId));
		return eventDto;
	}

	private List<String> getUserIdList(List<String> eventCollectionNameList){
		List<String> userIdList = new ArrayList<>();
		for(String eventCollectionName : eventCollectionNameList) {
			userIdList.add( eventCollectionName.substring( 
					EAConstants.EVENT_COLLECTION_PREFIX.length(), 
					eventCollectionName.length() - EAConstants.EVENT_COLLECTION_POSTFIX.length()) );
		}
		return userIdList;
	}	
	
	private long getPostedEventsCount(String userId) throws EasyApperDbException {
		return postedEventDao.getEventsCount(userId);
	}
	
	public List<EventDto> getAllUserEvent(String userId, Map<String, String> paramMap, int page, int size, long skip) 
			throws UserIdNotExistException, EasyApperDbException, InvalidDateFormatException, InvalidTimeFormatException {
		List<PostedEventEntity> postedEntityList  = postedEventDao.getAllEvent(userId, paramMap, page, size, skip);
		List<EventDto> postedDtoList = eventsTranslator.getPostedEventDtoList(postedEntityList);
		return postedDtoList;
	}
	
}

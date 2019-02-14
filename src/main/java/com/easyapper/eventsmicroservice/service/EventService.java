package com.easyapper.eventsmicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapper.eventsmicroservice.dao.PostedEventDao;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.model.UserEventListsContainerDTO;
import com.easyapper.eventsmicroservice.model.EventDTO;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EAUtil;

@Service
public class EventService {
	
	@Autowired
	PostedEventDao eventDao;
	
	@Autowired
	UserService userService;
	
	public List<EventDTO> getAllPostedEvents(Map<String, String> paramMap) throws EasyApperDbException, InvalidDateFormatException, InvalidTimeFormatException {
		List<String> eventCollectionNameList = eventDao.getAllEventCollectionName();
		List<String> userIdList = this.getUserIdList(eventCollectionNameList);
		List<EventDTO> allPostedEventList = new ArrayList();
		for(String userId : userIdList) {
			UserEventListsContainerDTO userAllEventResponse = userService.getAllUserEvent(userId, paramMap);
			allPostedEventList.addAll(userAllEventResponse.getPosted());
		}
		return allPostedEventList;
	}
	
	public EventDTO getPostedEvent(String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException {
		String userId = EAUtil.getUserId(eventId);
		if(userId == null) {
			throw new UserIdNotExistException();
		}
		EventDTO eventDto = userService.getPostedEvent(userId, eventId);
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
}

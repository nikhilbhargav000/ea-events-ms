package com.easyapper.eventsmicroservice.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.easyapper.eventsmicroservice.api.ImageApi;
import com.easyapper.eventsmicroservice.dao.DBSeqValueFinder;
import com.easyapper.eventsmicroservice.dao.PostedEventDao;
import com.easyapper.eventsmicroservice.dao.PostedEventDaoImpl;
import com.easyapper.eventsmicroservice.dao.SubscribedEventDao;
import com.easyapper.eventsmicroservice.entity.AddressSubEntity;
import com.easyapper.eventsmicroservice.entity.EventBookingSubEntity;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.entity.LocationSubEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidPostedEventIdException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.NoExtensionFoundException;
import com.easyapper.eventsmicroservice.exception.PostedEventExistsException;
import com.easyapper.eventsmicroservice.exception.SubscribedEventNotFoundException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.model.AddressDto;
import com.easyapper.eventsmicroservice.model.UserEventListsContainerDto;
import com.easyapper.eventsmicroservice.translator.EventsTranslator;
import com.easyapper.eventsmicroservice.model.EventBookingDto;
import com.easyapper.eventsmicroservice.model.EventDto;
import com.easyapper.eventsmicroservice.model.LocationDto;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EAUtil;
import com.easyapper.eventsmicroservice.utility.EAValidator;

@Service
public class UserService {
	
	@Autowired
	EAValidator validator;
	@Autowired
	EventsTranslator eventsTranslator;
	@Autowired
	PostedEventDao postedEventDao ;
	@Autowired
	SubscribedEventDao subscribedEventDao;
	@Autowired
	DBSeqValueFinder dbSeqFinder;
	
	public String createPostedEvent(String userId, EventDto eventDto) throws EasyApperDbException, PostedEventExistsException, InvalidDateFormatException, InvalidTimeFormatException {
		if(validator.postedEventExists(userId, eventDto)) {
			throw new PostedEventExistsException();
		}
		PostedEventEntity eventEntity = eventsTranslator.getPostedEventEntity(eventDto);
		String dbName = EAUtil.getEventCollectionName(userId);
		String eventId = EAUtil.getPostedEventId(userId, dbSeqFinder.getNextSeqValue(dbName));
		eventEntity.set_id(eventId);
		eventEntity.setUser_id(userId);
		eventEntity.setEvent_type(eventEntity.getEvent_type().toLowerCase());
		return postedEventDao.insertEvent(eventEntity);
	}
	
	public String createSubscribededEvent(String userId, EventDto eventDto) throws EasyApperDbException, InvalidPostedEventIdException {
		if(!validator.isValidPostedEventId(eventDto.getPosted_event_id())) {
			throw new InvalidPostedEventIdException();
		}
		SubscribedEventEntity subcEventEntity = eventsTranslator.getSubcribedEventEntity(eventDto);
		String eventId = EAUtil.getSubscribedEventId(dbSeqFinder.getNextSeqValue(
				EAConstants.SUBSCRIBED_EVENT_COLLECTION_DB_NAME));
		subcEventEntity.set_id(eventId);
		subcEventEntity.setUser_id(userId);
		String id = subscribedEventDao.insertSubscribedEvent(subcEventEntity);
		return id;
	}

	public EventDto getPostedEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException {
		PostedEventEntity eventEntity = postedEventDao.getEvent(userId, eventId);
		EventDto eventDto = eventsTranslator.getEventDto(eventEntity);
		return eventDto;
	}
	
	public EventDto getSubscribedEvent(String userId, String eventId) throws EasyApperDbException, SubscribedEventNotFoundException {
		SubscribedEventEntity subcEventEntity = subscribedEventDao.getEvent(eventId, userId);
		return eventsTranslator.getEventDto(subcEventEntity);
	}

	public EventDto getEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException, SubscribedEventNotFoundException {
		EventDto eventDto = null;
		if(EAUtil.canBePostedEventId(eventId)) {
			eventDto = this.getPostedEvent(userId, eventId);
		}else if(EAUtil.canBeSubscribedEventId(eventId)) {
			eventDto = this.getSubscribedEvent(userId, eventId);
		}else {
			throw new EventIdNotExistException();
		}
		return eventDto;
	}
	
	public void updateEvent(String userId, String eventId, 
			EventDto eventDto) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException, InvalidPostedEventIdException {
		if(eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_POSTED)) {
			PostedEventEntity eventEntity = eventsTranslator.getPostedEventEntity(eventDto);
			eventEntity.set_id(null);
			eventEntity.setUser_id(userId);
			eventEntity.setEvent_type(eventEntity.getEvent_type().toLowerCase());
			postedEventDao.updateEvent(userId, eventId, eventEntity);
		}else if(eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_SUBSCRIBED)) {
			if(!validator.isValidPostedEventId(eventDto.getPosted_event_id())) {
				throw new InvalidPostedEventIdException();
			}
			SubscribedEventEntity subcEventEntity = eventsTranslator.getSubcribedEventEntity(eventDto);
			subcEventEntity.set_id(null);
			subcEventEntity.setUser_id(userId);
			subscribedEventDao.updateEvent(eventId, subcEventEntity);
		}
	}
	
	public void deleteEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException, SubscribedEventNotFoundException {
		if(EAUtil.canBePostedEventId(eventId)) {
			postedEventDao.deleteEvent(userId, eventId);
		}else if(EAUtil.canBeSubscribedEventId(eventId)) {
			subscribedEventDao.deleteEvent(eventId, userId);
		}else {
			throw new EventIdNotExistException();
		}
	}
	
	public String storeImage(HttpServletRequest request, String userId,
			MultipartFile imageFile) throws IOException, NoExtensionFoundException  {
		Path imagesDirPath = Paths.get(EAUtil.getImageRootDir());
		String strTimeStamp = EAUtil.getTimeStampFormatStr(new Timestamp(System.currentTimeMillis()) );
		String serverFileName = userId + EAConstants.UNDERSCORE + strTimeStamp
				+ EAUtil.getImageFileExtension(imageFile);
		if(!Files.exists(imagesDirPath)) {
			Files.createDirectories(imagesDirPath);
		}
		Path serverFilePath = Files.write(imagesDirPath.resolve(serverFileName), imageFile.getBytes());
		String imgUrl = EAUtil.getDomainUrl(request) + File.separator + 
				EAConstants.IMAGE_API_MAPPING + File.separator + serverFileName;
		return imgUrl;
	}
	
	/**
	 * Used in Validating similar posted events
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 * @throws UserIdNotExistException
	 * @throws EasyApperDbException
	 * @throws InvalidDateFormatException
	 * @throws InvalidTimeFormatException
	 */
	public UserEventListsContainerDto getAllUserEvent(String userId, int page, int size) 
			throws UserIdNotExistException, EasyApperDbException, InvalidDateFormatException, InvalidTimeFormatException {
		return this.getAllUserEvent(userId, new HashMap<>(), page, size, 0);
	}
	
	/**
	 *  Used By UserApi
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param size
	 * @return
	 * @throws UserIdNotExistException
	 * @throws EasyApperDbException
	 * @throws InvalidDateFormatException
	 * @throws InvalidTimeFormatException
	 */
	public UserEventListsContainerDto getAllUserEvent(String userId, Map<String, String> paramMap, int page, int size) throws UserIdNotExistException, EasyApperDbException, InvalidDateFormatException, InvalidTimeFormatException {
		return getAllUserEvent(userId, paramMap, page, size, 0);
	}
	
	/**
	 * @param userId
	 * @return
	 * @throws UserIdNotExistException
	 * @throws EasyApperDbException
	 * @throws InvalidDateFormatException
	 * @throws InvalidTimeFormatException
	 */
	public UserEventListsContainerDto getAllUserEvent(String userId, Map<String, String> paramMap, int page, int size, long skip) 
			throws UserIdNotExistException, EasyApperDbException, InvalidDateFormatException, InvalidTimeFormatException {
		List<PostedEventEntity> postedEntityList  = postedEventDao.getAllEvent(userId, paramMap, page, size, skip);
		List<EventDto> postedDtoList = eventsTranslator.getPostedEventDtoList(postedEntityList);
		List<SubscribedEventEntity> subcEntityList = subscribedEventDao.getAllEvent(userId, paramMap, page, size, skip);
		List<EventDto> subcDtoList = eventsTranslator.getSubscribedEventDtoList(subcEntityList);		
		return new UserEventListsContainerDto(subcDtoList, postedDtoList);
	}
	
}

package com.easyapper.eventsmicroservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.easyapper.eventsmicroservice.dao.DBSeqValueFinder;
import com.easyapper.eventsmicroservice.dao.PostedEventDao;
import com.easyapper.eventsmicroservice.dao.SubscribedEventDao;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidPostedEventIdException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidUpdateEventRequestException;
import com.easyapper.eventsmicroservice.exception.NoExtensionFoundException;
import com.easyapper.eventsmicroservice.exception.PostedEventExistsException;
import com.easyapper.eventsmicroservice.exception.SubscribedEventNotFoundException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.model.EventDto;
import com.easyapper.eventsmicroservice.model.UserEventListsContainerDto;
import com.easyapper.eventsmicroservice.translator.EventsTranslator;
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
		postedEventDao.createTTLIndexAtLastDate(dbName);
		eventEntity.set_id(eventId);
		eventEntity.setUser_id(userId);
		eventEntity.setEvent_type(eventEntity.getEvent_type().toLowerCase());
		if(eventEntity.getEvent_approved() == null) {
			eventEntity.setEvent_approved(EAConstants.APPROVED_VAL_0);
		}
		return postedEventDao.insertEvent(eventEntity);
	}
	
	public String createSubscribededEvent(String userId, EventDto eventDto) throws EasyApperDbException, InvalidPostedEventIdException, EventIdNotExistException {
		if(!validator.isValidPostedEventId(eventDto.getPosted_event_id())) {
			throw new InvalidPostedEventIdException();
		}
		String postedEventUserId = EAUtil.getUserId(eventDto.getPosted_event_id());
		PostedEventEntity postedEventEntity = postedEventDao.getEvent(postedEventUserId, 
				eventDto.getPosted_event_id());
		SubscribedEventEntity subcEventEntity = eventsTranslator.getSubcribedEventEntity(eventDto, postedEventEntity);
		String eventId = EAUtil.getSubscribedEventId(dbSeqFinder.getNextSeqValue(
				EAConstants.FIXED_DB_NAME.SUBSCRIBED_EVENT_COLLECTION_NAME.toString()));
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
			EventDto eventDto) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException, InvalidPostedEventIdException, InvalidUpdateEventRequestException, SubscribedEventNotFoundException {
		if(eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_POSTED)) {
			this.updatePostedEvent(userId, eventId, eventDto);
		}else if(eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_SUBSCRIBED)) {
			this.updateSubscribedEvent(userId, eventId, eventDto);
//			if(!validator.isValidPostedEventId(eventDto.getPosted_event_id())) {
//				throw new InvalidPostedEventIdException();
//			}
//			SubscribedEventEntity subcEventEntity = eventsTranslator.getSubcribedEventEntity(eventDto);
//			subcEventEntity.set_id(null);
//			subcEventEntity.setUser_id(userId);
//			subscribedEventDao.updateEvent(eventId, subcEventEntity);
		}
	}
	
	private void updateSubscribedEvent(String userId, String eventId, 
			EventDto eventDto) throws InvalidUpdateEventRequestException, EventIdNotExistException, InvalidPostedEventIdException, SubscribedEventNotFoundException, EasyApperDbException {
		EventDto existingEventDto = this.getSubscribedEvent(userId, eventId);
		if(validator.isValidUpdateSubscribedEvent(eventDto, existingEventDto)) {
			String postedEventUserId = EAUtil.getUserId(eventDto.getPosted_event_id());
			PostedEventEntity postedEventEntity = postedEventDao.getEvent(postedEventUserId, 
					eventDto.getPosted_event_id());
			SubscribedEventEntity subcEventEntity = eventsTranslator.getSubcribedEventEntity(eventDto, postedEventEntity);
			subcEventEntity.set_id(null);
			subcEventEntity.setUser_id(userId);
			subscribedEventDao.updateEvent(eventId, subcEventEntity);
		}else {
			throw new InvalidUpdateEventRequestException();
		}
	}
	
	
	private void updatePostedEvent(String userId, String eventId, 
			EventDto eventDto) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException, InvalidUpdateEventRequestException {
		EventDto existingEventDto = this.getPostedEvent(userId, eventId);
		if(validator.isValidUpdatePostedEvent(eventDto, existingEventDto)) {	
			PostedEventEntity eventEntity = eventsTranslator.getPostedEventEntity(eventDto);
			eventEntity.set_id(null);
			eventEntity.setUser_id(userId);
			eventEntity.setEvent_type(eventEntity.getEvent_type().toLowerCase());
			postedEventDao.updateEvent(userId, eventId, eventEntity);
		}else {
			throw new InvalidUpdateEventRequestException();
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

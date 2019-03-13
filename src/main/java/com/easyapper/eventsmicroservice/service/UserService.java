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
import com.easyapper.eventsmicroservice.model.EventBookingDto;
import com.easyapper.eventsmicroservice.model.EventDto;
import com.easyapper.eventsmicroservice.model.LocationDto;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EAUtil;
import com.easyapper.eventsmicroservice.utility.EAValidator;

@Service
public class UserService {

	@Autowired
	PostedEventDao postedEventDao ;
	
	@Autowired
	SubscribedEventDao subscribedEventDao;
	
	@Autowired
	DBSeqValueFinder dbSeqFinder;
	
	@Autowired
	EAValidator validator;
	
	public String createPostedEvent(String userId, EventDto eventDto) throws EasyApperDbException, PostedEventExistsException, InvalidDateFormatException, InvalidTimeFormatException {
		if(validator.postedEventExists(userId, eventDto)) {
			throw new PostedEventExistsException();
		}
		PostedEventEntity eventEntity = EAUtil.getPostedEventEntity(eventDto);
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
		SubscribedEventEntity subcEventEntity = EAUtil.getSubcribedEventEntity(eventDto);
		String eventId = EAUtil.getSubscribedEventId(dbSeqFinder.getNextSeqValue(
				EAConstants.SUBSCRIBED_EVENT_COLLECTION_DB_NAME));
		subcEventEntity.set_id(eventId);
		subcEventEntity.setUser_id(userId);
		String id = subscribedEventDao.insertSubscribedEvent(subcEventEntity);
		return id;
	}

	public EventDto getPostedEvent(String userId, String eventId) throws UserIdNotExistException, EventIdNotExistException, EasyApperDbException {
		PostedEventEntity eventEntity = postedEventDao.getEvent(userId, eventId);
		EventDto eventDto = EAUtil.getEventDto(eventEntity);
		return eventDto;
	}
	
	public EventDto getSubscribedEvent(String userId, String eventId) throws EasyApperDbException, SubscribedEventNotFoundException {
		SubscribedEventEntity subcEventEntity = subscribedEventDao.getEvent(eventId, userId);
		return EAUtil.getEventDto(subcEventEntity);
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
			PostedEventEntity eventEntity = EAUtil.getPostedEventEntity(eventDto);
			eventEntity.set_id(null);
			eventEntity.setUser_id(userId);
			eventEntity.setEvent_type(eventEntity.getEvent_type().toLowerCase());
			postedEventDao.updateEvent(userId, eventId, eventEntity);
		}else if(eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_SUBSCRIBED)) {
			if(!validator.isValidPostedEventId(eventDto.getPosted_event_id())) {
				throw new InvalidPostedEventIdException();
			}
			SubscribedEventEntity subcEventEntity = EAUtil.getSubcribedEventEntity(eventDto);
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
	 * Used by EventsApi
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
		List<EventDto> postedDtoList = this.getPostedEventDtoList(postedEntityList);
		List<SubscribedEventEntity> subcEntityList = subscribedEventDao.getAllEvent(userId, paramMap, page, size, skip);
		List<EventDto> subcDtoList = this.getSubscribedEventDtoList(subcEntityList);		
		return new UserEventListsContainerDto(subcDtoList, postedDtoList);
	}
	
	public long getPostedEventsCount(String userId) throws EasyApperDbException {
		return postedEventDao.getEventsCount(userId);
	}
	
//	private void filterEventLists(List<EventDto> postedDtoList, List<EventDto> subcDtoList, 
//			 Map<String, String> paramMap) throws InvalidDateFormatException, InvalidTimeFormatException {
//		if(paramMap.size() == 0) {
//			return;
//		}
//		filterEventLists(postedDtoList, paramMap);
//		filterEventLists(subcDtoList, paramMap);
//	}
//	
//	private void filterEventLists(List<EventDto> eventDtoList, Map<String, String> paramMap) throws InvalidDateFormatException, InvalidTimeFormatException{
//		List<EventDto> toRemoveList = new ArrayList();
//		for(Iterator<EventDto> itrList = eventDtoList.iterator(); itrList.hasNext() ; ) {
//			EventDto eventDto = itrList.next();
//			if(containsEventType(paramMap.get(EAConstants.EVENT_TYPE_KEY), eventDto) && 
//					containsCategory(paramMap.get(EAConstants.EVENT_CATEGORY_KEY), eventDto) &&
//					containsSubCategory(paramMap.get(EAConstants.EVENT_SUB_CATEGORY_KEY), eventDto) &&
//					containsLongitude(paramMap.get(EAConstants.EVENT_LOCATION_LONGITUDE_KEY), eventDto) &&
//					containsLatitude(paramMap.get(EAConstants.EVENT_LOCATION_LATITUDE_KEY), eventDto) &&
//					containsCity(paramMap.get(EAConstants.EVENT_ADDRESS_CITY_KEY), eventDto) &&
//					containsStreet(paramMap.get(EAConstants.EVENT_ADDRESS_STREET_KEY), eventDto) &&
//					containsPin(paramMap.get(EAConstants.EVENT_ADDRESS_PIN_KEY), eventDto) &&
//					containsOrganiserEmail(paramMap.get(EAConstants.EVENT_ORGANIZER_EMAIL_KEY), eventDto) &&
//					containsEventName(paramMap.get(EAConstants.EVENT_NAME_KEY), eventDto) &&
//					containsEventDescription(paramMap.get(EAConstants.EVENT_DESCRIBTION_KEY), eventDto) &&
//					containsEventMinAge(paramMap.get(EAConstants.EVENT_MIN_AGE_KEY), eventDto) &&
//					containsEventMaxAge(paramMap.get(EAConstants.EVENT_MAX_AGE_KEY), eventDto) &&
//					containsEventPrice(paramMap.get(EAConstants.EVENT_PRICE_KEY), eventDto) &&
//					containsStartDate(paramMap.get(EAConstants.EVENT_START_DATE_KEY), eventDto) &&
//					containsLastDate(paramMap.get(EAConstants.EVENT_LAST_DATE_KEY), eventDto) &&
//					containsPostedEventId(paramMap.get(EAConstants.EVENT_POSTED_EVENT_ID_KEY), eventDto) &&
//					containsStartTime(paramMap.get(EAConstants.EVENT_START_TIME_KEY), eventDto) &&
//					containsEndTime(paramMap.get(EAConstants.EVENT_END_TIME_KEY), eventDto) 	
//					) {
//				continue;
//			}
//			toRemoveList.add(eventDto);
//		}
//		eventDtoList.removeAll(toRemoveList);
//	}
//	private boolean containsStartTime(String checkVal, EventDto eventDto) throws InvalidTimeFormatException {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_start_time() == null) {
//			return false;
//		}
//		if(!EAValidator.isValidTime(eventDto.getEvent_start_time())) {
//			throw new InvalidTimeFormatException();
//		}
//		String strTime = eventDto.getEvent_start_time();
//		String strCheckTime = EAUtil.getTimeFormatStr(EAUtil.getTimeFormatObj(checkVal));
//		if(strTime.equals(strCheckTime)) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsEndTime(String checkVal, EventDto eventDto) throws InvalidTimeFormatException {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_end_time() == null) {
//			return false;
//		}
//		if(!EAValidator.isValidTime(eventDto.getEvent_end_time())) {
//			throw new InvalidTimeFormatException();
//		}
//		String strTime = eventDto.getEvent_end_time();
//		String strCheckTime = EAUtil.getTimeFormatStr(EAUtil.getTimeFormatObj(checkVal));
//		if(strTime.equals(strCheckTime)) {
//			return true;
//		}
//		return false;
//	}
//	
//	private boolean containsLastDate(String checkVal, EventDto eventDto) throws InvalidDateFormatException {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_last_date() == null) {
//			return false;
//		}
//		if(!EAValidator.isValidDate(checkVal)) {
//			throw new InvalidDateFormatException();
//		}
//		String strLastDate = eventDto.getEvent_last_date();
//		String checkLastDate = EAUtil.getDateFormatStr(EAUtil.getDateFormatObj(checkVal));
//		if(strLastDate.equals(checkLastDate)) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsStartDate(String checkVal, EventDto eventDto) throws InvalidDateFormatException {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_start_date() == null) {
//			return false;
//		}
//		if(!EAValidator.isValidDate(checkVal)) {
//			throw new InvalidDateFormatException();
//		}
//		String strStartDate = eventDto.getEvent_start_date();
//		String checkStartDate = EAUtil.getDateFormatStr(EAUtil.getDateFormatObj(checkVal));
//		if(strStartDate.equals(checkStartDate)) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsPostedEventId(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getPosted_event_id() == null) {
//			return false;
//		}
//		if(eventDto.getPosted_event_id().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsEventType(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_type() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_type().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsCategory(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_category() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_category().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsSubCategory(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_subcategory() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_subcategory().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsLongitude(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_location().getLongitude() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_location().getLongitude().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsLatitude(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_location().getLatitude() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_location().getLatitude().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsCity(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_location().getAddress().getCity() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_location().getAddress().getCity().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsStreet(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_location().getAddress().getStreet() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_location().getAddress().getStreet().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsPin(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		} if(eventDto.getEvent_location().getAddress().getPin() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_location().getAddress().getPin().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsOrganiserEmail(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getOrganizer_email() == null) {
//			return false;
//		}
//		if(eventDto.getOrganizer_email() == null) {
//			return false;
//		}
//		if(eventDto.getOrganizer_email().toLowerCase().contains(checkVal.toLowerCase())) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsEventName(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		}if(eventDto.getEvent_name() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_name().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsEventDescription(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		} if(eventDto.getEvent_description() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_description().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsEventMinAge(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		} if(eventDto.getEvent_min_age() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_min_age().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsEventMaxAge(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		} if(eventDto.getEvent_max_age() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_max_age().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	private boolean containsEventPrice(String checkVal, EventDto eventDto) {
//		if(checkVal == null) {
//			return true;
//		} if(eventDto.getEvent_price() == null) {
//			return false;
//		}
//		if(eventDto.getEvent_price().toLowerCase().contains(checkVal.toLowerCase()) ) {
//			return true;
//		}
//		return false;
//	}
//	
	private List<EventDto> getPostedEventDtoList(List<PostedEventEntity> eventEntityList){
		List<EventDto> eventDtoList = new ArrayList<>();
		for(PostedEventEntity eventEntity : eventEntityList) {
			eventDtoList.add(EAUtil.getEventDto(eventEntity));
		}
		return eventDtoList;
	}
	private List<EventDto> getSubscribedEventDtoList(List<SubscribedEventEntity> subcEventEntityList){
		List<EventDto> eventDtoList = new ArrayList<>();
		for(SubscribedEventEntity subcEventEntity : subcEventEntityList) {
			eventDtoList.add(EAUtil.getEventDto(subcEventEntity));
		}
		return eventDtoList;
	}
}

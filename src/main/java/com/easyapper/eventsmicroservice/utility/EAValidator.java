package com.easyapper.eventsmicroservice.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.NoExtensionFoundException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.model.CategoryDto;
import com.easyapper.eventsmicroservice.model.EventDto;
import com.easyapper.eventsmicroservice.model.LocationDto;
import com.easyapper.eventsmicroservice.service.CategoryService;
import com.easyapper.eventsmicroservice.service.EventService;
import com.easyapper.eventsmicroservice.service.UserService;

@Component
public class EAValidator {
	
	private static EALogger logger = EALogger.getLogger();
	
	@Autowired
	private EventService eventService;
	
	@Autowired 
	CategoryService categoryService;
	
	@Autowired
	UserService userService;
	
	public boolean isValidImageFile(MultipartFile imageFile) {
		try {
			String fileExtension = EAUtil.getImageFileExtension(imageFile);
			if(fileExtension.toLowerCase().equals(EAConstants.PNG_EXTENTION) ||
					fileExtension.toLowerCase().equals(EAConstants.JPEG_EXTENTION)) {
				return true;
			}else {
				logger.warning(EAConstants.PNG_EXTENTION + " & " + EAConstants.JPEG_EXTENTION);
				return false;
			}
		} catch (NoExtensionFoundException e) {
			logger.warning("No Image File extension found", e);
			return false;
		}
	}
	
	public boolean isValidPageRequest(int page, int size) {
		if(page >= 1 && size >= 1) {
			return true;
		}
		return false;
	}
	
	public boolean isValidEvent(EventDto eventDto) throws EasyApperDbException {
		if(!isValidEventType(eventDto.getEvent_type())) {
			return false;
		}
		if(eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_POSTED)) {
			if(isValidPostedEvent(eventDto)) {
				return true;
			}
		}else if(eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_SUBSCRIBED)) {
			if(isValidSubscribedEvent(eventDto)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidPostedEvent(EventDto eventDto) throws EasyApperDbException {
		//Event Type
		if(!isValidEventType(eventDto.getEvent_type())) {
			return false;
		} 
		if(!eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_POSTED)) {
			logger.warning("event_type cannot be other then posted");
			return false;
		}
		//Date And Time 
		if(!this.isValidEventDateAndTime(eventDto)) {
			return false;
		}
//		//Dates
//		if(eventDto.getEvent_start_date() != null && !isValidDate(eventDto.getEvent_start_date())) {
//			return false;
//		} 
//		if(eventDto.getEvent_last_date() != null && !isValidDate(eventDto.getEvent_last_date())) {
//			return false;
//		} 
//		if(eventDto.getEvent_start_date() != null && 
//				eventDto.getEvent_last_date() != null &&
//				!isValidLastDate(eventDto.getEvent_start_date(), eventDto.getEvent_last_date())) {
//			return false;
//		}
//		//Time
//		if(eventDto.getEvent_start_time() != null && !isValidTime(eventDto.getEvent_start_time())) {
//			return false;
//		}
//		if(eventDto.getEvent_end_time() != null && !isValidTime(eventDto.getEvent_end_time())) {
//			return false;
//		}
//		if(eventDto.getEvent_start_time() != null &&
//				eventDto.getEvent_end_time() != null &&
//				!isValidEndTime(eventDto.getEvent_start_time(), eventDto.getEvent_end_time())) {
//			return false;
//		}
		//Location
		if(eventDto.getEvent_location() == null) {
			logger.warning("event_location should not be null for posted events");
			return false;
		}
		if(!isValidEventLocation(eventDto.getEvent_location())) {
			return false;
		}
		//Booking
		if(eventDto.getEvent_booking() == null) {
			logger.warning("event_booking should not be null for posted events");
			return false;
		}
		//PostedEventId
		if(eventDto.getPosted_event_id() != null) {
			logger.warning("posted_event_id should be null for posted events");
			return false;
		}
		//Category
		if(!isValidCategory(eventDto.getEvent_category())) {
			return false;
		}
		//Approved
		if(!isValidEventApproved(eventDto.getEvent_approved())) {
			return false;
		}
		return true;
	}
	
	public boolean isValidSubscribedEvent(EventDto eventDto) {
		//Event Type
		if(!isValidEventType(eventDto.getEvent_type())) {
			return false;
		} 
		if(!eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_SUBSCRIBED)) {
			logger.warning("event_type cannot be other then subscribed");
			return false;
		}
		//Date And Time 
		if(!this.isValidEventDateAndTime(eventDto)) {
			return false;
		}
//		//Dates
//		if(eventDto.getEvent_start_date() != null && !isValidDate(eventDto.getEvent_start_date())) {
//			return false;
//		} 
//		if(eventDto.getEvent_last_date() != null && !isValidDate(eventDto.getEvent_last_date())) {
//			return false;
//		} 
//		if(eventDto.getEvent_start_date() != null && 
//				eventDto.getEvent_last_date() != null &&
//				!isValidLastDate(eventDto.getEvent_start_date(), eventDto.getEvent_last_date())) {
//			return false;
//		}
//		//Time
//		if(eventDto.getEvent_start_time() != null && !isValidTime(eventDto.getEvent_start_time())) {
//			return false;
//		}
//		if(eventDto.getEvent_end_time() != null && !isValidTime(eventDto.getEvent_end_time())) {
//			return false;
//		}
//		if(eventDto.getEvent_start_time() != null &&
//				eventDto.getEvent_end_time() != null &&
//				!isValidEndTime(eventDto.getEvent_start_time(), eventDto.getEvent_end_time())) {
//			return false;
//		}
		//PostedEventId
		if(eventDto.getPosted_event_id() == null) {
			logger.warning("posted_event_id cannot be null for subcribed events");
			return false;
		}
		//Booking
		if(eventDto.getEvent_booking() != null) {
			logger.warning("event_booking should be null for subcribed events");
			return false;
		}
		//Location
		if(eventDto.getEvent_location() != null) {
			logger.warning("event_location should be null for subcribed events");
			return false;
		}
		return true;
	}
	
	public boolean isValidUpdatePostedEvent(EventDto eventDto, EventDto existingEventDto) throws EasyApperDbException {
		//Event Type
		if(!isValidEventType(eventDto.getEvent_type())) {
			return false;
		} 
		if(!eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_POSTED)) {
			logger.warning("event_type cannot be other then posted");
			return false;
		}
		//Dates
		if(!this.isValidUpdateEventDateAndTime(eventDto, existingEventDto)) {
			return false;
		}
		//Category
		if( eventDto.getEvent_category() != null && !isValidCategory(eventDto.getEvent_category())) {
			return false;
		}
		//Approved
		if( eventDto.getEvent_approved() != null && !isValidEventApproved(eventDto.getEvent_approved())) {
			return false;
		}
		return true;
	}
	
	//SubscricedUpdateEventValidator
	
	public boolean isValidCategory(CategoryDto categoryDto) {
		if(categoryDto.getId() == 0) {
			return true;
		}else {
			logger.warning("id for category should be zero, while posting");
			return false;
		}
	}
	
	public boolean postedEventExists(String userId, EventDto eventDto) throws UserIdNotExistException, EasyApperDbException, InvalidDateFormatException, InvalidTimeFormatException {
		String eventHashKey = EAUtil.getHashString_ForPostedEvent(eventDto);
		int page = 1, size = 200 ;
		List<EventDto> eventList = userService.getAllUserEvent(userId, page, size).getPosted();
		for(page = 2 ; eventList.size() > 0 ; page++) {
			for(EventDto curEventDto : eventList) {
				String curEventHaskKey = EAUtil.getHashString_ForPostedEvent(curEventDto);
				if(eventHashKey.equals(curEventHaskKey)) {
					return true;
				}
			}
			eventList = userService.getAllUserEvent(userId, page, size).getPosted();
		}
		return false;
	}
	
	public boolean isValidCategory(String category) throws EasyApperDbException {
		if(category == null) {
			logger.warning("Category can not be null");
			return false;
		}
		List<CategoryDto> categoryList = categoryService.getCatorgies();
		for(CategoryDto categoryDto : categoryList) {
			String curCategory = categoryDto.getName();
			if(curCategory != null && curCategory.equals(category)) {
				return true;
			}
		}
		logger.warning("Invalid category : " + category);
		return false;
	}
	
	public boolean isValidEventApproved(String eventApproved) throws EasyApperDbException {
		if(eventApproved == null) {
			return true;
		}
		if(eventApproved.toLowerCase().equals(EAConstants.APPROVED_VAL_0) || 
				eventApproved.toLowerCase().equals(EAConstants.APPROVED_VAL_1)) {
			return true;
		}else {
			logger.warning("event_approved should be null, 0 or 1");
			logger.warning("Invalid event_approved : " + eventApproved);
			return false;
		}
	}
	
	
	public boolean isValidPostedEventId(String postedEventId) throws EasyApperDbException {
		try {
			eventService.getPostedEvent(postedEventId);
		} catch (UserIdNotExistException e) {
			logger.warning("Invalid posted_event_id : " + postedEventId);
			return false;
		} catch (EventIdNotExistException e) {
			logger.warning("Invalid posted_event_id : " + postedEventId);
			return false;
		}
		return true;
	}
	
	public static boolean isValidEventType(String eventType) {
		if(eventType == null) {
			logger.info("Invalid event_type : " + eventType);
			return false;
		}
		if(eventType.toLowerCase().equals(EAConstants.EVENT_TYPE_POSTED.toLowerCase()) ||
				eventType.toLowerCase().equals(EAConstants.EVENT_TYPE_SUBSCRIBED.toLowerCase())) {
			return true;
		}
		logger.info("Invalid event_type : " + eventType);
		return false;
	}
	
	public static boolean isValidDate(String strDate) {
		if(strDate == null) {
			logger.warning("Invalid date format : " + strDate);
			return false;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.DATE_FORMAT_PATTERN);
			Date date = dateFormat.parse(strDate);
		}catch(ParseException e) {
			logger.warning("Invalid date format : " + strDate);
			return false;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EAConstants.DATE_FORMAT_PATTERN);
			LocalDate localDate = LocalDate.parse(strDate, formatter);
		}catch(DateTimeParseException e) {
			logger.warning("Invalid date format : " + strDate, e);
			return false;
		}
		
		return true;
	}
	
	public static boolean isValidTime(String strTime) {
		if(strTime == null) {
			logger.warning("Invalid date format : " + strTime);
			return false;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EAConstants.TIME_FORMAT_PATTERN);
			LocalTime locaTime = LocalTime.parse(strTime, formatter);
		}catch(DateTimeParseException e) {
			logger.warning("Invalid time format : " + strTime, e);
			return false;
		}
		return true;
	}
	
	private boolean isValidEventDateAndTime(EventDto eventDto) {
		//Dates
		if(eventDto.getEvent_start_date() != null && !isValidDate(eventDto.getEvent_start_date())) {
			return false;
		} 
		if(eventDto.getEvent_last_date() != null && !isValidDate(eventDto.getEvent_last_date())) {
			return false;
		} 
		if(eventDto.getEvent_start_date() != null && 
				eventDto.getEvent_last_date() != null &&
				!isValidLastDate(eventDto.getEvent_start_date(), eventDto.getEvent_last_date())) {
			return false;
		}
		//Time
		if(eventDto.getEvent_start_time() != null && !isValidTime(eventDto.getEvent_start_time())) {
			return false;
		}
		if(eventDto.getEvent_end_time() != null && !isValidTime(eventDto.getEvent_end_time())) {
			return false;
		}
		if(eventDto.getEvent_start_time() != null &&
				eventDto.getEvent_end_time() != null &&
				!isValidEndTime(eventDto.getEvent_start_time(), eventDto.getEvent_end_time())) {
			return false;
		}
		return true;
	}
	
	private boolean isValidUpdateEventDateAndTime(EventDto eventDto, EventDto existingEventDto) {
		//Date And Time 
		if(!this.isValidEventDateAndTime(eventDto)) {
			return false;
		}
		//Date with Existing event
		if(eventDto.getEvent_start_date() != null && 
				eventDto.getEvent_last_date() == null &&
				existingEventDto.getEvent_last_date() != null && 
				!isValidLastDate(eventDto.getEvent_start_date(), existingEventDto.getEvent_last_date())) {
			return false;
		}
		if(eventDto.getEvent_start_date() == null && 
				eventDto.getEvent_last_date() != null &&
				existingEventDto.getEvent_start_date() != null &&
				!isValidLastDate(existingEventDto.getEvent_start_date(), eventDto.getEvent_last_date())) {
			return false;
		}
		//Time with Existing event
		if(eventDto.getEvent_start_time() != null &&
				eventDto.getEvent_end_time() == null &&
				existingEventDto.getEvent_end_time() != null &&
				!isValidEndTime(eventDto.getEvent_start_time(), existingEventDto.getEvent_end_time())) {
			return false;
		}
		if(eventDto.getEvent_start_time() == null &&
				eventDto.getEvent_end_time() != null &&
				existingEventDto.getEvent_start_time() != null &&
				!isValidEndTime(existingEventDto.getEvent_start_time(), eventDto.getEvent_end_time())) {
			return false;
		}
		return true;
	}
	
	private static boolean isValidEndTime(String strStartTime, String strEndTime) {
		if(strStartTime == null || strEndTime == null) {
			logger.warning("Null value for start_time or end_time");
			return false;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EAConstants.TIME_FORMAT_PATTERN);
		LocalTime startLocalDate = LocalTime.parse(strStartTime, formatter);
		LocalTime endLocalDate = LocalTime.parse(strEndTime, formatter);
		if(!endLocalDate.isAfter(startLocalDate)) {
			logger.warning("Invalid end_time : end_time should be greater then start_time : end_time : "
					+ "" + strEndTime);
			return false;
		}
		return true;
	}
	
	private static boolean isValidLastDate(String strStartDate, String strLastDate) {
		if(strStartDate == null || strLastDate == null) {
			logger.warning("Null value for start_date or last_date");
			return false;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EAConstants.DATE_FORMAT_PATTERN);
		LocalDate startLocalDate = LocalDate.parse(strStartDate, formatter);
		LocalDate endLocalDate = LocalDate.parse(strLastDate, formatter);
		LocalDate curLocalDate = LocalDate.now(); 
		if(!endLocalDate.isAfter(startLocalDate) && 
				!endLocalDate.isEqual(startLocalDate) ) {
			logger.warning("Invalid last_date : last_date should be greater then equal to start_date : "
					+ "last_date : "+ strLastDate);
			return false;
		}
		Date curDateObj = Date.from(curLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		if(!endLocalDate.isAfter(curLocalDate) && !endLocalDate.isEqual(curLocalDate)) {
			logger.warning("Invalid last_date : last_date should be greater then "
					+ " equal to current date - " + EAUtil.getDateFormatStr(curDateObj) + " : last_date : " + strLastDate);
			return false;
		}
		return true;
	}
	
	private static boolean isValidEventLocation(LocationDto locationDto) {
		if(locationDto.getAddress() == null) {
			logger.warning("address cannot be null");
			return false;
		}
		return true;
	}
}

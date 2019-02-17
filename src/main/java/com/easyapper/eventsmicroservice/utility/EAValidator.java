package com.easyapper.eventsmicroservice.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.easyapper.eventsmicroservice.model.LocationDTO;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.NoExtensionFoundException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.model.EventDTO;
import com.easyapper.eventsmicroservice.model.SubscribedEventDTO;
import com.easyapper.eventsmicroservice.service.EventService;

@Component
public class EAValidator {
	
	private static EALogger logger = EALogger.getLogger();
	
	@Autowired
	private EventService eventService;
	
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
	
	public static boolean isValidEvent(EventDTO eventDto) {
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
	
	public static boolean isValidPostedEvent(EventDTO eventDto) {
		//Event Type
		if(!isValidEventType(eventDto.getEvent_type())) {
			return false;
		} 
		if(!eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_POSTED)) {
			logger.warning("event_type cannot be other then posted");
			return false;
		}
		//Dates
		if(!isValidDate(eventDto.getEvent_start_date())) {
			return false;
		} 
		if(!isValidDate(eventDto.getEvent_last_date())) {
			return false;
		} 
		if(!isValidLastDate(eventDto.getEvent_start_date(), eventDto.getEvent_last_date())) {
			return false;
		}
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
		return true;
	}
	
	public static boolean isValidSubscribedEvent(EventDTO eventDto) {
		//Event Type
		if(!isValidEventType(eventDto.getEvent_type())) {
			return false;
		} 
		if(!eventDto.getEvent_type().equals(EAConstants.EVENT_TYPE_SUBSCRIBED)) {
			logger.warning("event_type cannot be other then subscribed");
			return false;
		}
		//Date
		if(!isValidDate(eventDto.getEvent_start_date())) {
			return false;
		} 
		if(!isValidDate(eventDto.getEvent_last_date())) {
			return false;
		} 
		if(!isValidLastDate(eventDto.getEvent_start_date(), eventDto.getEvent_last_date())) {
			return false;
		}
		//Time
		if(!isValidTime(eventDto.getEvent_start_time())) {
			return false;
		}
		if(!isValidTime(eventDto.getEvent_end_time())) {
			return false;
		}
		if(!isValidEndTime(eventDto.getEvent_start_time(), eventDto.getEvent_end_time())) {
			return false;
		}
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
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.DATE_FORMAT_PATTERN);
			Date date = dateFormat.parse(strDate);
		}catch(ParseException e) {
			logger.warning(e.getMessage(), e);
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
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EAConstants.TIME_FORMAT_PATTERN);
			LocalTime locaTime = LocalTime.parse(strTime, formatter);
		}catch(DateTimeParseException e) {
			logger.warning("Invalid time format : " + strTime, e);
			return false;
		}
		return true;
	}
	
	private static boolean isValidEndTime(String strStartTime, String strEndTime) {
		
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
	
	private static boolean isValidEventLocation(LocationDTO locationDto) {
		if(locationDto.getAddress() == null) {
			logger.warning("address cannot be null");
			return false;
		}
		return true;
	}
}

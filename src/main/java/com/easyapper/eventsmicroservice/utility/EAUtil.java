package com.easyapper.eventsmicroservice.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.easyapper.eventsmicroservice.entity.AddressSubEntity;
import com.easyapper.eventsmicroservice.entity.EventBookingSubEntity;
import com.easyapper.eventsmicroservice.entity.LocationSubEntity;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.model.AddressDTO;
import com.easyapper.eventsmicroservice.model.EventBookingDTO;
import com.easyapper.eventsmicroservice.model.LocationDTO;
import com.easyapper.eventsmicroservice.model.EventDTO;

public class EAUtil {
	
	/**
	 * Logger
	 */
	private static EALogger logger = EALogger.getLogger();
	
	public static String getEventCollectionName(String userId) {
		return EAConstants.EVENT_COLLECTION_PREFIX + userId + EAConstants.EVENT_COLLECTION_POSTFIX;
	}

	public static String getPostedEventId(String userId, Long nextSeq) {
		return EAConstants.POSTED_EVENT_ID_PREFIX + userId + "_" + String.valueOf(nextSeq);
	}
	public static String getSubscribedEventId( Long nextSeq) {
		return EAConstants.SUBSCRIBED_EVENT_ID_PREFIX + String.valueOf(nextSeq);
	}

	public static boolean canBePostedEventId(String eventId) {
		if(eventId.startsWith(EAConstants.POSTED_EVENT_ID_PREFIX)) {
			return true;
		}
		return false;
	}
	public static boolean canBeSubscribedEventId(String eventId) {
		if(eventId.startsWith(EAConstants.SUBSCRIBED_EVENT_ID_PREFIX)) {
			return true;
		}
		return false;
	}
	
	public static String getUserId(String postedEventId) {
		String[] arrStr = postedEventId.split("_");
		if(arrStr.length >= 2) {
			return arrStr[1];
		}else {
			return null;
		}
		
	}
	
	public static SubscribedEventEntity getSubcribedEventEntity(EventDTO eventDto) {
		SubscribedEventEntity subscribedEventEntity = new SubscribedEventEntity(
				eventDto.get_id(), eventDto.getUser_id(), eventDto.getPosted_event_id() 
				, eventDto.getEvent_type(),
				EAUtil.getDateFormatObj( eventDto.getEvent_start_date()), 
				EAUtil.getDateFormatObj( eventDto.getEvent_last_date()),
				EAUtil.getTimeFormatObj(eventDto.getEvent_start_time()),
				EAUtil.getTimeFormatObj(eventDto.getEvent_end_time()) );
		return subscribedEventEntity;
	}
	
	public static EventDTO getEventDto(SubscribedEventEntity subcEventEntity) {
		EventDTO eventDto = new EventDTO(subcEventEntity.get_id(), subcEventEntity.getUser_id(),
				subcEventEntity.getEvent_type(),
				EAUtil.getDateFormatStr(subcEventEntity.getEvent_start_date()),
				EAUtil.getDateFormatStr(subcEventEntity.getEvent_last_date()),
				subcEventEntity.getPost_event_id(), 
				EAUtil.getTimeFormatStr(subcEventEntity.getEvent_start_time()),
				EAUtil.getTimeFormatStr(subcEventEntity.getEvent_end_time()));
		return eventDto;
	}
	
	public static EventDTO getEventDto(PostedEventEntity eventEntity) {
		LocationSubEntity locationEntity = eventEntity.getEvent_location();
		AddressSubEntity addressEntity = locationEntity.getAddress();
		EventBookingSubEntity bookingEntity = eventEntity.getEvent_booking();
		
		AddressDTO addressDto = new AddressDTO(addressEntity.getId(), addressEntity.getCity(),
				addressEntity.getStreet(), addressEntity.getPin());
		LocationDTO locationDto = new LocationDTO(locationEntity.getLongitude(),
				locationEntity.getLatitude(), addressDto);
		EventBookingDTO bookingDto = new EventBookingDTO(bookingEntity.getUrl(),
				bookingEntity.getInquiry_url());
		String startDate = EAUtil.getDateFormatStr(eventEntity.getEvent_start_date());
		String lastDate = EAUtil.getDateFormatStr(eventEntity.getEvent_last_date());
		EventDTO eventDto = new EventDTO(eventEntity.get_id(), eventEntity.getUser_id(),
				eventEntity.getEvent_type(),
				eventEntity.getEvent_category(), eventEntity.getEvent_subcategory(),
				locationDto, eventEntity.getOrganizer_email(), eventEntity.getEvent_name(),
				eventEntity.getEvent_description(), eventEntity.getEvent_image_url(),
				startDate, lastDate,
				eventEntity.getEvent_min_age(), eventEntity.getEvent_min_age(),
				eventEntity.getEvent_price(), bookingDto);
				
		return eventDto;
	}
	
	public static PostedEventEntity getPostedEventEntity(EventDTO evenDto) {
		LocationDTO locationDto = evenDto.getEvent_location();
		AddressDTO addressDto = locationDto.getAddress();
		EventBookingDTO bookingDto = evenDto.getEvent_booking();
		AddressSubEntity addressSubEntity = new AddressSubEntity(addressDto.getId(),
				addressDto.getCity(), addressDto.getStreet(), addressDto.getPin());
		LocationSubEntity locationSubEntity = new LocationSubEntity(
				locationDto.getLongitude(), locationDto.getLatitude(), addressSubEntity);
		EventBookingSubEntity bookingEntity = new EventBookingSubEntity(bookingDto.getUrl(),
				bookingDto.getInquiry_url());
		Date startDateObj = EAUtil.getDateFormatObj(evenDto.getEvent_start_date());
		Date lastDateObj = EAUtil.getDateFormatObj(evenDto.getEvent_last_date());
		PostedEventEntity eventEntity = new PostedEventEntity(evenDto.get_id(),
				evenDto.getUser_id(), evenDto.getEvent_type(),
				evenDto.getEvent_category(), evenDto.getEvent_subcategory(),
				locationSubEntity, evenDto.getOrganizer_email(), 
				evenDto.getEvent_name(), evenDto.getEvent_description(),
				evenDto.getEvent_image_url(), startDateObj,
				lastDateObj, evenDto.getEvent_min_age(),
				evenDto.getEvent_max_age(), evenDto.getEvent_price(),
				bookingEntity);
		return eventEntity;
	}

	public static String getDateFormatStr(Date dateObj) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.DATE_FORMAT_PATTERN);
		return dateFormat.format(dateObj);
	}
	public static String getTimeFormatStr(Date dateObj) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.TIME_FORMAT_PATTERN);
		return dateFormat.format(dateObj);
	}

	public static Date getDateFormatObj(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.DATE_FORMAT_PATTERN);
		try {
			return dateFormat.parse(strDate);
		} catch (ParseException e) {
			logger.warning(e.getMessage(), e);
			return null;
		}
	}
	public static Date getTimeFormatObj(String strTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.TIME_FORMAT_PATTERN);
		try {
			return dateFormat.parse(strTime);
		} catch (ParseException e) {
			logger.warning(e.getMessage(), e);
			return null;
		}
	}
}

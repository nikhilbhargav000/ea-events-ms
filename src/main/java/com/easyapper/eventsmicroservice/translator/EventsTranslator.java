package com.easyapper.eventsmicroservice.translator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.easyapper.eventsmicroservice.entity.AddressSubEntity;
import com.easyapper.eventsmicroservice.entity.EventBookingSubEntity;
import com.easyapper.eventsmicroservice.entity.LocationSubEntity;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.model.AddressDto;
import com.easyapper.eventsmicroservice.model.EventBookingDto;
import com.easyapper.eventsmicroservice.model.EventDto;
import com.easyapper.eventsmicroservice.model.LocationDto;
import com.easyapper.eventsmicroservice.utility.EAUtil;

@Component
public class EventsTranslator {
	
	public static SubscribedEventEntity getSubcribedEventEntity(EventDto eventDto) {
		SubscribedEventEntity subscribedEventEntity = new SubscribedEventEntity(
				eventDto.get_id(), eventDto.getUser_id(), eventDto.getPosted_event_id() 
				, eventDto.getEvent_type(),
				EAUtil.getDateFormatObj( eventDto.getEvent_start_date()), 
				EAUtil.getDateFormatObj( eventDto.getEvent_last_date()),
				EAUtil.getTimeFormatObj(eventDto.getEvent_start_time()),
				EAUtil.getTimeFormatObj(eventDto.getEvent_end_time()) );
		return subscribedEventEntity;
	}
	
	public static EventDto getEventDto(SubscribedEventEntity subcEventEntity) {
		EventDto eventDto = new EventDto(subcEventEntity.get_id(), subcEventEntity.getUser_id(),
				subcEventEntity.getEvent_type(),
				EAUtil.getDateFormatStr(subcEventEntity.getEvent_start_date()),
				EAUtil.getDateFormatStr(subcEventEntity.getEvent_last_date()),
				subcEventEntity.getPost_event_id(), 
				EAUtil.getTimeFormatStr(subcEventEntity.getEvent_start_time()),
				EAUtil.getTimeFormatStr(subcEventEntity.getEvent_end_time()));
		return eventDto;
	}
	
	public static EventDto getEventDto(PostedEventEntity eventEntity) {
		LocationSubEntity locationEntity = eventEntity.getEvent_location();
		AddressSubEntity addressEntity = locationEntity.getAddress();
		EventBookingSubEntity bookingEntity = eventEntity.getEvent_booking();
		
		AddressDto addressDto = new AddressDto(addressEntity.getId(), addressEntity.getCity(),
				addressEntity.getStreet(), addressEntity.getPin());
		LocationDto locationDto = new LocationDto(locationEntity.getLongitude(),
				locationEntity.getLatitude(), addressDto);
		EventBookingDto bookingDto = new EventBookingDto(bookingEntity.getUrl(),
				bookingEntity.getInquiry_url());
		String startDate = EAUtil.getDateFormatStr(eventEntity.getEvent_start_date());
		String lastDate = EAUtil.getDateFormatStr(eventEntity.getEvent_last_date());
		EventDto eventDto = new EventDto(eventEntity.get_id(), eventEntity.getUser_id(),
				eventEntity.getEvent_type(),
				eventEntity.getEvent_category(), eventEntity.getEvent_subcategory(),
				locationDto, eventEntity.getOrganizer_email(), eventEntity.getEvent_name(),
				eventEntity.getEvent_description(), eventEntity.getEvent_image_url(),
				startDate, lastDate,
				eventEntity.getEvent_min_age(), eventEntity.getEvent_max_age(),
				eventEntity.getEvent_price(), bookingDto, 
				eventEntity.getOriginal_event(), eventEntity.getEvent_approved());
				
		return eventDto;
	}
	
	public static PostedEventEntity getPostedEventEntity(EventDto evenDto) {
		LocationDto locationDto = evenDto.getEvent_location();
		AddressDto addressDto = locationDto.getAddress();
		EventBookingDto bookingDto = evenDto.getEvent_booking();
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
				bookingEntity, evenDto.getOriginal_event(), evenDto.getEvent_approved());
		return eventEntity;
	}
	
	public List<EventDto> getPostedEventDtoList(List<PostedEventEntity> eventEntityList){
		List<EventDto> eventDtoList = new ArrayList<>();
		for(PostedEventEntity eventEntity : eventEntityList) {
			eventDtoList.add(this.getEventDto(eventEntity));
		}
		return eventDtoList;
	}
	
	public List<EventDto> getSubscribedEventDtoList(List<SubscribedEventEntity> subcEventEntityList){
		List<EventDto> eventDtoList = new ArrayList<>();
		for(SubscribedEventEntity subcEventEntity : subcEventEntityList) {
			eventDtoList.add(this.getEventDto(subcEventEntity));
		}
		return eventDtoList;
	}

}

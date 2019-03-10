package com.easyapper.eventsmicroservice.api;

import java.util.List;
import java.util.Map;

import com.easyapper.eventsmicroservice.entity.EventListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.exception.EventIdNotExistException;
import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.exception.UserIdNotExistException;
import com.easyapper.eventsmicroservice.model.UserEventListsContainerDto;
import com.easyapper.eventsmicroservice.model.EventDto;
import com.easyapper.eventsmicroservice.model.SubscribedEventDto;
import com.easyapper.eventsmicroservice.service.EventService;
import com.easyapper.eventsmicroservice.utility.EALogger;

@CrossOrigin
@Controller
@RequestMapping("events")
public class EventApi {

	@Autowired
	EALogger logger;
	
	@Autowired
	EventService eventService;
	
	/**
	 * Get all Posted events
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<EventListWrapper> getAllEvents( @RequestParam Map<String, String> paramMap, 
			@RequestParam(name="page", required=false, defaultValue="1") int page, 
			@RequestParam(name="total", required=false, defaultValue="10") int total){
		logger.info("In EventApi : getAllEvents");
		logger.info("ParamMap : " + paramMap + " | page : " + page +  ""
				+ " | total : " + total );
		List<EventDto> allPostEvents = null;
		try {
			allPostEvents =  eventService.getAllPostedEvents(paramMap, page, total);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (InvalidDateFormatException | InvalidTimeFormatException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		EventListWrapper wrapper = new EventListWrapper();
		wrapper.setEvents(allPostEvents);

		//return new ResponseEntity<List<EventDTO>>(allPostEvents, HttpStatus.OK);
		return new ResponseEntity<>(wrapper, HttpStatus.OK);
	}
	/**
	 * Get posted event
	 * @param eventId
	 * @return
	 */
	@RequestMapping(value="{eventId}", method=RequestMethod.GET)
	public ResponseEntity<EventDto> getEvent(@PathVariable String eventId){
		logger.info("In EventApi : getAllEvents");
		EventDto postedEventDto = null;
		try {
			postedEventDto = eventService.getPostedEvent(eventId);
		} catch (UserIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (EventIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<EventDto>(postedEventDto, HttpStatus.OK);
	}
	
}

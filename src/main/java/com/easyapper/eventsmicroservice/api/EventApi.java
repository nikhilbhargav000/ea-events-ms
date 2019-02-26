package com.easyapper.eventsmicroservice.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	public ResponseEntity<List<EventDto>> getAllEvents( @RequestParam Map<String, String> paramMap){
		logger.info("In EventApi : getAllEvents");
		logger.info("ParamMap : " + paramMap);
		List<EventDto> allPostEvents = null;
		try {
			allPostEvents =  eventService.getAllPostedEvents(paramMap);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (InvalidDateFormatException | InvalidTimeFormatException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<EventDto>>(allPostEvents, HttpStatus.OK);
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

package com.easyapper.eventsmicroservice.api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.easyapper.eventsmicroservice.model.UserEventListResponseDto;
import com.easyapper.eventsmicroservice.model.UserEventListsContainerDto;
import com.easyapper.eventsmicroservice.service.UserService;
import com.easyapper.eventsmicroservice.utility.EALogger;
import com.easyapper.eventsmicroservice.utility.EAValidator;

@CrossOrigin
@RestController
@RequestMapping(value="users")
public class UserApi {

	@Autowired
	UserService userService;
	
	@Autowired
	EALogger logger;
	
	@Autowired 
	EAValidator validator;
	
	/**
	 * Store Post Event in DB
	 * 
	 * @param userId
	 * @param eventPostDto
	 * @param result
	 * @return
	 */
	@RequestMapping(value="{userId}/events/", method=RequestMethod.POST)
	public ResponseEntity< String > createPostEvent(@PathVariable("userId") String userId, 
			@RequestBody @Valid EventDto eventPostDto, BindingResult result) {
		logger.info("In UserApi : createPostEvent");
		logger.info(eventPostDto.toString());
		if(result.hasErrors() ) {
			result.getAllErrors().stream().forEach(e -> logger.warning(e.getDefaultMessage()));
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}	
		String id = null;
		try {
			if(!validator.isValidPostedEvent(eventPostDto)) {
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			id = userService.createPostedEvent(userId, eventPostDto);
		} catch (PostedEventExistsException e) {
			logger.warning("Unable to create Event | Event already Exists | Event : " + eventPostDto);
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (InvalidDateFormatException | InvalidTimeFormatException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(id, HttpStatus.CREATED); 
	}
	
	/**
	 * Store subscribed event
	 * 
	 * @param userId
	 * @param eventId
	 * @param eventDto
	 * @param result
	 * @return
	 */
	@RequestMapping(value="{userId}/subscribeEvent", method=RequestMethod.POST)
	public ResponseEntity<String> createSubscribedEvent(
			@PathVariable("userId") String userId, @RequestBody @Valid EventDto eventDto, 
			BindingResult result ){
		logger.info("In UserApi : createSubscribedEvent");
		logger.info(eventDto.toString());
		if(result.hasErrors()) {
			result.getAllErrors().stream().forEach(e -> logger.warning(e.getDefaultMessage()));
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		if(!validator.isValidSubscribedEvent(eventDto)) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		String id = null;
		try {
			id = userService.createSubscribededEvent(userId, eventDto);
		} catch (InvalidPostedEventIdException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (EventIdNotExistException  e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
		} 
		return new ResponseEntity<String>(id, HttpStatus.CREATED); 
	}
	
	@RequestMapping(value="{userId}/events/{eventId}", method=RequestMethod.GET)
	public ResponseEntity<EventDto> getUserEvents(@PathVariable("userId") String userId,
			@PathVariable("eventId") String eventId) {
		logger.info("In UserApi : getUserEvents");
		logger.info("In UserApi : getUserEvents : userId : " + userId );
		logger.info("In UserApi : getUserEvents : eventId : " + eventId );
		EventDto eventObj = null;
		try {
			eventObj = userService.getEvent(userId, eventId);
		}
		catch(UserIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(EventIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (SubscribedEventNotFoundException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<EventDto>(eventObj, HttpStatus.OK);
	}
	
	@RequestMapping(value="{userId}/events/{eventId}", method=RequestMethod.PUT)
	public ResponseEntity<String> updateEvent(@PathVariable("userId") String userId, 
			@PathVariable("eventId") String eventId, 
			@RequestBody @Valid EventDto eventUpdateDto, BindingResult result) {
		logger.info("In UserApi : updateEvent");
		logger.info(eventUpdateDto.toString());
		if(result.hasErrors()) {
			result.getAllErrors().stream().forEach( e -> logger.warning(e.getDefaultMessage()));
			return new ResponseEntity<String>(result.getAllErrors().get(0).getDefaultMessage(), 
					HttpStatus.BAD_REQUEST);
		}
		try {
			userService.updateEvent(userId, eventId, eventUpdateDto);
		} catch (UserIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} catch (EventIdNotExistException | SubscribedEventNotFoundException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} catch (InvalidPostedEventIdException | InvalidUpdateEventRequestException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		String resMsg = "Success";
		return new ResponseEntity<String>(resMsg, HttpStatus.OK);
	}
	
	@RequestMapping(value="{userId}/events/{eventId}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteUserEvent(@PathVariable("userId") String userId,
			@PathVariable("eventId") String eventId) {
		logger.info("In UserApi : deleteUserEvent");
		try {
			userService.deleteEvent(userId, eventId);
		} catch (UserIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} catch (EventIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} catch (SubscribedEventNotFoundException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		String resMsg = "Success";
		return new ResponseEntity<String>(resMsg, HttpStatus.OK);
	}
	
	@RequestMapping(value="{userId}/events", method=RequestMethod.GET)
	public ResponseEntity<UserEventListResponseDto> getAllUserEvents(@PathVariable("userId") String userId, 
			@RequestParam Map<String, String> paramMap, 
			@RequestParam(name="page", required=false, defaultValue="1") int page, 
			@RequestParam(name="size", required=false, defaultValue="10") int size) {
		logger.info("In UserApi : getAllUserEvents");
		logger.info("ParamMap : " + paramMap + " | page : " + page +  ""
				+ " | size : " + size );
		UserEventListsContainerDto allEventResponse = null;
		try {
			if(!validator.isValidPageRequest(page, size)) {
				logger.warning("Invalid page and size value");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			allEventResponse = userService.getAllUserEvent(userId, paramMap, page, size);
		} catch (UserIdNotExistException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (InvalidDateFormatException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch (InvalidTimeFormatException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		UserEventListResponseDto responseDto = new UserEventListResponseDto(allEventResponse);
		return new ResponseEntity<UserEventListResponseDto>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="{userId}/image", method=RequestMethod.POST)
	public ResponseEntity<String> uploadImage(HttpServletRequest request
			, @PathVariable("userId") String userId
			,@RequestParam(value = "file", required=true) MultipartFile imageFile){
		logger.info("In UserApi : uploadImage");
		if(!validator.isValidImageFile(imageFile)) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		String imageUrl = null;
		try {
			imageUrl = userService.storeImage(request, userId, imageFile);
		}catch(IOException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (NoExtensionFoundException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(imageUrl, HttpStatus.CREATED);
	}
}

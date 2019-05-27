package com.easyapper.eventsmicroservice.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.model.ProviderDto;
import com.easyapper.eventsmicroservice.model.ProviderResponseDto;
import com.easyapper.eventsmicroservice.service.EventService;
import com.easyapper.eventsmicroservice.utility.EALogger;

@Controller
@RequestMapping(value="providers")
public class ProviderApi {

	@Autowired
	EALogger logger;
	
	@Autowired
	EventService eventService;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<ProviderResponseDto> getProviders(@RequestParam(name="page", required=false, defaultValue="1") int page, 
			@RequestParam(name="total", required=false, defaultValue="100") int total){
		logger.info("In ProviderApi : getProviders");
		
		List<ProviderDto> providers;
		try {
			providers = eventService.getProviders(page, total);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		ProviderResponseDto responseDto = new ProviderResponseDto();
		responseDto.setProviders(providers);
		return new ResponseEntity<ProviderResponseDto>(responseDto, HttpStatus.OK);
	}
}

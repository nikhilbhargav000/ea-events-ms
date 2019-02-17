package com.easyapper.eventsmicroservice.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easyapper.eventsmicroservice.exception.InvalidImageFileNameException;
import com.easyapper.eventsmicroservice.service.ImageService;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EALogger;

@Controller
@RequestMapping(value=EAConstants.IMAGE_API_MAPPING)
public class ImageApi {

	@Autowired
	EALogger logger;
	
	@Autowired
	ImageService imageService;
	
	@RequestMapping(value="/{imageName}", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(
			@PathVariable(value="imageName", required=true) String imageName) {
		logger.info("In ImageApi : getImage : " + imageName);
		byte[] byteArr;
		try {
			byteArr = imageService.getImage(imageName);
		} catch (IOException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<byte[]>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (InvalidImageFileNameException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<byte[]>(byteArr, HttpStatus.OK);
	}
}

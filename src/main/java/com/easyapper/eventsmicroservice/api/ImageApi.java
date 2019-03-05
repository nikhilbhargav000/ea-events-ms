package com.easyapper.eventsmicroservice.api;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeader;

import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	public void getImage(HttpServletResponse response, 
			@PathVariable(value="imageName", required=true) String imageName) {
		logger.info("In ImageApi : getImage : " + imageName);
		byte[] byteArr = null;
		try {
			byteArr = imageService.getImage(imageName);
			response.getOutputStream().write(byteArr);
		} catch (IOException e) {
			logger.warning(e.getMessage(), e);
			response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
			return;
		} catch (InvalidImageFileNameException e) {
			logger.warning(e.getMessage(), e);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}
		response.setContentType(MediaType.IMAGE_PNG_VALUE);
		response.setStatus(HttpStatus.OK.value());
	}
}
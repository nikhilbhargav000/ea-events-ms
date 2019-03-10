package com.easyapper.eventsmicroservice.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.model.CategoriesResponseDto;
import com.easyapper.eventsmicroservice.model.CategoryDto;
import com.easyapper.eventsmicroservice.service.CategoryService;
import com.easyapper.eventsmicroservice.utility.EALogger;
import com.easyapper.eventsmicroservice.utility.EAValidator;

@CrossOrigin
@RestController
@RequestMapping(value="categories")
public class CategoryApi {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	EALogger logger;
	
	@Autowired
	EAValidator validator;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<CategoriesResponseDto> getCategories(@RequestParam(name="page", required=false, defaultValue="1") int page, 
			@RequestParam(name="size", required=false, defaultValue="10") int size){
		logger.info("In CategoryApi : getCategories" + " | page : " + page +  ""
				+ " | size : " + size );
		List<CategoryDto> categoryList;
		try {
			if(!validator.isValidPageRequest(page, size)) {
				logger.warning("Invalid page and size value");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			categoryList = categoryService.getCatorgies(page, size);
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		CategoriesResponseDto responseDto = new CategoriesResponseDto(categoryList);
		return new ResponseEntity<CategoriesResponseDto>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<String> createCategory(@RequestBody CategoryDto categoryDto){
		
		return null;
	}
	
}

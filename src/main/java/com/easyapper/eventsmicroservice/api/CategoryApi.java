package com.easyapper.eventsmicroservice.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.model.CategoriesResponseDto;
import com.easyapper.eventsmicroservice.model.CategoryDto;
import com.easyapper.eventsmicroservice.service.CategoryService;
import com.easyapper.eventsmicroservice.utility.EALogger;

@RestController
@RequestMapping(value="categories")
public class CategoryApi {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	EALogger logger;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<CategoriesResponseDto> getCategories(){
		logger.info("In CategoryApi : getCategories");
		List<CategoryDto> categoryList;
		try {
			categoryList = categoryService.getCatorgies();
		} catch (EasyApperDbException e) {
			logger.warning(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		CategoriesResponseDto responseDto = new CategoriesResponseDto(categoryList);
		return new ResponseEntity<CategoriesResponseDto>(responseDto, HttpStatus.OK);
	}
	
}

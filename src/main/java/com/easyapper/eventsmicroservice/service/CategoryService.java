package com.easyapper.eventsmicroservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapper.eventsmicroservice.dao.CategoryDao;
import com.easyapper.eventsmicroservice.dao.DBSeqValueFinder;
import com.easyapper.eventsmicroservice.entity.CategoryEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.model.CategoryDto;
import com.easyapper.eventsmicroservice.translator.CategoriesTranslator;
import com.easyapper.eventsmicroservice.utility.EAConstants;

@Service
public class CategoryService {
	
	@Autowired
	CategoriesTranslator categoriesTranslator;
	@Autowired
	DBSeqValueFinder dbSeqFinder;
	@Autowired
	CategoryDao categoryDao;

	public int createCategory(CategoryDto categoryDto) throws EasyApperDbException {
		CategoryEntity categoryEntity = categoriesTranslator.getCategoryEntity(categoryDto);
		int categoryId = dbSeqFinder.getNextSeqValue(EAConstants.FIXED_DB_NAME.SUBSCRIBED_EVENT_COLLECTION_NAME.toString()).intValue();
		categoryEntity.set_id(categoryId);
		categoryId = categoryDao.insertCategory(categoryEntity);
		return categoryId;
	}
	
	public List<CategoryDto> getCatorgies() throws EasyApperDbException{
		return this.getCatorgies(EAConstants.INVALID_PAGINATION_VALUE, 
				EAConstants.INVALID_PAGINATION_VALUE);
	}
	
	public List<CategoryDto> getCatorgies(int page, int total) throws EasyApperDbException{
		
		List<CategoryEntity> categoryEntityList = new ArrayList<>();
		if(page != EAConstants.INVALID_PAGINATION_VALUE &&
				total != EAConstants.INVALID_PAGINATION_VALUE) {
			categoryEntityList = categoryDao.getAllCategories(page, total);
		}else {
			categoryEntityList = categoryDao.getAllCategories();
		}
		
//		List<CategoryDto> categoriesList = new ArrayList<>();
//		for(CategoryEntity curCategoryEntity : categoryEntityList) {
//			CategoryDto categoryDto = new CategoryDto(curCategoryEntity.get_id(), 
//					curCategoryEntity.getName(), curCategoryEntity.getImage_url());
//			categoriesList.add(categoryDto);
//		}
		return categoriesTranslator.getCategoriesList(categoryEntityList);
	}
	
}

package com.easyapper.eventsmicroservice.dao;

import java.util.List;

import com.easyapper.eventsmicroservice.entity.CategoryEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;

public interface CategoryDao {

	public List<CategoryEntity> getAllCategories(int page, int size) throws EasyApperDbException;
	
	public List<CategoryEntity> getAllCategories() throws EasyApperDbException;
	
	public int insertCategory(CategoryEntity categoryEntity) throws EasyApperDbException ;
	
}

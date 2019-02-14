package com.easyapper.eventsmicroservice.dao;

import java.util.List;

import com.easyapper.eventsmicroservice.entity.CategoryEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;

public interface CategoryDao {
	
	public List<CategoryEntity> getAllCategories() throws EasyApperDbException;
	
}

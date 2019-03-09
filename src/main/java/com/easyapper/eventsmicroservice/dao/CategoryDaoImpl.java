package com.easyapper.eventsmicroservice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.easyapper.eventsmicroservice.entity.CategoryEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.utility.EALogger;

@Repository
public class CategoryDaoImpl implements CategoryDao {

	@Autowired
	EALogger logger;
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<CategoryEntity> getAllCategories(int page, int total) throws EasyApperDbException {		
		try {
			Query query = new Query().with(new PageRequest(page-1, total));
			return mongoTemplate.find(query, CategoryEntity.class);
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
	}
	
	@Override
	public List<CategoryEntity> getAllCategories() throws EasyApperDbException {		
		try {
			return mongoTemplate.findAll(CategoryEntity.class);
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
	}
	
}

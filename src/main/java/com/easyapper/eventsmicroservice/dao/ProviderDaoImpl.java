package com.easyapper.eventsmicroservice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.easyapper.eventsmicroservice.dao.helper.DaoHepler;
import com.easyapper.eventsmicroservice.entity.ProviderEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.utility.EALogger;

@Repository
public class ProviderDaoImpl implements ProviderDao {
	
	@Autowired
	EALogger logger;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	DaoHepler daoHelper;
	
	public List<ProviderEntity> getProviders(int page, int size) throws EasyApperDbException{
		List<ProviderEntity> providers = new ArrayList<>();
		Query query = new Query();
		daoHelper.setPaginationInQuery(query, page, size);
		try {
			providers = mongoTemplate.find(query, ProviderEntity.class);
		}catch(Exception e) {
			logger.warning(e.getMessage(), e);
			throw new EasyApperDbException();
		}
		return providers;
	}
	
}

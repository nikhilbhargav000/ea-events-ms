package com.easyapper.eventsmicroservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.easyapper.eventsmicroservice.entity.CounterEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;

@Component
public class DBSeqValueFinder {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public Long getNextSeqValue(String dbSequenceName) throws EasyApperDbException{
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(dbSequenceName));
		Update update = new Update();
		update.inc("seq", 1);
		CounterEntity counterEntity = null;
		try {
			counterEntity = mongoTemplate.findAndModify(query, update,
						CounterEntity.class);
			if (counterEntity == null ) {
				counterEntity = new CounterEntity(dbSequenceName, new Long(1001));
				mongoTemplate.insert(counterEntity);
				return counterEntity.getSeq();
			}
		}catch(Exception e) {
			throw new EasyApperDbException();
		}
		return counterEntity.getSeq() + 1;
	}
	
}

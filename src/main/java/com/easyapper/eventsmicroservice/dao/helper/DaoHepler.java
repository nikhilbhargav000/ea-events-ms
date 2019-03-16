package com.easyapper.eventsmicroservice.dao.helper;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.utility.EAUtil;
import com.easyapper.eventsmicroservice.utility.EAValidator;

@Component
public class DaoHepler {
	
	@Autowired
	EAValidator validator;

	public void addSearchCriteriaForString(final String paramKey, final String dbEntityField, Query query, final Map<String, String> paramMap) throws InvalidTimeFormatException, InvalidDateFormatException {
		if(paramMap.get(paramKey) != null) {
			Pattern alikeCaseInsentitvePattern = Pattern.compile(Pattern.quote(paramMap.get(paramKey)) , Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where(dbEntityField).regex(alikeCaseInsentitvePattern));
		}
	}
	
	public void addSearchCriteriaForDate(final String paramFromDateKey, final String paramToDateKey, final String dbEntityField, Query query, final Map<String, String> paramMap) throws InvalidDateFormatException {
		if(paramMap.get(paramFromDateKey) != null && 
				paramMap.get(paramToDateKey) != null && 
				validator.isValidDate(paramMap.get(paramFromDateKey)) &&
				validator.isValidDate(paramMap.get(paramToDateKey)) ) {
				query.addCriteria(Criteria.where(dbEntityField)
						.gte(EAUtil.getDateFormatObj(paramMap.get(paramFromDateKey)))
						.lte(EAUtil.getDateFormatObj(paramMap.get(paramToDateKey))));
		}else if(paramMap.get(paramFromDateKey) != null && 
				validator.isValidDate(paramMap.get(paramFromDateKey))) {
			query.addCriteria(Criteria.where(dbEntityField)
					.gte(EAUtil.getDateFormatObj(paramMap.get(paramFromDateKey))));
		}else if(paramMap.get(paramToDateKey) != null
				&& validator.isValidDate(paramMap.get(paramToDateKey))) {
			query.addCriteria(Criteria.where(dbEntityField)
					.lte(EAUtil.getDateFormatObj(paramMap.get(paramToDateKey))));
		}
	}
	
	public void addSearchCriteriaForTime(final String paramFromTimeKey, final String paramToTimeKey, final String dbEntityField, Query query, final Map<String, String> paramMap) throws InvalidDateFormatException {
		if(paramMap.get(paramFromTimeKey) != null && 
				paramMap.get(paramToTimeKey) != null && 
				validator.isValidTime(paramMap.get(paramFromTimeKey)) &&
				validator.isValidTime(paramMap.get(paramToTimeKey)) ) {
				query.addCriteria(Criteria.where(dbEntityField)
						.gte(EAUtil.getTimeFormatObj(paramMap.get(paramFromTimeKey)))
						.lte(EAUtil.getTimeFormatObj(paramMap.get(paramToTimeKey))));
		}else if(paramMap.get(paramFromTimeKey) != null && 
				validator.isValidTime(paramMap.get(paramFromTimeKey))) {
			query.addCriteria(Criteria.where(dbEntityField)
					.gte(EAUtil.getTimeFormatObj(paramMap.get(paramFromTimeKey))));
		}else if(paramMap.get(paramToTimeKey) != null
				&& validator.isValidTime(paramMap.get(paramToTimeKey))) {
			query.addCriteria(Criteria.where(dbEntityField)
					.lte(EAUtil.getTimeFormatObj(paramMap.get(paramToTimeKey))));
		}
	}
	
	public void setPaginationInQuery(Query query, int page, int size, long skip){
		long totalskip = ((page - 1) * size) + skip;
		query.skip(totalskip);
		query.limit(size);
	}
	
}

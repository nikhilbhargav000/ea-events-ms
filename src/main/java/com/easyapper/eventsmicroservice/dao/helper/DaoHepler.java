package com.easyapper.eventsmicroservice.dao.helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EALogger;
import com.easyapper.eventsmicroservice.utility.EAUtil;

@Component
public class DaoHepler {
	
	@Autowired
	EALogger logger;

	public void addUpdateForField(final Object updateValue, final String dbEntityField, Update update) {
		if(updateValue != null) {
			update.set(dbEntityField, updateValue);
		}
	}

	public List<Criteria> createCriteriaList(){
		return new ArrayList<>();
	}
	
	public void addAlikeSearchCriteriaForString(final String paramKey, final String dbEntityField, 
			List<Criteria> orOperatorCriterias, final Map<String, String> paramMap) 
			throws InvalidTimeFormatException, InvalidDateFormatException {
		
		if(paramMap.get(paramKey) != null) {
			String values[] = paramMap.get(paramKey).split(EAConstants.COMMA); 
			Criteria criteriaArr[] = new Criteria[values.length]; 
			for (int index = 0 ; index < criteriaArr.length ; index++) {
				String value = values[index];
				Pattern alikeCaseInsentitvePattern = Pattern.compile(Pattern.quote(value.trim()) , Pattern.CASE_INSENSITIVE);
				Criteria newCriteria = Criteria.where(dbEntityField).regex(alikeCaseInsentitvePattern);
				criteriaArr[index] = newCriteria;
			}
			orOperatorCriterias.add(new Criteria().orOperator(criteriaArr));
		}
	}
	
	/** Not in use at present */
	public void addEqualSearchCriteriaForString(final String paramKey, final String dbEntityField, 
			List<Criteria> orOperatorCriterias, final Map<String, String> paramMap) 
			throws InvalidTimeFormatException, InvalidDateFormatException {
		
		if(paramMap.get(paramKey) != null) {
			String values[] = paramMap.get(paramKey).split(EAConstants.COMMA); 
			Criteria criteriaArr[] = new Criteria[values.length]; 
			for (int index = 0 ; index < criteriaArr.length ; index++) {
				String value = values[index];
				Criteria newCriteria = Criteria.where(dbEntityField).is(value.trim());
				criteriaArr[index] = newCriteria;
			}
			orOperatorCriterias.add(new Criteria().orOperator(criteriaArr));
		}
	}
	
	public void addAndOperationOnList(Query query, List<Criteria> orOperatorCriterias) {
		if(CollectionUtils.isNotEmpty(orOperatorCriterias)) {
			Criteria[] criteriaArr = new Criteria[orOperatorCriterias.size()];
			orOperatorCriterias.toArray(criteriaArr);
			query.addCriteria(new Criteria().andOperator(criteriaArr));
		}
	}
	
	
	
	public void addSearchCriteriaForDate(final String paramFromDateKey, final String paramToDateKey, final String dbEntityField, Query query, final Map<String, String> paramMap) throws InvalidDateFormatException {
		try {
			if(paramMap.get(paramFromDateKey) != null && 
					paramMap.get(paramToDateKey) != null) {
				query.addCriteria(Criteria.where(dbEntityField)
						.gte(EAUtil.getDateFormatObj_WithException(paramMap.get(paramFromDateKey)))
						.lte(EAUtil.getDateFormatObj_WithException(paramMap.get(paramToDateKey))));
			}else if(paramMap.get(paramFromDateKey) != null) {
				query.addCriteria(Criteria.where(dbEntityField)
						.gte(EAUtil.getDateFormatObj_WithException(paramMap.get(paramFromDateKey))));
			}else if(paramMap.get(paramToDateKey) != null) {
				query.addCriteria(Criteria.where(dbEntityField)
						.lte(EAUtil.getDateFormatObj_WithException(paramMap.get(paramToDateKey))));
			}
		} catch (ParseException e) {
			logger.warning("Invalid date format : " + paramMap.get(paramFromDateKey) + " "
					+ paramMap.get(paramToDateKey) );
			throw new InvalidDateFormatException();
		}
	}
	
	public void addSearchCriteriaForTime(final String paramFromTimeKey, final String paramToTimeKey, final String dbEntityField, Query query, final Map<String, String> paramMap) throws InvalidTimeFormatException {
		try {
			if(paramMap.get(paramFromTimeKey) != null && 
					paramMap.get(paramToTimeKey) != null) {
					query.addCriteria(Criteria.where(dbEntityField)
							.gte(EAUtil.getTimeFormatObj_WithException(paramMap.get(paramFromTimeKey)))
							.lte(EAUtil.getTimeFormatObj_WithException(paramMap.get(paramToTimeKey))));
			}else if(paramMap.get(paramFromTimeKey) != null) {
				query.addCriteria(Criteria.where(dbEntityField)
						.gte(EAUtil.getTimeFormatObj_WithException(paramMap.get(paramFromTimeKey))));
			}else if(paramMap.get(paramToTimeKey) != null) {
				query.addCriteria(Criteria.where(dbEntityField)
						.lte(EAUtil.getTimeFormatObj_WithException(paramMap.get(paramToTimeKey))));
			}
		} catch(ParseException e) {
			logger.warning("Invalid date format : " + paramMap.get(paramFromTimeKey) + " "
					+ paramMap.get(paramToTimeKey));
			throw new InvalidTimeFormatException();
		}
	}
	
	public void setPaginationInQuery(Query query, int page, int size) {
		setPaginationInQuery(query, page, size, 0);
	}
	
	public void setPaginationInQuery(Query query, int page, int size, long skip){
		long totalskip = ((page - 1) * size) + skip;
		query.skip(totalskip);
		query.limit(size);
	}
	
	public long getTotalSkip(int page, int size, long skip) {
		return ((page - 1) * size) + skip;
	}
		
}

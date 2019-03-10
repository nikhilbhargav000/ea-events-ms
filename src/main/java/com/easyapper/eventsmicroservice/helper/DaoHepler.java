package com.easyapper.eventsmicroservice.helper;

import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.easyapper.eventsmicroservice.exception.InvalidDateFormatException;
import com.easyapper.eventsmicroservice.exception.InvalidTimeFormatException;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EAUtil;
import com.easyapper.eventsmicroservice.utility.EAValidator;

@Component
public class DaoHepler {
	
	@Autowired
	EAValidator validator;
	
	public void addCriteriaForSearch(final String paramKey, final String dbEntityField, Query query, final Map<String, String> paramMap) throws InvalidTimeFormatException, InvalidDateFormatException {
		if(paramKey.equals(EAConstants.EVENT_START_DATE_KEY) || 
				paramKey.equals(EAConstants.EVENT_LAST_DATE_KEY)) {
			if(paramMap.get(paramKey) != null) {
				if(validator.isValidDate(paramMap.get(paramKey))) {
					Date paramDateObj = EAUtil.getDateFormatObj(paramMap.get(paramKey));
					query.addCriteria(Criteria.where(dbEntityField).is(paramDateObj));
				}else {
					throw new InvalidDateFormatException();
				}
			}
		}
		else if(paramKey.equals(EAConstants.EVENT_START_TIME_KEY) || 
				paramKey.equals(EAConstants.EVENT_END_TIME_KEY)) {
			if(paramMap.get(paramKey) != null) {
				if(validator.isValidDate(paramMap.get(paramKey))) {
					
				}else {
					throw new InvalidTimeFormatException();
				}
			}
		}
		else {	//For String Values
			if(paramMap.get(paramKey) != null) {
				Pattern alikeCaseInsentitvePattern = Pattern.compile(Pattern.quote(paramMap.get(paramKey)) , Pattern.CASE_INSENSITIVE);
				query.addCriteria(Criteria.where(dbEntityField).regex(alikeCaseInsentitvePattern));
			}
		}
	}

}

package com.easyapper.eventsmicroservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapper.eventsmicroservice.dao.PostedEventDao;
import com.easyapper.eventsmicroservice.entity.EventProviderEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.model.ProviderDto;
import com.easyapper.eventsmicroservice.translator.EventsTranslator;

@Service
public class ProviderService {

	@Autowired
	PostedEventDao postedEventDao;
	@Autowired 
	EventService eventService;
	@Autowired
	EventsTranslator eventsTranslator;
	
	public List<ProviderDto> getProviders(int page, int size) throws EasyApperDbException {
		
		List<String> eventCollectionNameList = postedEventDao.getAllEventCollectionName();
		List<String> userIdList = eventService.getUserIdList(eventCollectionNameList);
		List<EventProviderEntity> providerEntities = new ArrayList<>();
		long toSkip = (page-1) * size;
		int toPickSize = size;
		for(String userId : userIdList) {
			List<EventProviderEntity> userEventProviderEntities = postedEventDao.getEventProviders(userId, 1, toPickSize, toSkip); 
			if(userEventProviderEntities.size() == 0) {
				toSkip -= postedEventDao.getEventProvidersCount(userId);
			}else if(userEventProviderEntities.size() > 0) {
				toSkip = 0;
				toPickSize -= userEventProviderEntities.size();
			}
			providerEntities.addAll(userEventProviderEntities);
			if(toSkip < 0) {
				toSkip = 0;
			}
			if(toPickSize <= 0) {
				break;
			}
		}
		return eventsTranslator.getProviderDtoList(providerEntities);
	}
}

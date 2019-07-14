package com.easyapper.eventsmicroservice.dao;

import java.util.List;

import com.easyapper.eventsmicroservice.entity.ProviderEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;

public interface ProviderDao {

	public List<ProviderEntity> getProviders(int page, int size) throws EasyApperDbException;
}

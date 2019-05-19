package com.easyapper.eventsmicroservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.easyapper.eventsmicroservice.model.originalevent.OrglEventDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Refers to PostedEventEntity
 * @author nikhil
 *
 */

@Document
@AllArgsConstructor
@Getter @Setter
@ToString
public class EventProviderEntity {
	
	@Id
	private String organizer_email;
	
}

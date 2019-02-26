package com.easyapper.eventsmicroservice.model.originalevent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class OrglEventUpcomingOccurrenceDto {
	
	private long occurrence_id;
	private String date;
	private String end_date;
	private String start_time;
	private String end_time;
	private int single_occurrence;
	private String timezone;
	private boolean enable_ticketing;
	

}

package com.easyapper.eventsmicroservice.model.originalevent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class OrglEventVenueDto {
	private String name;
	private String address;
	private double lat;
	private double lon;
	private String city;
	
}

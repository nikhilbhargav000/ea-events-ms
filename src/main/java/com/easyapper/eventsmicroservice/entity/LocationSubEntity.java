package com.easyapper.eventsmicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class LocationSubEntity {
	
	private String longitude;
	private String latitude;
	private AddressSubEntity address;
}

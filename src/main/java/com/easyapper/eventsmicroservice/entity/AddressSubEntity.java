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
public class AddressSubEntity {
	private String id;
	private String city;
	private String street;
	private String pin;
	
}

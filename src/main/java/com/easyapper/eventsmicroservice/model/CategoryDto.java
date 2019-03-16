package com.easyapper.eventsmicroservice.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter @Setter
@ToString
public class CategoryDto {
	private int id;
	@NotEmpty(message="name should not be empty")
	private String name;
	@NotEmpty(message="image_url should not be empty")
	private String image_url;
	
}
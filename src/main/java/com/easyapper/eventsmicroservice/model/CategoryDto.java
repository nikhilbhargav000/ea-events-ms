package com.easyapper.eventsmicroservice.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class CategoryDto {
	@Null(message="_id should be null")
	private int _id;
	@NotEmpty(message="name should not be empty")
	private String name;
	@NotEmpty(message="image_url should not be empty")
	private String image_url;
	
	//Constructor
	public CategoryDto(int _id, String name, String image_url) {
		super();
		this._id = _id;
		this.name = name;
		this.image_url = image_url;
	}
	
}
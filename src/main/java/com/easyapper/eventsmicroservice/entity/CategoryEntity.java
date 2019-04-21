package com.easyapper.eventsmicroservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter @Setter
@ToString
@Document(collection="CategoryCollection")
public class CategoryEntity {
	
	@Id
	private int _id;
	private String name;
	private String image_url;
	private String regex_file_name;
}

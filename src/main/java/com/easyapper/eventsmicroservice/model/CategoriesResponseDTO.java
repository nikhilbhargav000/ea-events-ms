package com.easyapper.eventsmicroservice.model;

import java.util.List;

public class CategoriesResponseDTO {
	List<CategoryDTO> categories;
	//Constructor
	public CategoriesResponseDTO(List<CategoryDTO> categories) {
		super();
		this.categories = categories;
	}
	//Getter and Setter
	public List<CategoryDTO> getCategories() {
		return categories;
	}
	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}
	@Override
	public String toString() {
		return "CategoriesResponseDTO [categories=" + categories + "]";
	}
}

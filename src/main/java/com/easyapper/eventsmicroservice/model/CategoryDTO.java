package com.easyapper.eventsmicroservice.model;

public class CategoryDTO {

	private int _id;
	private String name;
	private String image_url;
	
	//Constructor
	public CategoryDTO(int _id, String name, String image_url) {
		super();
		this._id = _id;
		this.name = name;
		this.image_url = image_url;
	}
	
	//Getter and Setters;
	public int get_id() {
		return _id;
	}
	
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
}

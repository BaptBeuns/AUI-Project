package com.spots.data.model;

public class Category {

	int id;
	String name;
	String logo;

	public Category(int id) {
		this.id = id;
		this.name = new String();
		this.logo = new String();
	}

	public setId(int id) {
		this.id = id;
	}

	public setName(String name) {
		this.name = name;
	}

	public setLogo(String logo) {
		this.logo = logo;
	}

}
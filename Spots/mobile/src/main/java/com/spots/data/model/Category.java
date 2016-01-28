package com.spots.data.model;

public class Category {

	int id;
	String name;
	String logo;

	public Category() {
		this.name = new String();
		this.logo = new String();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

    public String getName() {
        return this.name;
    }

    public String getLogo() { return this.logo; }

    //public Image getLogo() { return this.logo; }

}
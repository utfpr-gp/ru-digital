package com.example.model;

public class Item {

	public int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String name;

	// getter and setter methods

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Item() {
		this.id = id;
		this.name = name;
	}

}
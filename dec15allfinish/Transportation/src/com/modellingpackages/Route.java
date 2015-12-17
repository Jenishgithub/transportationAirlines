package com.modellingpackages;
public class Route {
	private String id;
	private String name;
	private String number;
	private String full_route;
	private String added_date;
	private String is_deleted;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setFull_route(String full_route) {
		this.full_route = full_route;
	}

	public String getFull_route() {
		return full_route;
	}

	public void setAdded_date(String added_date) {
		this.added_date = added_date;
	}

	public String getAdded_date() {
		return added_date;
	}

	public void setIs_deleted(String is_deleted) {
		this.is_deleted = is_deleted;
	}

	public String getIs_deleted() {
		return is_deleted;
	}
}

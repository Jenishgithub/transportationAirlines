package com.modellingpackages;

public class Facilities {
	private String id;
	private String airlineclass_id;
	private String added_date;
	private String is_deleted;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAirlineclass_id(String airlineclass_id) {
		this.airlineclass_id = airlineclass_id;
	}

	public String getAirlineclass_id() {
		return airlineclass_id;
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

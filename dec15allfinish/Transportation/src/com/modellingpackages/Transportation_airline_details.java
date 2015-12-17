package com.modellingpackages;

public class Transportation_airline_details {
	private String id;
	private String transportation_id;
	private String airline_id;
	private String airline_class_id;
	private String added_date;
	private String is_deleted;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setTransportation_id(String transportation_id) {
		this.transportation_id = transportation_id;
	}

	public String getTransportation_id() {
		return transportation_id;
	}

	public void setAirline_id(String airline_id) {
		this.airline_id = airline_id;
	}

	public String getAirline_id() {
		return airline_id;
	}

	public void setAirline_class_id(String airline_class_id) {
		this.airline_class_id = airline_class_id;
	}

	public String getAirline_class_id() {
		return airline_class_id;
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

package com.modellingpackages;
public class Node {
	private String id;
	private String name;
	private String lat;
	private String lon;
	private String specialities;
	private String district_id;
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

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLat() {
		return lat;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLon() {
		return lon;
	}

	public void setSpecialities(String specialities) {
		this.specialities = specialities;
	}

	public String getSpecialities() {
		return specialities;
	}

	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}

	public String getDistrict_id() {
		return district_id;
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

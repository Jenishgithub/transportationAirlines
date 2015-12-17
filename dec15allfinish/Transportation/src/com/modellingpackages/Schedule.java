package com.modellingpackages;
public class Schedule {
	private String id;
	private String transportation_id;
	private String schedule_type;
	private String sun;
	private String mon;
	private String tues;
	private String wednes;
	private String thurs;
	private String fri;
	private String satur;
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

	public void setSchedule_type(String schedule_type) {
		this.schedule_type = schedule_type;
	}

	public String getSchedule_type() {
		return schedule_type;
	}

	public void setSun(String sun) {
		this.sun = sun;
	}

	public String getSun() {
		return sun;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public String getMon() {
		return mon;
	}

	public void setTues(String tues) {
		this.tues = tues;
	}

	public String getTues() {
		return tues;
	}

	public void setWednes(String wednes) {
		this.wednes = wednes;
	}

	public String getWednes() {
		return wednes;
	}

	public void setThurs(String thurs) {
		this.thurs = thurs;
	}

	public String getThurs() {
		return thurs;
	}

	public void setFri(String fri) {
		this.fri = fri;
	}

	public String getFri() {
		return fri;
	}

	public void setSatur(String satur) {
		this.satur = satur;
	}

	public String getSatur() {
		return satur;
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

package com.modellingpackages;

public class Pricing {
	private String id;
	private String airlineclass_id;
	private String sourceId;
	private String destId;
	private String rate;
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

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	public String getDestId() {
		return destId;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getRate() {
		return rate;
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

package com.modellingpackages;
public class Transportation_service {
	private String id;
	private String transportation_id;
	private String service_id;
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

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getService_id() {
		return service_id;
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

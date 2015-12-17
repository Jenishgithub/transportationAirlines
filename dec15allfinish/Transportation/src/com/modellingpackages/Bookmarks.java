package com.modellingpackages;

public class Bookmarks {
	private String id;
	private String route_node_id;
	private String added_date;
	private String is_deleted;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setRoute_node_id(String route_node_id) {
		this.route_node_id = route_node_id;
	}

	public String getRoute_node_id() {
		return route_node_id;
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

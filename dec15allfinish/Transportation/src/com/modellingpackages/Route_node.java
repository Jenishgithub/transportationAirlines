package com.modellingpackages;
public class Route_node {

	private String id;
	private String route_id;
	private String node_id;
	private String route_count;
	private String added_date;
	private String is_deleted;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}

	public String getRoute_id() {
		return route_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setRoute_count(String route_count) {
		this.route_count = route_count;
	}

	public String getRoute_count() {
		return route_count;
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

package com.modellingpackages;

import java.util.List;

//import com.modellingpackages.Airlines;
//import com.modellingpackages.Airlines_classes;
import com.modellingpackages.Bookmarks;
import com.modellingpackages.Category;
import com.modellingpackages.Company;
import com.modellingpackages.District;

import com.modellingpackages.Language;
import com.modellingpackages.Node;

import com.modellingpackages.Route;
import com.modellingpackages.Route_node;
import com.modellingpackages.Schedule;
import com.modellingpackages.Service;
import com.modellingpackages.Transportation;
import com.modellingpackages.Transportation_details;
import com.modellingpackages.Transportation_service;
import com.modellingpackages.Type;

public class TransportationInfoList {
	private List<Category> category;
	private List<Company> company;
	private List<District> district;
	private List<Node> node;
	private List<Route_node> route_node;
	private List<Route> route;
	private List<Schedule> schedule;
	private List<Transportation_details> transportation_details;
	private List<Transportation> transportation;
	private List<Type> type;
	private List<Bookmarks> bookmarks;
	private List<Language> language;
	private List<Service> service;
	private List<Transportation_service> transportation_service;

	// for airlines
	private List<Airlines> airlines;
	private List<Airlines_classes> airlines_classes;
	private List<Facilities> facilities;
	// private List<Pricing> pricing;
	private List<Transportation_airline_details> transportation_airline_details;

	public void setCategory(List<Category> category) {
		this.category = category;
	}

	public List<Category> getCategory() {
		return this.category;
	}

	public void setCompany(List<Company> company) {
		this.company = company;
	}

	public List<Company> getCompany() {
		return this.company;
	}

	public void setDistrict(List<District> district) {
		this.district = district;
	}

	public List<District> getDistrict() {
		return this.district;
	}

	public void setNode(List<Node> node) {
		this.node = node;
	}

	public List<Node> getNode() {
		return this.node;
	}

	public void setRoute_node(List<Route_node> route_node) {
		this.route_node = route_node;
	}

	public List<Route_node> getRoute_node() {
		return this.route_node;
	}

	public void setRoute(List<Route> route) {
		this.route = route;
	}

	public List<Route> getRoute() {
		return this.route;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}

	public List<Schedule> getSchedule() {
		return this.schedule;
	}

	public void setTransportation_details(
			List<Transportation_details> transportation_details) {
		this.transportation_details = transportation_details;
	}

	public List<Transportation_details> getTransportation_details() {
		return this.transportation_details;
	}

	public void setTransportation(List<Transportation> transportation) {
		this.transportation = transportation;
	}

	public List<Transportation> getTransportation() {
		return this.transportation;
	}

	public void setType(List<Type> type) {
		this.type = type;
	}

	public List<Type> getType() {
		return this.type;
	}

	public void setBookmarks(List<Bookmarks> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public List<Bookmarks> getBookmarks() {
		return this.bookmarks;
	}

	public void setLanguage(List<Language> language) {
		this.language = language;
	}

	public List<Language> getLanguage() {
		return this.language;
	}

	public void setService(List<Service> service) {
		this.service = service;
	}

	public List<Service> getService() {
		return this.service;
	}

	public void setTransportation_service(
			List<Transportation_service> transportation_service) {
		this.transportation_service = transportation_service;
	}

	public List<Transportation_service> getTransportation_service() {
		return this.transportation_service;
	}

	// for airlines

	public void setAirlines(List<Airlines> airlines) {
		this.airlines = airlines;
	}

	public List<Airlines> getAirlines() {
		return this.airlines;
	}

	public void setAirlines_classes(List<Airlines_classes> airlines_classes) {
		this.airlines_classes = airlines_classes;
	}

	public List<Airlines_classes> getAirlines_classes() {
		return this.airlines_classes;
	}

	// public void setFacilities(List<Facilities> facilities) {
	// this.facilities = facilities;
	// }
	//
	// public List<Facilities> getFacilities() {
	// return this.facilities;
	// }

	// public void setPricing(List<Pricing> pricing) {
	// this.pricing = pricing;
	// }

	// public List<Pricing> getPricing() {
	// return this.pricing;
	// }

	public void setTransportation_airline_details(
			List<Transportation_airline_details> transportation_airline_details) {
		this.transportation_airline_details = transportation_airline_details;
	}

	public List<Transportation_airline_details> getTransportation_airline_details() {
		return this.transportation_airline_details;
	}

}

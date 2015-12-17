package com.crossovernepal.transportation;

public class node {
	public static String name;
	public static String routeId;
	public static String routeName;
	public static node node_parent;

	public node(String nodeName, String route_Id, String route_Name, node parent) {
		name = nodeName;
		routeId = route_Id;
		routeName = route_Name;
		node_parent = parent;

	}
}

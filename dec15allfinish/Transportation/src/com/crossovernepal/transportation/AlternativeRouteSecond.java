package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class AlternativeRouteSecond extends Activity {
	MySQLiteHelper dbHelper;
	String actual_from, actual_to;
	Double sourceLat, sourceLong;
	Double destLat, destLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alternativeroute);
		dbHelper = new MySQLiteHelper(getApplicationContext());
		actual_from = shared.fromString;
		actual_to = shared.toString;

		String from_cordinate = "select " + tablehelper.NodeColumns.LAT + ", "
				+ tablehelper.NodeColumns.LNG + " from "
				+ tablehelper.NodeColumns.TABLENAME + " where "
				+ tablehelper.NodeColumns.NAME + " = '" + actual_from + "'";
		String to_cordinate = "select " + tablehelper.NodeColumns.LAT + ", "
				+ tablehelper.NodeColumns.LNG + " from "
				+ tablehelper.NodeColumns.TABLENAME + " where "
				+ tablehelper.NodeColumns.NAME + " = '" + actual_to + "'";
		Cursor cur_latlong = dbHelper.getData(from_cordinate);

		cur_latlong.moveToFirst();
		sourceLat = Double.parseDouble(cur_latlong.getString(cur_latlong
				.getColumnIndex(tablehelper.NodeColumns.LAT)));
		sourceLong = Double.parseDouble(cur_latlong.getString(cur_latlong
				.getColumnIndex(tablehelper.NodeColumns.LNG)));

		Log.d("source lat long ", "lat and longs source are " + sourceLat + " "
				+ sourceLong);

		LatLng my_latlong = new LatLng(sourceLat, sourceLong);
		LatLng frnd_latlong = new LatLng(27.715721, 85.283561);
		String dist = getDistance(my_latlong, frnd_latlong);
		Log.d("distance", "distance between two locaitons:" + dist);

		cur_latlong.close();
		cur_latlong = dbHelper.getData(to_cordinate);
		cur_latlong.moveToFirst();
		destLat = Double.parseDouble(cur_latlong.getString(cur_latlong
				.getColumnIndex(tablehelper.NodeColumns.LAT)));
		destLong = Double.parseDouble(cur_latlong.getString(cur_latlong
				.getColumnIndex(tablehelper.NodeColumns.LNG)));

		Log.d("dest lat long", "lat and longs dest are " + destLat + " "
				+ destLong);
		cur_latlong.close();

		// get the route id of the destination also
		String query = "select " + tablehelper.RouteColumns.ID + ", "
				+ tablehelper.RouteColumns.FULL_ROUTE + " from "
				+ tablehelper.RouteColumns.TABLENAME + " where "
				+ tablehelper.RouteColumns.FULL_ROUTE + " not like '"
				+ actual_to + "%' and " + tablehelper.RouteColumns.FULL_ROUTE
				+ " like '%" + actual_to + "%'";
		cur_latlong = dbHelper.getData(query);
		List<String> list_routeId_to = new ArrayList<>();
		if (cur_latlong.getCount() != 0) {
			cur_latlong.moveToFirst();
			while (!cur_latlong.isAfterLast()) {
				list_routeId_to.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.RouteColumns.ID)));
				cur_latlong.moveToNext();
			}
			cur_latlong.close();
			Log.d("crossover", "list of route ids of " + actual_to + " are:"
					+ list_routeId_to);
		}

		// get the route id of the source also

		query = "SELECT " + tablehelper.RouteColumns.ID + ", "
				+ tablehelper.RouteColumns.FULL_ROUTE + " FROM "
				+ tablehelper.RouteColumns.TABLENAME + " WHERE "
				+ tablehelper.RouteColumns.FULL_ROUTE + " NOT LIKE '%"
				+ actual_from + ",' AND " + tablehelper.RouteColumns.FULL_ROUTE
				+ " LIKE '%" + actual_from + "%'";
		cur_latlong = dbHelper.getData(query);
		List<String> list_routeId_from = new ArrayList<>();
		if (cur_latlong.getCount() != 0) {
			cur_latlong.moveToFirst();
			while (!cur_latlong.isAfterLast()) {
				list_routeId_from.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.RouteColumns.ID)));
				cur_latlong.moveToNext();
			}
			cur_latlong.close();
			Log.d("crossover", "list of route ids of " + actual_to + " are:"
					+ list_routeId_from);
		}

		// get the list of nodes belong to the routes of "from"
		List<String> nodes_from = getNodes(list_routeId_from).list_id;
		List<String> nodes_to = getNodes(list_routeId_to).list_id;

		// compare two nodes from two different lists
		List<String> same_nodes_id = new ArrayList<>();
		for (String node : nodes_to) {
			if (nodes_from.contains(node))
				same_nodes_id.add(node);
		}

		// filter the nodes start----------------
		Iterator iterator = same_nodes_id.iterator();
		StringBuilder build = new StringBuilder();
		build.append("select " + tablehelper.NodeColumns.NAME + " from "
				+ tablehelper.NodeColumns.TABLENAME + " where "
				+ tablehelper.NodeColumns.ID + " in (");
		while (iterator.hasNext()) {
			build.append(iterator.next() + ", ");
		}
		build.append("0)");
		query = build.toString();
		cur_latlong = dbHelper.getData(query);
		List<String> unfilteredNodes = new ArrayList<>();
		if (cur_latlong.getCount() != 0) {
			cur_latlong.moveToFirst();
			while (!cur_latlong.isAfterLast()) {
				unfilteredNodes.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.NodeColumns.NAME)));
				cur_latlong.moveToNext();
			}
			cur_latlong.close();
			Log.d("common nodes", "nodes: " + unfilteredNodes);
		}

		List<String> filteredNodes = new ArrayList<>();

		for (String string : unfilteredNodes) {
			int correct_route_count = 0;
			correct_route_count = checkIfNodeIsValid(actual_from, string);
			Log.d("crossover", "value of correct route count:"
					+ correct_route_count);
			if (correct_route_count > 0) {
				// check intermediate node with dest
				correct_route_count = checkIfNodeIsValid(string, actual_to);
				if (correct_route_count > 0) {
					filteredNodes.add(string);
				}
			} else {
				// separate node

			}
		}
		Log.d("crossover", "the filtered out nodes are:" + filteredNodes);
		build = new StringBuilder();
		build.append("select " + tablehelper.NodeColumns.ID + " from "
				+ tablehelper.NodeColumns.TABLENAME + " where "
				+ tablehelper.NodeColumns.NAME + " in (");
		Iterator itFilter = filteredNodes.iterator();
		while (itFilter.hasNext()) {
			build.append("'" + itFilter.next() + "', ");
		}
		build.append("'')");
		query = build.toString();
		cur_latlong = dbHelper.getData(query);
		cur_latlong.moveToFirst();
		List<String> same_nodes_id_filtered = new ArrayList<>();
		while (!cur_latlong.isAfterLast()) {
			same_nodes_id_filtered.add(cur_latlong.getString(cur_latlong
					.getColumnIndex(tablehelper.NodeColumns.ID)));
			cur_latlong.moveToNext();
		}
		cur_latlong.close();
		Log.d("crossover", "the id of same nodes filtered:"
				+ same_nodes_id_filtered);
		// filter nodes end-------------------

		Log.d("crossover", "the id of same nodes:" + same_nodes_id);
		StringBuilder nodeBuilder = new StringBuilder();
		nodeBuilder.append("select " + tablehelper.NodeColumns.NAME + ", "
				+ tablehelper.NodeColumns.ID + ", "
				+ tablehelper.NodeColumns.LAT + ", "
				+ tablehelper.NodeColumns.LNG + " from "
				+ tablehelper.NodeColumns.TABLENAME + " where "
				+ tablehelper.NodeColumns.ID + " in (");
		Iterator<String> it = same_nodes_id.iterator();
		while (it.hasNext()) {
			nodeBuilder.append(it.next() + ", ");
		}
		nodeBuilder.append("0)");
		query = nodeBuilder.toString();
		cur_latlong = dbHelper.getData(query);

		List<String> same_nodes_names;
		Map<String, List<String>> nodelatlongMap = new HashMap<String, List<String>>();
		if (cur_latlong.getCount() != 0) {
			cur_latlong.moveToFirst();
			while (!cur_latlong.isAfterLast()) {
				same_nodes_names = new ArrayList<>();
				same_nodes_names.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.NodeColumns.NAME)));
				same_nodes_names.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.NodeColumns.LAT)));
				same_nodes_names.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.NodeColumns.LNG)));
				nodelatlongMap.put(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.NodeColumns.ID)),
						same_nodes_names);
				cur_latlong.moveToNext();
			}
			Log.d("crossover", "key value pairs of nodes and lat lng are: "
					+ nodelatlongMap);
		}

		// calculate the distance between source and all the intermediate nodes
		Map<String, Double> nodeId_distance_map = new HashMap<String, Double>();

		for (Map.Entry<String, List<String>> tempEntry : nodelatlongMap
				.entrySet()) {
			String node_id = tempEntry.getKey();
			List<String> latlong = tempEntry.getValue();
			Double destLat = Double.parseDouble(latlong.get(1));
			Double destLong = Double.parseDouble(latlong.get(2));

			Double distance = calculateDistance(destLat, destLong);
			nodeId_distance_map.put(node_id, distance);

		}
		Log.d("crossover", "the node id and its distance is:"
				+ nodeId_distance_map);

		// search for node which has the shortest distance from source
		List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(
				nodeId_distance_map.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());

			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Double> sortedDistance = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list) {
			sortedDistance.put(entry.getKey(), entry.getValue());
		}
		Log.d("after sorting..", "the node id and its distance is:"
				+ sortedDistance);
	}

	private String getDistance(LatLng my_latlong, LatLng frnd_latlong) {
		Location l1 = new Location("One");
		l1.setLatitude(my_latlong.latitude);
		l1.setLongitude(my_latlong.longitude);

		Location l2 = new Location("Two");
		l2.setLatitude(frnd_latlong.latitude);
		l2.setLongitude(frnd_latlong.longitude);

		float distance = l1.distanceTo(l2);
		String dist = distance + " M";

		if (distance > 1000.0f) {
			distance = distance / 1000.0f;
			dist = distance + " KM";
		}
		return dist;
	}

	private int checkIfNodeIsValid(String actual_from, String string) {
		// TODO Auto-generated method stub
		String query = "select " + tablehelper.RouteColumns.ID + ", "
				+ tablehelper.RouteColumns.FULL_ROUTE + " from "
				+ tablehelper.RouteColumns.TABLENAME + " where "
				+ tablehelper.RouteColumns.FULL_ROUTE + " like '%"
				+ actual_from + "%' and " + tablehelper.RouteColumns.FULL_ROUTE
				+ " like '%" + string + "%'";
		List<Integer> route_Id = new ArrayList<>();
		Cursor cur_latlong = dbHelper.getData(query);
		if (cur_latlong.getCount() != 0) {
			cur_latlong.moveToFirst();
			while (!cur_latlong.isAfterLast()) {
				route_Id.add(Integer.parseInt(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.RouteColumns.ID))));
				cur_latlong.moveToNext();
			}
			cur_latlong.close();
			Log.d("routeID", "routeID: " + route_Id);
		}
		int i = 0;
		int correct_route_count = 0;
		while (i < route_Id.size()) {
			String query1 = "select " + tablehelper.Route_NodeColumns.ID
					+ " from " + tablehelper.Route_NodeColumns.TABLENAME
					+ " where " + tablehelper.Route_NodeColumns.ROUTE_ID
					+ " = " + route_Id.get(i) + " and "
					+ tablehelper.Route_NodeColumns.NODE_ID + " = (select "
					+ tablehelper.NodeColumns.ID + " from "
					+ tablehelper.NodeColumns.TABLENAME + " where "
					+ tablehelper.NodeColumns.NAME + " = '" + actual_from
					+ "')";
			String query2 = "select " + tablehelper.Route_NodeColumns.ID
					+ " from " + tablehelper.Route_NodeColumns.TABLENAME
					+ " where " + tablehelper.Route_NodeColumns.ROUTE_ID
					+ " = " + route_Id.get(i) + " and "
					+ tablehelper.Route_NodeColumns.NODE_ID + " = (select "
					+ tablehelper.NodeColumns.ID + " from "
					+ tablehelper.NodeColumns.TABLENAME + " where "
					+ tablehelper.NodeColumns.NAME + " = '" + string + "')";
			Cursor cur1 = dbHelper.getData(query1);
			Cursor cur2 = dbHelper.getData(query2);
			int route_nodeID1 = 0, route_nodeID2 = 0;

			if (cur1.getCount() != 0) {
				cur1.moveToFirst();
				route_nodeID1 = Integer.parseInt(cur1.getString(cur1
						.getColumnIndex(tablehelper.Route_NodeColumns.ID)));
				Log.d("crossover", "route id: " + route_nodeID1);
			}
			if (cur2.getCount() != 0) {
				cur2.moveToFirst();
				route_nodeID2 = Integer.parseInt(cur2.getString(cur2
						.getColumnIndex(tablehelper.Route_NodeColumns.ID)));
				Log.d("crossover", "route id: " + route_nodeID2);
			}
			if (route_nodeID1 < route_nodeID2)
				correct_route_count++;
			i++;
		}
		return correct_route_count;
	}

	private Double calculateDistance(Double destLat, Double destLong) {
		// TODO Auto-generated method stub

		// source
		Double x1 = sourceLat;
		Double y1 = sourceLong;

		// dest
		Double x2 = destLat;
		Double y2 = destLong;

		double xdist = Math.pow((x2 - x1), 2);
		double ydist = Math.pow((y2 - y1), 2);

		double sum = xdist + ydist;
		double distance = Math.sqrt(sum);
		return distance;
	}

	private NodeNamenID getNodes(List<String> list_routeId) {
		// TODO Auto-generated method stub
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT DISTINCT " + tablehelper.NodeColumns.ID
				+ ", " + tablehelper.NodeColumns.NAME + " FROM "
				+ tablehelper.Route_NodeColumns.TABLENAME + " JOIN "
				+ tablehelper.NodeColumns.TABLENAME + " ON "
				+ tablehelper.NodeColumns.ID + " = "
				+ tablehelper.Route_NodeColumns.NODE_ID + " JOIN "
				+ tablehelper.RouteColumns.TABLENAME + " ON "
				+ tablehelper.RouteColumns.ID + " = "
				+ tablehelper.Route_NodeColumns.ROUTE_ID + " WHERE "
				+ tablehelper.Route_NodeColumns.ROUTE_ID + " IN (");
		Iterator<String> it = list_routeId.iterator();
		while (it.hasNext()) {
			queryBuilder.append(it.next() + ", ");
		}
		queryBuilder.append("0) order by " + tablehelper.NodeColumns.ID);
		String query = queryBuilder.toString();
		Cursor cur_latlong = dbHelper.getData(query);
		List<String> nodesID = new ArrayList<>();
		List<String> nodesNAME = new ArrayList<>();
		if (cur_latlong.getCount() != 0) {
			cur_latlong.moveToFirst();
			while (!cur_latlong.isAfterLast()) {
				nodesID.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.NodeColumns.ID)));
				nodesNAME.add(cur_latlong.getString(cur_latlong
						.getColumnIndex(tablehelper.NodeColumns.NAME)));
				cur_latlong.moveToNext();
			}
			Log.d("crossover", "the list of nodes belong to route of source: "
					+ nodesID + nodesNAME);

		}
		return new NodeNamenID(nodesID, nodesNAME);
	}

	public class NodeNamenID {
		List<String> list_id = new ArrayList<>();
		List<String> list_name = new ArrayList<>();

		public NodeNamenID(List<String> list_id, List<String> list_name) {
			this.list_id = list_id;
			this.list_name = list_name;

		}

	}
}

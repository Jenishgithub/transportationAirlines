package com.crossovernepal.transportation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AlternativeRouteThirdDijkstra extends Activity {
	Map<String, Map<String, Double>> distmap = new HashMap<>();
	int MAX;
	public static double[][] actual_matrix;
	List<String> all_nodes = new ArrayList<>();
	String actual_from, actual_to;
	// TextView tvPath;
	MySQLiteHelper dbHelper;
	String[] values = null;
	List<List<String>> alternative_routes = new ArrayList<List<String>>();
	ProgressBar pbAlternativeRoute;
	RelativeLayout rlNoAlternativeRoute;
	String main_source;
	String main_destination;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alternativeroutethirddijkstra);
		distmap = SplashActivity.saveddistancecost;
		pbAlternativeRoute = (ProgressBar) findViewById(R.id.pbAlternativeRoute);
		rlNoAlternativeRoute = (RelativeLayout) findViewById(R.id.rlNoAlternativeRoute);

		actual_from = shared.fromString;
		actual_to = shared.toString;
		dbHelper = new MySQLiteHelper(getApplicationContext());

		// SharedPreferences sharedPref = getSharedPreferences(
		// SplashActivity.MyPREFERENCES1, MODE_PRIVATE);
		// String node_list = sharedPref.getString("node_list", null);
		String node_list = new SessionManager(
				AlternativeRouteThirdDijkstra.this).getNodeNames();
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		Type type = new TypeToken<List<String>>() {
		}.getType();
		all_nodes = gson.fromJson(node_list, type);

		MAX = all_nodes.size();
		actual_matrix = new double[MAX][MAX];
		new GetAlternativeRoute().execute();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	public class GetAlternativeRoute extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for (int i = 0; i < MAX; i++) {
				for (int j = 0; j < MAX; j++) {
					actual_matrix[i][j] = 99999999.00;
				}

			}

			Set<Entry<String, Map<String, Double>>> entrySet = distmap
					.entrySet();
			Iterator<Entry<String, Map<String, Double>>> it = entrySet
					.iterator();

			while (it.hasNext()) {
				Map.Entry<String, Map<String, Double>> map = it.next();
				String firstnode = map.getKey();

				Map<String, Double> dist_node = map.getValue();
				Set<String> enSet = dist_node.keySet();
				for (String string : enSet) {
					Log.d("crossover", "first intermediate node:" + string);
					try {
						actual_matrix[all_nodes.indexOf(firstnode)][all_nodes
								.indexOf(string)] = dist_node.get(string);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}
			}
			Log.d("crossover", "the values of matrix:" + actual_matrix);

			int from = all_nodes.indexOf(actual_from);
			int to = all_nodes.indexOf(actual_to);
			if ((from != -1) && (to != -1))
				getTheShortestPath(from, to);
			else {
				// do here something for no path found
			}
			// tvPath.setText("Not Found!!!");
			values = getRouteValues(alternative_routes);

			Log.d("crossover", "value of values:" + values);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			shared.fromString = actual_from;
			shared.toString = actual_to;
			if (alternative_routes.size() >= 2) {

				String alternative_source = alternative_routes.get(0).get(0);
				String alternative_destination = alternative_routes.get(
						alternative_routes.size() - 1).get(0);

				String[] alternative_source_trimmed = alternative_source
						.split("-");
				String[] alternative_destination_trimmed = alternative_destination
						.split("-");

				main_source = alternative_source_trimmed[0].trim();
				main_destination = alternative_destination_trimmed[1].trim();

				if (main_source.equals(shared.fromString)
						&& main_destination.equals(shared.toString)) {
					Intent intent = new Intent(getApplicationContext(),
							AlternativeRouteList.class);

					intent.putExtra("alternative_routes", values);

					startActivity(intent);

				} else {
					pbAlternativeRoute.setVisibility(View.GONE);
					rlNoAlternativeRoute.setVisibility(View.VISIBLE);
				}
			} else {
				pbAlternativeRoute.setVisibility(View.GONE);
				rlNoAlternativeRoute.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pbAlternativeRoute.setVisibility(View.VISIBLE);
			rlNoAlternativeRoute.setVisibility(View.GONE);
		}

	}

	private void getTheShortestPath(int from, int to) {
		// TODO Auto-generated method stub
		Double[] distance = new Double[MAX];
		int[] visited = new int[MAX];
		int[] preD = new int[MAX];

		int nextNode = 0;
		LinkedHashMap<Integer, Double> node_cost = new LinkedHashMap<Integer, Double>();

		System.out.println("Enter the starting node:");
		int startingNode = from;
		System.out.println("Enter the destination node:");
		int endingNode = to;

		for (int i = 0; i < MAX; i++) {

			visited[i] = 0;
			preD[i] = 999;
			distance[i] = 99999999.00;
			node_cost.put(i, 99999999.00);

			if (i == startingNode) {
				node_cost.put(i, 0.00);
				distance[i] = 0.00;
			}

		}

		LinkedHashMap<Integer, Double> Q = new LinkedHashMap<>();
		Q = node_cost;
		// put starting node in the queue
		// Q.put(startingNode, 0);
		preD[startingNode] = startingNode;
		while (!Q.isEmpty()) {
			LinkedHashMap<Integer, Double> sortedQ = sortByCoparator(Q, true);
			Integer node = (new ArrayList<Integer>(sortedQ.keySet())).get(0);
			Double cost = (new ArrayList<Double>(sortedQ.values())).get(0);
			// sortedQ.remove(node);
			// Q.remove(node);

			for (int i = 0; i < MAX; i++) {
				if (i != startingNode) {
					if (actual_matrix[node][i] != 99999999.00) {
						Double min = cost + actual_matrix[node][i];
						// int prevcost = Q.get(i);
						if (min < distance[i]) {
							// node_cost.put(i, min);
							Q.put(i, min);
							preD[i] = node;
							distance[i] = min;
						}

					}
				}
			}
			Q.remove(node);
		}
		for (int i = 0; i < distance.length; i++) {
			System.out.println(distance[i]);

		}

		// display distance and path
		Double minDistance = distance[endingNode];
		System.out.print("Minimum dist from startingnode " + startingNode
				+ " to endingNode " + endingNode + " is : "
				+ distance[endingNode]);
		Log.d("crossover", "the minimum distance is:" + minDistance);

		System.out.println();

		int j;
		StringBuilder builder = new StringBuilder();
		builder.append("Path from startingPoint " + all_nodes.get(startingNode)
				+ " to endingNode " + all_nodes.get(endingNode) + " is: \n"
				+ all_nodes.get(endingNode));

		List<String> path_route = new ArrayList<>();
		path_route.add(all_nodes.get(endingNode));

		j = endingNode;
		do {
			j = preD[j];
			if (j != 999) {
				builder.append(" <- " + all_nodes.get(j));
				path_route.add(all_nodes.get(j));
			}
			if (j == 999)
				break;
		} while (j != startingNode);
		String shortstpath = builder.toString();
		Log.d("crossover", "shortest path is :" + shortstpath);
		if (j == 999) {
			// tvPath.setText("Not found!!!");
			// do here something for no path found
		} else
		// tvPath.setText(shortstpath);
		{
			List<String> first_route;

			int i = 1;
			if (path_route.size() > 2) {
				String src = path_route.get(path_route.size() - 1);
				String dest;
				do {
					dest = path_route.get(i);
					Boolean check = checkForRoutes(src, dest);
					if (check) {
						// save route
						first_route = new ArrayList<>();
						first_route.add(src + " - " + dest);
						alternative_routes.add(first_route);
						// int t = 1;
						src = path_route.get(i);
						i = -1;
						// dest = path_route.get(t);
					}

					i++;

					// } while (i <= path_route.size() - 2);
				} while (i <= (path_route.indexOf(src) - 1));
				Log.d("crossover", "final path:" + alternative_routes);
			}

		}

		System.out.println(path_route);
	}

	private String[] getRouteValues(List<List<String>> alternative_routes) {
		// TODO Auto-generated method stub
		String[] values2 = null;
		int i = 0;
		List<String> valueList = new ArrayList<String>();
		while (i < alternative_routes.size()) {
			for (String string : alternative_routes.get(i)) {
				valueList.addAll(Arrays.asList(string.split("-")));
			}
			values2 = valueList.toArray(new String[valueList.size()]);
			i++;
		}
		return values2;
	}

	private boolean checkForRoutes(String from, String to) {
		// TODO Auto-generated method stub
		String query = "select r1.*, r2.* from route_node r1 join route_node r2 on r1.route_id = r2.route_id join "
				+ tablehelper.TransportationColumns.TABLENAME
				+ " on r1.route_id = "
				+ tablehelper.TransportationColumns.ROUTE_ID
				+ " where r1.node_id = (select "
				+ tablehelper.NodeColumns.ID
				+ " from "
				+ tablehelper.NodeColumns.TABLENAME
				+ " where "
				+ tablehelper.NodeColumns.NAME
				+ " = '"
				+ from
				+ "') and r2.node_id = (select "
				+ tablehelper.NodeColumns.ID
				+ " from "
				+ tablehelper.NodeColumns.TABLENAME
				+ " where "
				+ tablehelper.NodeColumns.NAME
				+ " = '"
				+ to
				+ "') and r1.id<r2.id";
		Cursor cur = dbHelper.getData(query);
		if (cur.getCount() > 0) {
			return true;
		} else
			return false;
	}

	private static LinkedHashMap<Integer, Double> sortByCoparator(
			LinkedHashMap<Integer, Double> unsortMap, final boolean order) {
		// TODO Auto-generated method stub

		List<Entry<Integer, Double>> list = new LinkedList<Entry<Integer, Double>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Integer, Double>>() {
			public int compare(Entry<Integer, Double> o1,
					Entry<Integer, Double> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Entry<Integer, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}

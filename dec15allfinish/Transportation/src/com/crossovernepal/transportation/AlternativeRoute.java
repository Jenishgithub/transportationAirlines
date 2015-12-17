package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.crossovernepal.transportation.AlternativeRouteList.Route;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class AlternativeRoute extends Activity {
	MySQLiteHelper dbHelper;
	int number_of_nodes1;
	List<List<String>> alternative_routes = new ArrayList<List<String>>();
	List<Route> route = new ArrayList<Route>();
	List<String> first_route = null;
	List<String> second_route = null;
	List<String> third_route = null;
	List<String> fourth_route = null;
	List<String> fifth_route = null;
	List<String> all_nodes = new ArrayList<String>();
	String[] values = null;
	String[] valuescopy = null;
	ProgressBar pbAlternativeRoute;
	RelativeLayout rlNoAlternativeRoute;
	String main_source;
	String main_destination;
	String actual_from, actual_to;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alternativeroute);
		pbAlternativeRoute = (ProgressBar) findViewById(R.id.pbAlternativeRoute);
		rlNoAlternativeRoute = (RelativeLayout) findViewById(R.id.rlNoAlternativeRoute);
		dbHelper = new MySQLiteHelper(getApplicationContext());
		actual_from = shared.fromString;
		actual_to = shared.toString;
		String query = "select name from node;";
		Cursor cur = dbHelper.getData(query);

		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			all_nodes.add(cur.getString(cur.getColumnIndex("name")));
			cur.moveToNext();
		}

		number_of_nodes1 = cur.getCount();
		cur.close();
		new GetAlternativeRoute().execute();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		new HomeFragment().refresh();

		shared.fromString = "";
		shared.toString = "";
		shared.through1String = "";
		shared.fromPrice = "";
		shared.toPrice = "";
		shared.departureDate = "";
		shared.day = "";
		if (shared.through2String.length() > 0) {
			shared.through2String = "";
		}
		if (shared.through3String.length() > 0) {
			shared.through3String = "";
		}

		if (HomeFragment.throughPath2 != null) {
			HomeFragment.throughPath2.setText("");
			HomeFragment.throughPath2.setEnabled(true);
			HomeFragment.throughPath2.setFocusableInTouchMode(true);
			HomeFragment.throughPath2.setError(null);

		}
		if (HomeFragment.throughPath3 != null) {
			HomeFragment.throughPath3.setText("");
			HomeFragment.throughPath3.setEnabled(true);
			HomeFragment.throughPath3.setFocusableInTouchMode(true);
			HomeFragment.throughPath3.setError(null);
		}

		HomeActivity.mFrameLayout.setVisibility(View.GONE);
		finish();
	}

	public List<String> returnIndividualRoutes(List<List<String>> full_route) {
		List<String> routes_individuals_trimmed = new ArrayList<String>();
		// split of the roots

		int z = 0;
		String routes_individuals[] = null;
		while (z < full_route.size()) {
			routes_individuals = full_route.get(z).get(0).split(",");
			z++;
		}
		for (int k = 0; k < routes_individuals.length; k++)
			routes_individuals_trimmed.add(routes_individuals[k].trim());
		Log.d("crossvoer", "trimmed routes are :" + routes_individuals_trimmed);
		return routes_individuals_trimmed;
	}

	public String buildRouteSearchQuery(String from, String to,
			List<String> route_id_str) {
		StringBuilder qb = new StringBuilder();

		qb.append("select route.full_route, route.id from route "
				+ "where route.id = (select id from route where full_route like '%"
				+ from
				+ "%' and full_route like '%"
				+ to
				+ "%') and ((select route_node.id from route_node join node on node.id = route_node.node_id join route on route.id = route_node.route_id where node.id = (select id from node where name = '"
				+ from
				+ "') and route.id = (select id from route where (full_route like '%"
				+ from
				+ "%' and full_route like '%"
				+ to
				+ "%')))<(select route_node.id from route_node join node on node.id = route_node.node_id join route on route.id = route_node.route_id where node.id = (select id from node where name = '"
				+ to
				+ "') and route.id = (select id from route where (full_route like '%"
				+ from + "%' and full_route like '%" + to + "%'))))");
		Iterator<String> it = route_id_str.iterator();
		while (it.hasNext()) {
			qb.append(" and route.id!=");
			qb.append(Integer.parseInt(it.next()));

		}
		String her = qb.toString();
		Log.d("crossover", "check string:" + her);
		return qb.toString();
	}

	public String[] getIndividualNodes(List<String> route) {
		String routes = route.get(0);
		String split[] = routes.split("-");
		String first_node = split[0].trim();
		String second_node = split[1].trim();
		return new String[] { first_node, second_node };

	}

	public class GetAlternativeRoute extends AsyncTask<Void, Void, Void> {
		List<String> route_id_str = new ArrayList<String>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pbAlternativeRoute.setVisibility(View.VISIBLE);
			rlNoAlternativeRoute.setVisibility(View.GONE);
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
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pbAlternativeRoute.setVisibility(View.VISIBLE);
					rlNoAlternativeRoute.setVisibility(View.GONE);
				}
			});

			loop1: for (int i = 0; i < number_of_nodes1; i++) {

				String q1 = buildRouteSearchQuery(shared.fromString,
						all_nodes.get(i), route_id_str);
				Cursor c1 = dbHelper.getData(q1);
				List<List<String>> full_route;
				if (c1.getCount() != 0) {
					c1.moveToFirst();
					full_route = new ArrayList<List<String>>();
					List<String> full_route1 = null;
					while (!c1.isAfterLast()) {
						full_route1 = new ArrayList<String>();
						full_route1.add(c1.getString(c1
								.getColumnIndex("full_route")));
						route_id_str.add(c1.getString(c1.getColumnIndex("id")));
						c1.moveToNext();
					}
					full_route.add(full_route1);
					List<String> routes_individuals_trimmed = returnIndividualRoutes(full_route);

					Log.d("crossvoer", "trimmed routes are :"
							+ routes_individuals_trimmed);
					first_route = new ArrayList<String>();
					first_route.add(shared.fromString + " - "
							+ all_nodes.get(i));
					if (all_nodes.get(i).equals(shared.toString)) {
						if (second_route != null)
							second_route.remove(second_route.size() - 1);
						if (third_route != null)
							third_route.remove(third_route.size() - 1);
						if (fourth_route != null)
							fourth_route.remove(fourth_route.size() - 1);
						if (fifth_route != null)
							fifth_route.remove(fifth_route.size() - 1);
						break loop1;
					}

					for (int pos = routes_individuals_trimmed.indexOf(all_nodes
							.get(i)); pos < routes_individuals_trimmed.size(); pos++) {
						if (routes_individuals_trimmed.get(pos).equals(
								shared.toString)) {
							first_route = new ArrayList<String>();
							first_route.add(shared.fromString + " - "
									+ shared.toString);
							break loop1;
						}

					}

					for (int rootnode = routes_individuals_trimmed
							.indexOf(all_nodes.get(i)); rootnode < routes_individuals_trimmed
							.size(); rootnode++) {

						for (int j = 0; j < all_nodes.size(); j++) {
							if (!routes_individuals_trimmed.get(rootnode)
									.equals(shared.fromString)) {
								String q2 = buildRouteSearchQuery(
										routes_individuals_trimmed
												.get(rootnode),
										all_nodes.get(j), route_id_str);
								Cursor c2 = dbHelper.getData(q2);
								List<List<String>> second_full_route;// multiple
																		// routes
								if (c2.getCount() != 0) {
									first_route = new ArrayList<String>();
									first_route.add(shared.fromString
											+ " - "
											+ routes_individuals_trimmed
													.get(rootnode));
									c2.moveToFirst();
									second_full_route = new ArrayList<List<String>>();
									List<String> full_route2 = null;
									while (!c2.isAfterLast()) {
										full_route2 = new ArrayList<String>();
										full_route2.add(c2.getString(c2
												.getColumnIndex("full_route")));
										route_id_str.add(c2.getString(c2
												.getColumnIndex("id")));
										c2.moveToNext();
									}
									second_full_route.add(full_route2);
									List<String> routes_individuals_trimmed2 = returnIndividualRoutes(second_full_route);

									if (all_nodes.get(j)
											.equals(shared.toString)) {
										if (third_route != null)
											third_route.remove(third_route
													.size() - 1);
										if (fourth_route != null)
											fourth_route.remove(fourth_route
													.size() - 1);
										if (fifth_route != null)
											fifth_route.remove(fifth_route
													.size() - 1);
										break loop1;
									}

									for (int pos = routes_individuals_trimmed2
											.indexOf(all_nodes.get(j)); pos < routes_individuals_trimmed2
											.size(); pos++) {
										if (routes_individuals_trimmed2
												.get(pos).equals(
														shared.toString)) {
											second_route = new ArrayList<String>();
											second_route
													.add(routes_individuals_trimmed
															.get(rootnode)
															+ " - "
															+ shared.toString);
											break loop1;
										}

									}

									for (int rootnode1 = routes_individuals_trimmed2
											.indexOf(all_nodes.get(j)); rootnode1 < routes_individuals_trimmed2
											.size(); rootnode1++) {

										for (int k = 0; k < all_nodes.size(); k++) {
											if (!routes_individuals_trimmed2
													.get(rootnode1).equals(
															shared.fromString)) {
												String q3 = buildRouteSearchQuery(
														routes_individuals_trimmed2
																.get(rootnode1),
														all_nodes.get(k),
														route_id_str);
												Cursor c3 = dbHelper
														.getData(q3);
												List<List<String>> third_full_route;
												if (c3.getCount() != 0) {

													second_route = new ArrayList<String>();
													second_route
															.add(routes_individuals_trimmed
																	.get(rootnode)
																	+ " - "
																	+ routes_individuals_trimmed2
																			.get(rootnode1));
													c3.moveToFirst();
													third_full_route = new ArrayList<List<String>>();
													List<String> full_route3 = null;
													while (!c3.isAfterLast()) {
														full_route3 = new ArrayList<String>();
														full_route3
																.add(c3.getString(c3
																		.getColumnIndex("full_route")));
														route_id_str
																.add(c3.getString(c3
																		.getColumnIndex("id")));
														c3.moveToNext();

													}
													third_full_route
															.add(full_route3);
													List<String> routes_individual_trimmed3 = returnIndividualRoutes(third_full_route);

													if (all_nodes
															.get(k)
															.equals(shared.toString)) {
														if (fourth_route != null)
															fourth_route
																	.remove(fourth_route
																			.size() - 1);
														if (fifth_route != null)
															fifth_route
																	.remove(fifth_route
																			.size() - 1);
														break loop1;
													}

													for (int pos = routes_individual_trimmed3
															.indexOf(all_nodes
																	.get(k)); pos < routes_individual_trimmed3
															.size(); pos++) {
														if (routes_individual_trimmed3
																.get(pos)
																.equals(shared.toString)) {
															third_route = new ArrayList<String>();
															third_route
																	.add(routes_individuals_trimmed2
																			.get(rootnode1)
																			+ " - "
																			+ shared.toString);
															break loop1;
														}

													}

													for (int rootnode2 = routes_individual_trimmed3
															.indexOf(all_nodes
																	.get(k)); rootnode2 < routes_individual_trimmed3
															.size(); rootnode2++) {

														for (int l = 0; l < all_nodes
																.size(); l++) {
															if (!routes_individual_trimmed3
																	.get(rootnode2)
																	.equals(shared.fromString)) {
																String q4 = buildRouteSearchQuery(
																		routes_individual_trimmed3
																				.get(rootnode2),
																		all_nodes
																				.get(l),
																		route_id_str);
																Cursor c4 = dbHelper
																		.getData(q4);
																List<List<String>> fourth_full_route;
																if (c4.getCount() != 0) {
																	third_route = new ArrayList<String>();
																	third_route
																			.add(routes_individuals_trimmed2
																					.get(rootnode1)
																					+ " - "
																					+ routes_individual_trimmed3
																							.get(rootnode2));

																	c4.moveToFirst();
																	fourth_full_route = new ArrayList<List<String>>();
																	List<String> full_route4 = null;
																	while (!c4
																			.isAfterLast()) {
																		full_route4 = new ArrayList<String>();
																		full_route4
																				.add(c4.getString(c4
																						.getColumnIndex("full_route")));
																		route_id_str
																				.add(c4.getString(c4
																						.getColumnIndex("id")));
																		c4.moveToNext();
																	}
																	fourth_full_route
																			.add(full_route4);
																	List<String> routes_individual_trimmed4 = returnIndividualRoutes(fourth_full_route);

																	if (all_nodes
																			.get(l)
																			.equals(shared.toString)) {

																		if (fifth_route != null)
																			fifth_route
																					.remove(fifth_route
																							.size() - 1);
																		break loop1;
																	}

																	for (int pos = routes_individual_trimmed4
																			.indexOf(all_nodes
																					.get(l)); pos < routes_individual_trimmed4
																			.size(); pos++) {
																		if (routes_individual_trimmed4
																				.get(pos)
																				.equals(shared.toString)) {
																			fourth_route = new ArrayList<String>();
																			fourth_route
																					.add(routes_individual_trimmed3
																							.get(rootnode2)
																							+ " - "
																							+ shared.toString);
																			break loop1;
																		}

																	}
																	for (int rootnode3 = routes_individual_trimmed4
																			.indexOf(all_nodes
																					.get(l)); rootnode3 < routes_individual_trimmed4
																			.size(); rootnode3++) {

																		if (!routes_individual_trimmed4
																				.get(rootnode3)
																				.equals(shared.fromString)) {

																			String q5 = buildRouteSearchQuery(
																					routes_individual_trimmed4
																							.get(rootnode3),
																					shared.toString,
																					route_id_str);
																			Cursor c5 = dbHelper
																					.getData(q5);
																			List<List<String>> fifth_full_route;
																			if (c5.getCount() != 0) {
																				fourth_route = new ArrayList<String>();
																				fourth_route
																						.add(routes_individual_trimmed3
																								.get(rootnode2)
																								+ " - "
																								+ routes_individual_trimmed4
																										.get(rootnode3));
																				c5.moveToFirst();
																				fifth_full_route = new ArrayList<List<String>>();
																				List<String> full_route5 = null;
																				while (!c5
																						.isAfterLast()) {
																					full_route5 = new ArrayList<String>();
																					full_route5
																							.add(c5.getString(c5
																									.getColumnIndex("full_route")));
																					route_id_str
																							.add(c5.getString(c5
																									.getColumnIndex("id")));
																					c5.moveToNext();
																				}
																				fifth_full_route
																						.add(full_route5);
																				List<String> routes_individual_trimmed5 = returnIndividualRoutes(fifth_full_route);
																				fifth_route = new ArrayList<String>();
																				fifth_route
																						.add(routes_individual_trimmed4
																								.get(rootnode3)
																								+ " - "
																								+ shared.toString);

																				for (int pos = routes_individual_trimmed5
																						.indexOf(shared.toString); pos < routes_individual_trimmed5
																						.size(); pos++) {
																					if (routes_individual_trimmed5
																							.get(pos)
																							.equals(shared.toString))
																						break loop1;
																				}
																				break loop1;
																			}
																		}
																	}
																}
															}
														}
													}

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if ((first_route != null) && (!first_route.isEmpty())) {

				alternative_routes.add(first_route);
			}
			if ((second_route != null) && (!second_route.isEmpty())) {

				alternative_routes.add(second_route);
			}
			if ((third_route != null) && (!third_route.isEmpty())) {

				alternative_routes.add(third_route);
			}
			if ((fourth_route != null) && (!fourth_route.isEmpty())) {

				alternative_routes.add(fourth_route);
			}
			if ((fifth_route != null) && (!fifth_route.isEmpty())) {

				alternative_routes.add(fifth_route);
			}
			Log.d("crossover", "the value of alternative_routes are:"
					+ alternative_routes);
			values = getRouteValues(alternative_routes);
			String values2[] = values;
			Log.d("crossover", " the value of values is :" + values2);
			Log.d("crossover", " the value of route_id_str is :" + route_id_str);
			if (values != null)
				messageHandler.sendEmptyMessage(0);

			return null;
		}

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

	Handler messageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			checkAlternativeRoutes();
		}

	};

	private void checkAlternativeRoutes() {
		// TODO Auto-generated method stub
		AlternativeRouteList obJ = new AlternativeRouteList();
		for (int r = 0; r < values.length; r += 2) {
			route.add(obJ.new Route(values[r].trim(), values[r + 1].trim()));
		}

		if (shared.through2String.length() > 0) {
			shared.through2String = "";
		}
		if (shared.through3String.length() > 0) {
			shared.through3String = "";
		}
		shared.through1String = "";
		shared.fromPrice = "";
		shared.toPrice = "";
		shared.departureDate = "";
		shared.day = "";
		shared.vehicle = "all";

		int size = 0;
		List<List<String>> all_full_routes = new ArrayList<>();
		List<String> one_full_route = null;
		while (size < route.size()) {
			one_full_route = new ArrayList<>();
			shared.fromString = route.get(size).source;
			shared.toString = route.get(size).destination;
			String myquery = selectQuery("cost", true);
			Cursor cur = dbHelper.getData(myquery);
			cur.moveToFirst();
			List<String> rt_id = new ArrayList<>();
			while (!cur.isAfterLast()) {

				rt_id.add(cur.getString(cur.getColumnIndex("route_id")));
				cur.moveToNext();
			}
			Iterator<String> it = rt_id.iterator();
			StringBuilder find_route = new StringBuilder();

			find_route
					.append("select node.name from route_node join node on node.id = route_node.node_id join route on route.id = route_node.route_id where route_id in (");
			while (it.hasNext()) {
				find_route.append(it.next() + ", ");
			}
			find_route.append("0)");
			Cursor route_cursor = dbHelper.getData(find_route.toString());
			route_cursor.moveToFirst();
			while (!route_cursor.isAfterLast()) {
				one_full_route.add(route_cursor.getString(route_cursor
						.getColumnIndex("name")));
				route_cursor.moveToNext();
			}
			all_full_routes.add(one_full_route);
			size++;
		}
		Log.d("crossover", "here all full routes are:" + all_full_routes);

		for (int i = 0; i < alternative_routes.size(); i++) {

			second_for: for (int f = (all_full_routes.get(i).indexOf(route
					.get(i).source)) + 1; f < all_full_routes.get(i).size(); f++) {
				String str = all_full_routes.get(i).get(f);
				int num_route = route.size() - 1;
				while (num_route > (i + 1)) {
					if (str.equals(route.get(num_route).source)) {

						List<String> new_route = new ArrayList<>();
						new_route.add(route.get(i).source + " - " + str);
						alternative_routes.remove(i);
						alternative_routes.add(i, new_route);

						route.get(i).destination = str;

						int i1 = i + 1;
						while (!route.get(i1).source.equals(str)) {
							alternative_routes.remove(i1);
							route.remove(i1);
							all_full_routes.remove(i1);
							// i1++;
						}
						i--;
						Log.d("crossover", "alternative route:"
								+ alternative_routes);
						break second_for;
					}
					num_route--;
				}
			}
		}
		Log.d("crossover", "alternative route:" + alternative_routes);
		// here is now new optimized alternative_routes, route, all_full_routes
		values = getRouteValues(alternative_routes);
		Log.d("crossover", "value of values:" + values);
	}

	public String selectQuery(String order, boolean check) {
		String MULTIPLE_VALID_ROUTE = "";
		String single_route = "";
		String ourCase = "";
		String myquery = "select "
				+ tablehelper.TransportationColumns.CONTACT_PHONE + ", "
				+ tablehelper.TransportationColumns.DEPART_TIME + ", "
				+ tablehelper.TransportationColumns.ARRIVAL_TIME + ", "
				+ tablehelper.TransportationColumns.COST + ", "
				+ tablehelper.TransportationColumns.REMARKS + ", "
				+ tablehelper.TransportationColumns.TYPE_ID + ", "
				+ tablehelper.TransportationColumns.ID + ", "
				+ tablehelper.TransportationColumns.INTERVAL + ", "
				+ tablehelper.TransportationColumns.ROUTE_ID + ", "
				+ tablehelper.CompanyColumns.NAME + ", "
				+ tablehelper.RouteColumns.FULL_ROUTE + " from "
				+ tablehelper.TransportationColumns.TABLENAME + " JOIN "
				+ tablehelper.ScheduleColumns.TABLENAME + " ON "
				+ tablehelper.ScheduleColumns.TRANSPORTATION_ID + " = "
				+ tablehelper.TransportationColumns.ID + " JOIN "
				+ tablehelper.CompanyColumns.TABLENAME + " ON "
				+ tablehelper.CompanyColumns.ID + " = "
				+ tablehelper.TransportationColumns.COMPANY_ID + " JOIN "
				+ tablehelper.RouteColumns.TABLENAME + " ON "
				+ tablehelper.TransportationColumns.ROUTE_ID + " = "
				+ tablehelper.RouteColumns.ID
				+ " where route.full_route like '%" + shared.fromString
				+ "%' and route.full_route like '%" + shared.toString
				+ "%' and ('" + shared.fromString
				+ "' = (select name from node where name  = '"
				+ shared.fromString + "')) and ('" + shared.toString
				+ "' = (select name from node where name = '" + shared.toString
				+ "'))";

		myquery = myquery + " order by transportation." + order + " asc";

		Cursor cur = dbHelper.getData(myquery);
		cur.moveToFirst();
		int row_count = cur.getCount();

		if (cur.getCount() == 0) {
			ourCase = "NO_ROUTE_ATALL_JPT";

		}
		if (row_count == 1) {
			ourCase = "SINGLE_ROUTE";
			int r_id = Integer.parseInt(cur.getString(cur
					.getColumnIndex("route_id")));
			String quer1 = "select route_node.id from route_node where route_id = "
					+ r_id
					+ " and node_id = (select id from node where name = '"
					+ shared.fromString + "')";
			String quer2 = "select route_node.id from route_node where route_id = "
					+ r_id
					+ " and node_id = (select id from node where name = '"
					+ shared.toString + "')";
			Cursor cur1 = dbHelper.getData(quer1);
			Cursor cur2 = dbHelper.getData(quer2);
			cur1.moveToFirst();
			cur2.moveToFirst();
			List<Integer> r_n_id = new ArrayList<Integer>();
			r_n_id.add(Integer.parseInt(cur1.getString(cur1
					.getColumnIndex("id"))));
			r_n_id.add(Integer.parseInt(cur2.getString(cur2
					.getColumnIndex("id"))));
			if (r_n_id.get(0) < r_n_id.get(1)) {
				single_route = "select "
						+ tablehelper.TransportationColumns.CONTACT_PHONE
						+ ", " + tablehelper.TransportationColumns.DEPART_TIME
						+ ", " + tablehelper.TransportationColumns.ARRIVAL_TIME
						+ ", " + tablehelper.TransportationColumns.COST + ", "
						+ tablehelper.TransportationColumns.REMARKS + ", "
						+ tablehelper.TransportationColumns.TYPE_ID + ", "
						+ tablehelper.TransportationColumns.ID + ", "
						+ tablehelper.TransportationColumns.INTERVAL + ", "
						+ tablehelper.TransportationColumns.ROUTE_ID + ", "
						+ tablehelper.CompanyColumns.NAME + ", "
						+ tablehelper.RouteColumns.FULL_ROUTE + " from "
						+ tablehelper.TransportationColumns.TABLENAME
						+ " JOIN " + tablehelper.ScheduleColumns.TABLENAME
						+ " ON "
						+ tablehelper.ScheduleColumns.TRANSPORTATION_ID + " = "
						+ tablehelper.TransportationColumns.ID + " JOIN "
						+ tablehelper.CompanyColumns.TABLENAME + " ON "
						+ tablehelper.CompanyColumns.ID + " = "
						+ tablehelper.TransportationColumns.COMPANY_ID
						+ " JOIN " + tablehelper.RouteColumns.TABLENAME
						+ " ON " + tablehelper.TransportationColumns.ROUTE_ID
						+ " = " + tablehelper.RouteColumns.ID
						+ " where route.full_route like '%" + shared.fromString
						+ "%' and route.full_route like '%" + shared.toString
						+ "%' and route_id = " + r_id;

			} else {
				ourCase = "NO_ROUTE_ATALL_JPT";

			}
		}
		if (row_count > 1) {
			ourCase = "MULTIPLE_ROUTE";
			int correct_count = 0;

			List<Integer> r_id = new ArrayList<Integer>();
			List<Integer> r_id_valid = new ArrayList<Integer>();
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				r_id.add(Integer.parseInt(cur.getString(cur
						.getColumnIndex("route_id"))));
				cur.moveToNext();
			}
			int i = 0;
			while (i < r_id.size()) {
				String quer1 = "select route_node.id from route_node where route_id = "
						+ r_id.get(i)
						+ " and node_id = (select id from node where name = '"
						+ shared.fromString + "')";
				String quer2 = "select route_node.id from route_node where route_id = "
						+ r_id.get(i)
						+ " and node_id = (select id from node where name = '"
						+ shared.toString + "')";
				Cursor cur1 = dbHelper.getData(quer1);
				Cursor cur2 = dbHelper.getData(quer2);
				List<Integer> r_n_id = new ArrayList<Integer>();
				cur1.moveToFirst();
				cur2.moveToFirst();
				r_n_id.add(Integer.parseInt(cur1.getString(cur1
						.getColumnIndex("id"))));
				r_n_id.add(Integer.parseInt(cur2.getString(cur2
						.getColumnIndex("id"))));
				Log.d("crossvoer", "values of r_n_id:" + r_n_id);
				if (r_n_id.get(0) < r_n_id.get(1)) {

					r_id_valid.add(r_id.get(i));
					correct_count++;

				}
				i++;
			}
			if (correct_count == 0) {
				ourCase = "NO_ROUTE_ATALL_JPT";

			}
			if (correct_count > 0) {
				ourCase = "MULTIPLE_ROUTE";
			}
			StringBuilder MULTIPLE_ROUTE_VALID_ROUTE = new StringBuilder();
			MULTIPLE_ROUTE_VALID_ROUTE.append("select "
					+ tablehelper.TransportationColumns.CONTACT_PHONE + ", "
					+ tablehelper.TransportationColumns.DEPART_TIME + ", "
					+ tablehelper.TransportationColumns.ARRIVAL_TIME + ", "
					+ tablehelper.TransportationColumns.COST + ", "
					+ tablehelper.TransportationColumns.REMARKS + ", "
					+ tablehelper.TransportationColumns.TYPE_ID + ", "
					+ tablehelper.TransportationColumns.ID + ", "
					+ tablehelper.TransportationColumns.INTERVAL + ", "
					+ tablehelper.TransportationColumns.ROUTE_ID + ", "
					+ tablehelper.CompanyColumns.NAME + ", "
					+ tablehelper.RouteColumns.FULL_ROUTE + " from "
					+ tablehelper.TransportationColumns.TABLENAME + " JOIN "
					+ tablehelper.ScheduleColumns.TABLENAME + " ON "
					+ tablehelper.ScheduleColumns.TRANSPORTATION_ID + " = "
					+ tablehelper.TransportationColumns.ID + " JOIN "
					+ tablehelper.CompanyColumns.TABLENAME + " ON "
					+ tablehelper.CompanyColumns.ID + " = "
					+ tablehelper.TransportationColumns.COMPANY_ID + " JOIN "
					+ tablehelper.RouteColumns.TABLENAME + " ON "
					+ tablehelper.TransportationColumns.ROUTE_ID + " = "
					+ tablehelper.RouteColumns.ID
					+ " where route.full_route like '%" + shared.fromString
					+ "%' and route.full_route like '%" + shared.toString
					+ "%' and route_id in (");
			Iterator<Integer> it = r_id_valid.iterator();
			while (it.hasNext()) {
				MULTIPLE_ROUTE_VALID_ROUTE.append(it.next());
				MULTIPLE_ROUTE_VALID_ROUTE.append(",");
			}
			MULTIPLE_ROUTE_VALID_ROUTE.append("0)");
			MULTIPLE_VALID_ROUTE = MULTIPLE_ROUTE_VALID_ROUTE.toString();

		}// end of if
		if (ourCase.equals("NO_ROUTE_ATALL_JPT")) {

			return "select full_route from route where name = 'aaa'";
		} else if (ourCase.equals("SINGLE_ROUTE")) {
			return single_route;
		} else if (ourCase.equals("MULTIPLE_ROUTE")) {
			return MULTIPLE_VALID_ROUTE;
		} else
			return "";
	}

}

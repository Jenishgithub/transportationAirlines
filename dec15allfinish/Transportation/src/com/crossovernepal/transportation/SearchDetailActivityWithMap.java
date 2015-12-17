package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class SearchDetailActivityWithMap extends FragmentActivity {
	MySQLiteHelper dbHelper;
	String transportation_id, route_id;
	GoogleMap googleMap;

	final String TAG = "PathGoogleMapActivity";
	LatLng KUPONDOLE, RATNAPARK, MAITIGHAR, THROUGHPOINT1, THROUGHPOINT2,
			THROUGHPOINT3;
	Cursor cur;

	List<String> arrivaltimes = new ArrayList<String>();
	TextView sourcendestination, tvcost;
	TextView tvdeparturetime, tvarrivaltime, tvfromlocation, tvtolocation;
	String transportation_cost, arrived_time;
	List<List<Double>> outerlist = new ArrayList<List<Double>>();
	ProgressBar pbWait;
	RelativeLayout llMap;
	TextView tvdeparturetimefirstinner, tvdeparturetimesecondinner,
			tvdeparturetimethirdinner;
	TextView tvfromLocationfirstinner, tvfromLocationsecondinner,
			tvfromLocationthirdinner;
	List<String> routes_individuals_trimmed = new ArrayList<String>();
	View linefirstinner, linesecondinner, linethirdinner;
	ListView lvfull_route;
	List<FullRouteArrivalTime> route_arrival = new ArrayList<FullRouteArrivalTime>();
	SearchDetailAdapter adapter_seachdetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_google_map);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		llMap = (RelativeLayout) findViewById(R.id.llMap);

		Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					SearchDetailActivityWithMap.this
							.runOnUiThread(new Runnable() {
								public void run() {
									pbWait.setVisibility(View.GONE);
									llMap.setVisibility(View.VISIBLE);
								}
							});
				}
				super.run();

			}

		};
		timer.start();
		ImageButton backBtn;
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();

			}
		});
		String routes_individuals[] = null;

		ImageButton bookmarkbtn;
		bookmarkbtn = (ImageButton) findViewById(R.id.bookMarkBtn);
		bookmarkbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String check = "select * from bookmarks where "
						+ tablehelper.BookmarksColumns.TRANSPORTATION_ID
						+ " = " + transportation_id;
				boolean if_exist = dbHelper.checkIfDataExists(check);

				if (!if_exist) {

					ContentValues contentValue = new ContentValues();
					contentValue.put("transportation_id", transportation_id);

					dbHelper.insertIntoTable(dbHelper.TAG_BOOKMARKS,
							contentValue);
					Toast.makeText(getApplicationContext(),
							"Page is succesfully added to the bookmarks...",
							Toast.LENGTH_LONG).show();

				}

				else {

					new AlertDialog.Builder(SearchDetailActivityWithMap.this)

							.setMessage(
									"You have already bookmarked this transportation.")
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				}
			}

		});
		sourcendestination = (TextView) findViewById(R.id.tvSourceNDestination);
		tvcost = (TextView) findViewById(R.id.tvCost);

		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		googleMap = fm.getMap();

		dbHelper = new MySQLiteHelper(getApplicationContext());

		String query1 = "select " + tablehelper.NodeColumns.LAT + ","
				+ tablehelper.NodeColumns.LNG + " from "
				+ tablehelper.NodeColumns.TABLENAME + " where "
				+ tablehelper.NodeColumns.NAME + "=" + "'" + shared.fromString
				+ "'";
		String query2 = "select " + tablehelper.NodeColumns.LAT + ","
				+ tablehelper.NodeColumns.LNG + " from "
				+ tablehelper.NodeColumns.TABLENAME + " where "
				+ tablehelper.NodeColumns.NAME + "=" + "'" + shared.toString
				+ "'";
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			transportation_id = extra.getString("id");

			route_id = extra.getString("key_route_id");

			String query_fullroute = "select full_route	from route where id="
					+ route_id;
			Cursor cur_full_route = dbHelper.getData(query_fullroute);
			cur_full_route.moveToFirst();
			String full_route = cur_full_route.getString(cur_full_route
					.getColumnIndex("full_route"));

			// split of the roots

			routes_individuals = full_route.split(",");

			for (int i = 0; i < routes_individuals.length; i++) {

				routes_individuals_trimmed.add(routes_individuals[i].trim());
			}

			// get latitude and longitude value of indivudual node:

			int i = 0;
			while (i < routes_individuals.length) {
				List<Double> listlatlng = new ArrayList<Double>();
				String query_latlng_individual_node = "select lat, lon from node where name="
						+ "'" + routes_individuals_trimmed.get(i) + "'";
				Cursor cur_latlng_individual_node = dbHelper
						.getData(query_latlng_individual_node);
				cur_latlng_individual_node.moveToFirst();
				listlatlng.add(Double.parseDouble(cur_latlng_individual_node
						.getString(0)));
				listlatlng.add(Double.parseDouble(cur_latlng_individual_node
						.getString(1)));
				cur_latlng_individual_node.close();
				outerlist.add(listlatlng);
				i++;
			}

			@SuppressWarnings("unused")
			int size_outerlist = outerlist.size();

			int i1 = 1;
			if (outerlist.size() > 2) {

				@SuppressWarnings("unused")
				Double kamalpokharilat = outerlist.get(i1).get(0);
				@SuppressWarnings("unused")
				Double kamalpokharilong = outerlist.get(i1).get(1);

				i1++;

			}
			if (i1 != (outerlist.size() - 1)) {

				@SuppressWarnings("unused")
				Double sinamalgallat = outerlist.get(i1).get(0);
				@SuppressWarnings("unused")
				Double sinamagngallong = outerlist.get(i1).get(1);
				i1++;

			}

			String query_cost = "select "
					+ tablehelper.TransportationColumns.COST + " from "
					+ tablehelper.TransportationColumns.TABLENAME + " where "
					+ tablehelper.TransportationColumns.ID + "="
					+ transportation_id;
			Cursor cur_cost = dbHelper.getData(query_cost);
			cur_cost.moveToFirst();

			transportation_cost = cur_cost.getString(0);

			String query = "select "
					+ tablehelper.TransportationDetailsColumns.DEPART_TIME
					+ ","
					+ tablehelper.TransportationDetailsColumns.ARRIVAL_TIME
					+ ","
					+ tablehelper.TransportationDetailsColumns.DETAILS
					+ ","
					+ tablehelper.NodeColumns.NAME
					+ ","
					+ tablehelper.NodeColumns.LAT
					+ ","
					+ tablehelper.NodeColumns.LNG
					+ " from "
					+ tablehelper.TransportationDetailsColumns.TABLENAME
					+ " JOIN "
					+ tablehelper.NodeColumns.TABLENAME
					+ " ON "
					+ tablehelper.TransportationDetailsColumns.NODE_ID
					+ " = "
					+ tablehelper.NodeColumns.ID
					+ " where "
					+ tablehelper.TransportationDetailsColumns.TRANSPORTATION_ID
					+ " = " + transportation_id;

			cur = dbHelper.getData(query);

			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				arrivaltimes.add(cur.getString(1));
				cur.moveToNext();
			}

		}

		Cursor curd1 = dbHelper.getData(query1);
		Cursor curd2 = dbHelper.getData(query2);

		List<String> latlngsource = new ArrayList<String>();
		List<String> latlngdest = new ArrayList<String>();
		if (curd1 != null) {

			while (curd1.moveToNext()) {
				latlngsource.add(curd1.getString(curd1.getColumnIndex("lat")));
				latlngsource.add(curd1.getString(curd1.getColumnIndex("lon")));
			}

		}
		if (curd2 != null) {

			while (curd2.moveToNext()) {
				latlngdest.add(curd2.getString(curd2.getColumnIndex("lat")));
				latlngdest.add(curd2.getString(curd2.getColumnIndex("lon")));
			}

		}

		KUPONDOLE = new LatLng(outerlist.get(0).get(0), outerlist.get(0).get(1));
		RATNAPARK = new LatLng(outerlist.get(outerlist.size() - 1).get(0),
				outerlist.get(outerlist.size() - 1).get(1));
		int waitforlat = 1;
		if (outerlist.size() > 2) {
			THROUGHPOINT1 = new LatLng(outerlist.get(1).get(0), outerlist
					.get(1).get(1));
			waitforlat++;
			if (waitforlat != outerlist.size() - 1) {

				THROUGHPOINT2 = new LatLng(outerlist.get(2).get(0), outerlist
						.get(2).get(1));
				waitforlat++;
				if (waitforlat != outerlist.size() - 1) {
					THROUGHPOINT3 = new LatLng(outerlist.get(3).get(0),
							outerlist.get(3).get(1));
					waitforlat++;
				}
			}

		}

		String url = getMapsApiDirectionsUrl();
		ReadTask downloadTask = new ReadTask();
		downloadTask.execute(url);
		if (outerlist.size() > 10) {
			String url2 = getMapsApiDirectionsUrl2();
			ReadTask2 downloadTask2 = new ReadTask2();
			downloadTask2.execute(url2);
		}
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KUPONDOLE, 13));
		addMarkers();

		if (googleMap == null) {
			Toast.makeText(getApplicationContext(),
					"Sorry!!! somethings wrong", Toast.LENGTH_LONG).show();

		}

		sourcendestination.setText(routes_individuals_trimmed.get(0)
				+ "-"
				+ routes_individuals_trimmed.get(routes_individuals_trimmed
						.size() - 1));

		if ((arrivaltimes.get(0).equals("00:00:00"))
				&& (arrivaltimes.get(arrivaltimes.size() - 1)
						.equals("00:00:00"))) {
			sourcendestination.append("(All Day)");
		} else
			sourcendestination.append("(" + arrivaltimes.get(0) + "-"
					+ arrivaltimes.get(arrivaltimes.size() - 1) + ")");

		tvcost.setText("Rs." + transportation_cost);

		int a = 0;
		while (a < routes_individuals_trimmed.size()) {
			route_arrival.add(new FullRouteArrivalTime(arrivaltimes.get(a),
					routes_individuals_trimmed.get(a)));
			a++;
		}
		lvfull_route = (ListView) findViewById(R.id.lvfull_route);
		adapter_seachdetail = new SearchDetailAdapter(getApplicationContext(),
				R.layout.searchdetailadapterlistitem, route_arrival);
		lvfull_route.setAdapter(adapter_seachdetail);
		lvfull_route.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				googleMap.animateCamera(CameraUpdateFactory
						.newLatLng(new LatLng(outerlist.get(position).get(0),
								outerlist.get(position).get(1))));
			}
		});
	}

	int i1 = 1;

	private String getMapsApiDirectionsUrl() {
		// TODO Auto-generated method stub
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");

		urlString.append(outerlist.get(0).get(0));
		urlString.append(",");

		urlString.append(outerlist.get(0).get(1));
		urlString.append("&destination=");

		if (outerlist.size() <= 10) {

			urlString.append(outerlist.get(outerlist.size() - 1).get(0));
			urlString.append(",");

			urlString.append(outerlist.get(outerlist.size() - 1).get(1));
		} else {
			urlString.append(outerlist.get(9).get(0));
			urlString.append(",");

			urlString.append(outerlist.get(9).get(1));
		}
		urlString.append("&waypoints=optimize:true|");
		int i1 = 1;

		while ((i1 < (outerlist.size() - 2) && (i1 < 8))) {

			urlString.append(outerlist.get(i1).get(0));
			urlString.append(",");
			urlString.append(outerlist.get(i1).get(1));
			urlString.append("|");
			i1++;
		}
		urlString.append(outerlist.get(i1).get(0));
		urlString.append(",");
		urlString.append(outerlist.get(i1).get(1));

		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}

	private String getMapsApiDirectionsUrl2() {
		// TODO Auto-generated method stub
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");

		urlString.append(outerlist.get(9).get(0));
		urlString.append(",");

		urlString.append(outerlist.get(9).get(1));
		urlString.append("&destination=");

		urlString.append(outerlist.get(outerlist.size() - 1).get(0));
		urlString.append(",");

		urlString.append(outerlist.get(outerlist.size() - 1).get(1));
		urlString.append("&waypoints=optimize:true|");
		i1 = 10;
		int i2 = 1;
		while ((i1 < outerlist.size() - 2) && (i2 < 8)) {

			urlString.append(outerlist.get(i1).get(0));
			urlString.append(",");
			urlString.append(outerlist.get(i1).get(1));
			urlString.append("|");
			i1++;
			i2++;
		}
		urlString.append(outerlist.get(outerlist.size() - 2).get(0));
		urlString.append(",");
		urlString.append(outerlist.get(outerlist.size() - 2).get(1));

		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}

	private void addMarkers() {
		if (googleMap != null) {

			int j = 0;
			while (j < outerlist.size()) {
				googleMap.addMarker(new MarkerOptions().position(
						new LatLng(outerlist.get(j).get(0), outerlist.get(j)
								.get(1))).title(
						routes_individuals_trimmed.get(j)));
				j++;

			}

		}
	}

	private class ReadTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				// this exception is caught

			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			new ParserTask().execute(result);
		}
	}

	private class ReadTask2 extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				// this exception is caught

			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			new ParserTask2().execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) {

			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;
			// traversing through routes
			try {
				for (int i = 0; i < routes.size(); i++) {
					points = new ArrayList<LatLng>();
					polyLineOptions = new PolylineOptions();
					List<HashMap<String, String>> path = routes.get(i);

					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);
						points.add(position);
					}
					polyLineOptions.addAll(points);
					polyLineOptions.width(4);
					polyLineOptions.color(Color.BLUE);
				}
				googleMap.addPolyline(polyLineOptions);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(
						getApplicationContext(),
						"Oops!!! Could not draw the path due to server disconnectivity. Please try again!!!",
						Toast.LENGTH_SHORT).show();

			}
		}
	}

	private class ParserTask2 extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) {

			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;
			// traversing through routes
			try {
				for (int i = 0; i < routes.size(); i++) {
					points = new ArrayList<LatLng>();
					polyLineOptions = new PolylineOptions();
					List<HashMap<String, String>> path = routes.get(i);

					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);
						points.add(position);
					}
					polyLineOptions.addAll(points);
					polyLineOptions.width(4);
					polyLineOptions.color(Color.BLUE);
				}
				googleMap.addPolyline(polyLineOptions);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(
						getApplicationContext(),
						"Oops!!! Could not draw the path due to server disconnectivity. Please try again!!!",
						Toast.LENGTH_SHORT).show();

			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}

}
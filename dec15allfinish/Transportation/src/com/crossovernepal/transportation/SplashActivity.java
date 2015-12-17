package com.crossovernepal.transportation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.modellingpackages.TransportationInfoList;

public class SplashActivity extends Activity {
	MySQLiteHelper myDbHelper;

	// public static final String MyPREFERENCES1 = "MyPrefs1";
	// public static final String MyPREFERENCES2 = "MyPrefs2";

	private final int FIRST_DELAY = 1000;
	private final int SECOND_DELAY = 2000;
	private final int THIRD_DELAY = 3000;
	private final int FOURTH_DELAY = 4000;

	private Handler mHandler;
	private ImageView mImageView;

	JSONArray bookmarks, category, company, district, language, node, route,
			route_node, service, type, transportation, schedule,
			transportation_details = null;
	// for airlines
	JSONArray airlines, airlines_classes,
			transportation_airline_details = null;

	public static String url = "http://demo.crossovernepal.com/transportation/admin/jsonData";
	// public static String url =
	// "http://192.168.0.78/crossover/transportation/admin/jsondata";
	// public static String url =
	// "http://192.168.0.72/crossover/transportation/admin/jsondata";
	// public static String url =
	// "http://192.168.1.106/crossover/transportation/admin/jsondata";
	TextView tvSplash;
	public static TransportationInfoList transportInfo;
	// SharedPreferences sharedPref;
	Map<String, Map<String, Float>> NODEDISTMAP;
	public static Map<String, Map<String, Double>> saveddistancecost;
	// public static SharedPreferences.Editor editor1;
	Gson gson = new Gson();

	List<String> all_nodes = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		myDbHelper = new MySQLiteHelper(getApplicationContext());
		tvSplash = (TextView) findViewById(R.id.tvSplash);
		boolean isdatabasecreated = myDbHelper.checkDataBase();
		boolean isnetworkpresent = isOnline();

		if (isdatabasecreated == false && isnetworkpresent == false) {
			Toast.makeText(getApplicationContext(),
					"Internet is must to install this application",
					Toast.LENGTH_LONG).show();

			goToalert();

		} else if (isdatabasecreated == true && isnetworkpresent == true) {

			// sharedPref = getSharedPreferences(MyPREFERENCES1, MODE_PRIVATE);
			// // if (sharedPref != null) {
			// Boolean isDistCostCalculated = sharedPref.getBoolean(
			// "DistCost", false);
			if (!(new SessionManager(SplashActivity.this).checkDistCost())) {
				getCorrespondingNodes(this);
			}

			// }

			saveddistancecost = loadMap(this);
			start();

		} else if (isdatabasecreated == false && isnetworkpresent == true) {
			new GetJsonData().execute();
		} else if (isdatabasecreated == true && isnetworkpresent == false) {
			// sharedPref = getSharedPreferences(MyPREFERENCES1, MODE_PRIVATE);
			// if (sharedPref != null) {
			// Boolean isDistCostCalculated = sharedPref.getBoolean(
			// "DistCost", false);
			if (!(new SessionManager(SplashActivity.this).checkDistCost())) {
				getCorrespondingNodes(this);
			}

			// }
			// retrieve the distance cost hashmap
			saveddistancecost = loadMap(this);
			start();
		}

	}

	public Map<String, Map<String, Double>> loadMap(Context context) {
		// TODO Auto-generated method stub
		Map<String, Map<String, Double>> outputMap = new HashMap<>();
		// SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES1,
		// MODE_PRIVATE);
		try {
			// if (sharedPref != null) {

			// String jsonString = sharedPref.getString("DistCostVal",
			// (new JSONObject()).toString());
			// JSONObject jsonObject = new JSONObject(jsonString);
			// Iterator<String> keysItr = jsonObject.keys();
			// while (keysItr.hasNext()) {
			//
			// String key = keysItr.next();
			// Map<String, Double> temp = new HashMap<>();
			// JSONObject tempjSONOBject = new JSONObject(
			// (String) jsonObject.get(key));
			//
			// Iterator<String> tempKey = tempjSONOBject.keys();
			// while (tempKey.hasNext()) {
			// String key2 = tempKey.next();
			// temp.put(key2, tempjSONOBject.getDouble(key2));
			// }
			// outputMap.put(key, temp);
			// }

			// String jsonString = sharedPref.getString("DistCostVal",
			// (new JSONObject()).toString());
			String jsonString = new SessionManager(context).getDistCostVal();
			Type type = new TypeToken<Map<String, Map<String, Double>>>() {
			}.getType();
			outputMap = gson.fromJson(jsonString, type);
			// }
		} catch (Exception e) {
			e.printStackTrace();
			String error = e.toString();
			Log.d("crossover", "the error is:" + error);
		}
		return outputMap;
	}

	public void getCorrespondingNodes(Context context) {
		// TODO Auto-generated method stub
		MySQLiteHelper myDbHelper = new MySQLiteHelper(context);
		String query_nodes = "select distinct " + tablehelper.NodeColumns.NAME
				+ ", " + tablehelper.NodeColumns.LAT + ", "
				+ tablehelper.NodeColumns.LNG + " from "
				+ tablehelper.NodeColumns.TABLENAME + " order by "
				+ tablehelper.NodeColumns.NAME + " asc";
		Cursor cur = myDbHelper.getData(query_nodes);

		if (cur.getCount() > 0) {
			cur.moveToFirst();
			List<List<String>> nodes = new ArrayList<>();
			List<String> nodes_latlng = null;
			while (!cur.isAfterLast()) {

				all_nodes.add(cur.getString(cur
						.getColumnIndex(tablehelper.NodeColumns.NAME)));

				nodes_latlng = new ArrayList<>();
				nodes_latlng.add(cur.getString(cur
						.getColumnIndex(tablehelper.NodeColumns.NAME)));
				nodes_latlng.add(cur.getString(cur
						.getColumnIndex(tablehelper.NodeColumns.LAT)));
				nodes_latlng.add(cur.getString(cur
						.getColumnIndex(tablehelper.NodeColumns.LNG)));
				nodes.add(nodes_latlng);
				cur.moveToNext();
			}
			cur.close();
			int i = 0;
			NODEDISTMAP = new HashMap<>();
			while (i < nodes.size()) {

				String query = "SELECT DISTINCT " + tablehelper.RouteColumns.ID
						+ ", " + tablehelper.RouteColumns.FULL_ROUTE + " FROM "
						+ tablehelper.RouteColumns.TABLENAME + " JOIN "
						+ tablehelper.Route_NodeColumns.TABLENAME + " ON "
						+ tablehelper.RouteColumns.ID + " = "
						+ tablehelper.Route_NodeColumns.ROUTE_ID + " WHERE "
						+ tablehelper.RouteColumns.FULL_ROUTE + " NOT LIKE '%"
						+ nodes.get(i).get(0) + ",' AND "
						+ tablehelper.RouteColumns.FULL_ROUTE + " LIKE '%"
						+ nodes.get(i).get(0) + "%' AND "
						+ tablehelper.Route_NodeColumns.NODE_ID + " = (SELECT "
						+ tablehelper.NodeColumns.ID + " FROM "
						+ tablehelper.NodeColumns.TABLENAME + " WHERE "
						+ tablehelper.NodeColumns.NAME + " = '"
						+ nodes.get(i).get(0) + "')";

				cur = myDbHelper.getData(query);
				List<Integer> routeId = new ArrayList<>();
				if (cur.getCount() > 0) {
					cur.moveToFirst();
					while (!cur.isAfterLast()) {
						routeId.add(cur.getInt(cur
								.getColumnIndex(tablehelper.RouteColumns.ID)));
						cur.moveToNext();
					}
					cur.close();

					Map<String, Float> distMap = new HashMap<>();
					for (int ROUTEID : routeId) {
						query = "SELECT "
								+ tablehelper.Route_NodeColumns.NODE_ID + ", "
								+ tablehelper.NodeColumns.NAME + ", "
								+ tablehelper.NodeColumns.LAT + ", "
								+ tablehelper.NodeColumns.LNG + " FROM "
								+ tablehelper.Route_NodeColumns.TABLENAME
								+ " JOIN " + tablehelper.NodeColumns.TABLENAME
								+ " ON "
								+ tablehelper.Route_NodeColumns.NODE_ID + " = "
								+ tablehelper.NodeColumns.ID + " WHERE "
								+ tablehelper.Route_NodeColumns.ROUTE_ID
								+ " = " + ROUTEID + " and "
								+ tablehelper.Route_NodeColumns.ID
								+ " =(( SELECT "
								+ tablehelper.Route_NodeColumns.ID + "  FROM "
								+ tablehelper.Route_NodeColumns.TABLENAME
								+ " JOIN " + tablehelper.NodeColumns.TABLENAME
								+ " ON "
								+ tablehelper.Route_NodeColumns.NODE_ID + " = "
								+ tablehelper.NodeColumns.ID + " WHERE "
								+ tablehelper.Route_NodeColumns.ROUTE_ID
								+ " = " + ROUTEID + " and "
								+ tablehelper.NodeColumns.NAME + " ='"
								+ nodes.get(i).get(0) + "') +1)";
						cur = myDbHelper.getData(query);
						if (cur.getCount() > 0) {
							cur.moveToFirst();
							distMap.put(
									cur.getString(cur
											.getColumnIndex(tablehelper.NodeColumns.NAME)),
									getDistance(
											new LatLng(Double.parseDouble(nodes
													.get(i).get(1)), Double
													.parseDouble(nodes.get(i)
															.get(2))),
											new LatLng(
													Double.parseDouble(cur.getString(cur
															.getColumnIndex(tablehelper.NodeColumns.LAT))),
													Double.parseDouble(cur.getString(cur
															.getColumnIndex(tablehelper.NodeColumns.LNG))))));
							cur.close();
							Log.d("crossover", "value of distmap:" + distMap);
						}
					}
					NODEDISTMAP.put(nodes.get(i).get(0), distMap);
				}
				i++;
			}
			Log.d("crossover", "list of all mappings:" + NODEDISTMAP);

		}

		// sharedPref = getSharedPreferences(MyPREFERENCES1, MODE_PRIVATE);
		// editor1 = sharedPref.edit();
		// editor1.putBoolean("DistCost", true);
		// editor1.commit();
		new SessionManager(context).saveDistCost(true);

		// JSONObject json = new JSONObject(NODEDISTMAP);
		// String jsonString = json.toString();

		String jsonString = gson.toJson(NODEDISTMAP);
		// SharedPreferences.Editor editor2 = sharedPref.edit();
		// editor2.putString("DistCostVal", jsonString);
		// editor2.commit();
		new SessionManager(context).saveDistCostVal(jsonString);

		// store all node names
		String nodes_str = gson.toJson(all_nodes);
		// SharedPreferences.Editor editor3 = sharedPref.edit();
		// editor3.putString("node_list", nodes_str);
		// editor3.commit();
		new SessionManager(context).saveNodeNames(nodes_str);

	}

	private Float getDistance(LatLng my_latlong, LatLng frnd_latlong) {
		// TODO Auto-generated method stub
		Location l1 = new Location("One");
		l1.setLatitude(my_latlong.latitude);
		l1.setLongitude(my_latlong.longitude);

		Location l2 = new Location("Two");
		l2.setLatitude(frnd_latlong.latitude);
		l2.setLongitude(frnd_latlong.longitude);

		Float distance = l1.distanceTo(l2);
		// String dist = distance + " M";
		//
		// if (distance > 1000.0f) {
		// distance = distance / 1000.0f;
		// dist = distance + " KM";
		// }
		return distance;
	}

	private void goToalert() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				SplashActivity.this);

		// set title
		alertDialogBuilder.setTitle("No Server Connectivity");

		// set dialog message
		alertDialogBuilder
				.setMessage("Choose!")
				.setCancelable(false)
				.setPositiveButton("Exit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								System.exit(0);
							}
						})
				.setNegativeButton("Setting",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								startActivity(new Intent(
										Settings.ACTION_SETTINGS));
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	private boolean isOnline() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) SplashActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}

	Runnable image1Runnable = new Runnable() {
		@Override
		public void run() {
			mImageView.setBackgroundResource(R.drawable.screen01);// First image
																	// icon
		}
	};
	Runnable image2Runnable = new Runnable() {
		@Override
		public void run() {
			mImageView.setBackgroundResource(R.drawable.screen02);// Second
																	// image
																	// icon
		}
	};
	Runnable image3Runnable = new Runnable() {
		@Override
		public void run() {
			mImageView.setBackgroundResource(R.drawable.screen03);// Third image
																	// icon
		}
	};
	Runnable image4Runnable = new Runnable() {
		@Override
		public void run() {
			mImageView.setBackgroundResource(R.drawable.screen04);// Fourth
			Intent intent = new Intent(getApplicationContext(),
					HomeActivity.class);
			startActivity(intent); // image
									// icon
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	public void start() {
		tvSplash.setVisibility(View.GONE);
		mImageView = (ImageView) findViewById(R.id.imageView1);
		mHandler = new Handler();
		mHandler.postDelayed(image1Runnable, FIRST_DELAY);
		mHandler.postDelayed(image2Runnable, SECOND_DELAY);
		mHandler.postDelayed(image3Runnable, THIRD_DELAY);
		mHandler.postDelayed(image4Runnable, FOURTH_DELAY);

	}

	public void checkForUpdate() {
		url = "http://demo.crossovernepal.com/transportation/admin/jsonData";
		new GetJsonData().execute();
	}

	private class GetJsonData extends AsyncTask<Void, Void, Void> {
		String jsondata;

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			getCorrespondingNodes(getApplicationContext());
			saveddistancecost = loadMap(getApplicationContext());
			start();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ObjectMapper mapper = new ObjectMapper();

			if (transportInfo == null) {
				try {
					URL jsonURL = new URL(url);
					transportInfo = mapper.readValue(jsonURL,
							TransportationInfoList.class);
					jsondata = mapper.writeValueAsString(transportInfo);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// ServiceHandler sh = new ServiceHandler();
			// String jsondata = sh.makeServiceCall(url, ServiceHandler.GET);
			// try {
			// jsondata = readJsonFromUrl(url);
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			if (jsondata != null) {

				try {

					JSONObject jsonObj = new JSONObject(jsondata);
					bookmarks = jsonObj.getJSONArray(myDbHelper.TAG_BOOKMARKS);
					category = jsonObj.getJSONArray(myDbHelper.TAG_CATEGORY);
					company = jsonObj.getJSONArray(myDbHelper.TAG_COMPANY);
					district = jsonObj.getJSONArray(myDbHelper.TAG_DISTRICT);
					language = jsonObj.getJSONArray(myDbHelper.TAG_LANGUAGE);
					node = jsonObj.getJSONArray(myDbHelper.TAG_NODE);
					route = jsonObj.getJSONArray(myDbHelper.TAG_ROUTE);
					route_node = jsonObj
							.getJSONArray(myDbHelper.TAG_ROUTE_NODE);
					service = jsonObj.getJSONArray(myDbHelper.TAG_SERVICE);
					type = jsonObj.getJSONArray(myDbHelper.TAG_TYPE);
					transportation = jsonObj
							.getJSONArray(myDbHelper.TAG_TRANSPORTATION);
					schedule = jsonObj.getJSONArray(myDbHelper.TAG_SCHEDULE);
					transportation_details = jsonObj
							.getJSONArray(myDbHelper.TAG_TRANSPORTATION_DETAILS);
					// for airlines
					airlines = jsonObj.getJSONArray(myDbHelper.TAG_AIRLINES);
					airlines_classes = jsonObj
							.getJSONArray(myDbHelper.TAG_AIRLINES_CLASSES);
					// pricing = jsonObj.getJSONArray(myDbHelper.TAG_PRICING);
					transportation_airline_details = jsonObj
							.getJSONArray(myDbHelper.TAG_TRANSPORTATION_AIRLINE_DETAILS);

					myDbHelper = new MySQLiteHelper(getApplicationContext());

					myDbHelper.createInsertQuery(myDbHelper.TAG_BOOKMARKS,
							bookmarks);
					myDbHelper.createInsertQuery(myDbHelper.TAG_CATEGORY,
							category);
					// myDbHelper.createInsertQuery(myDbHelper.TAG_COMPANY,
					// company);
					myDbHelper.insertCompany(myDbHelper.TAG_COMPANY);

					myDbHelper.createInsertQuery(myDbHelper.TAG_DISTRICT,
							district);
					myDbHelper.createInsertQuery(myDbHelper.TAG_LANGUAGE,
							language);
					// myDbHelper.createInsertQuery(myDbHelper.TAG_NODE, node);
					myDbHelper.insertNode(myDbHelper.TAG_NODE);

					// myDbHelper.createInsertQuery(myDbHelper.TAG_ROUTE,
					// route);
					myDbHelper.insertRoute(myDbHelper.TAG_ROUTE);

					// myDbHelper.createInsertQuery(myDbHelper.TAG_ROUTE_NODE,
					// route_node);
					myDbHelper.insertRoute_node(myDbHelper.TAG_ROUTE_NODE);

					myDbHelper.createInsertQuery(myDbHelper.TAG_SERVICE,
							service);
					myDbHelper.createInsertQuery(myDbHelper.TAG_TYPE, type);
					// myDbHelper.createInsertQuery(myDbHelper.TAG_SCHEDULE,
					// schedule);
					myDbHelper.insertSchedule(myDbHelper.TAG_SCHEDULE);
					// myDbHelper.createInsertQuery(
					// myDbHelper.TAG_TRANSPORTATION_DETAILS,
					// transportation_details);
					myDbHelper
							.insertTransportation_details(myDbHelper.TAG_TRANSPORTATION_DETAILS);

					// myDbHelper.createInsertQuery(myDbHelper.TAG_TRANSPORTATION,
					// transportation);
					myDbHelper
							.insertTransportation(myDbHelper.TAG_TRANSPORTATION);
					// for airlines
					myDbHelper.createInsertQuery(myDbHelper.TAG_AIRLINES,
							airlines);
					myDbHelper.createInsertQuery(
							myDbHelper.TAG_AIRLINES_CLASSES, airlines_classes);
					// myDbHelper.createInsertQuery(myDbHelper.TAG_PRICING,
					// pricing);
					myDbHelper.createInsertQuery(
							myDbHelper.TAG_TRANSPORTATION_AIRLINE_DETAILS,
							transportation_airline_details);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showAlert("Could Not Connect To Server.", "Oops!!");

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

			}

			return null;
		}

	}

	private void showAlert(String message, String title) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				getApplicationContext());
		builder1.setMessage(message);
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.setTitle(title);
		alert11.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
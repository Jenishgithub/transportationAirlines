package com.crossovernepal.transportation;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crossovernepal.transportation.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modellingpackages.TransportationInfoList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class UpdataActivity extends Activity {
	MySQLiteHelper myDbHelper;

	JSONArray bookmarks, category, company, district, language, node, route,
			route_node, service, type, transportation, schedule,
			transportation_details = null;
	// for airlines
	JSONArray airlines, airlines_classes,
			transportation_airline_details = null;
	private String url = "http://demo.crossovernepal.com/transportation/admin/jsonData";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		myDbHelper = new MySQLiteHelper(getApplicationContext());
		if (isOnline())
			new GetJsonData().execute();
		else {
			Toast.makeText(getApplicationContext(), "No server connectivity",
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateAutocompleteTextfields() {
		// TODO Auto-generated method stub
		MySQLiteHelper dbHelper = new MySQLiteHelper(getApplicationContext());
		ArrayAdapter adapter;
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		String[] route_name;
		int i = 0;
		try {
			list = dbHelper.getAllColumnsData("Select * from "
					+ tablehelper.NodeColumns.TABLENAME + "");

			route_name = new String[list.size()];
			for (ArrayList temp : list) {
				route_name[i] = temp.get(1).toString();
				i++;
				System.out.println("crossover:" + temp.get(1));

			}
			adapter = new ArrayAdapter(getApplicationContext(),
					R.layout.text_view, route_name);
			HomeFragment.fromPath.setThreshold(1);
			HomeFragment.fromPath.setAdapter(adapter);

			HomeFragment.toPath.setThreshold(1);
			HomeFragment.toPath.setAdapter(adapter);

			HomeFragment.throughPath1.setThreshold(1);
			HomeFragment.throughPath1.setAdapter(adapter);

			for (i = 0; i < list.size(); i++) {
				System.out.println("String array:" + route_name[i]);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class GetJsonData extends AsyncTask<Void, Void, Void> {
		String jsondata;

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			new SplashActivity().getCorrespondingNodes(getApplicationContext());
			SplashActivity.saveddistancecost = new SplashActivity()
					.loadMap(getApplicationContext());
			updateAutocompleteTextfields();
			finish();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			// SplashActivity.editor1.putBoolean("DistCost", false);

			// ServiceHandler sh = new ServiceHandler();
			// String jsondata = sh.makeServiceCall(SplashActivity.url,
			// ServiceHandler.GET);
			ObjectMapper mapper = new ObjectMapper();

			try {
				URL jsonURL = new URL(url);
				SplashActivity.transportInfo = mapper.readValue(jsonURL,
						TransportationInfoList.class);
				jsondata = mapper
						.writeValueAsString(SplashActivity.transportInfo);
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

			if (jsondata != null) {
				MySQLiteHelper mahHelper = new MySQLiteHelper(
						getApplicationContext());
				SQLiteDatabase db = mahHelper.getWritableDatabase();
				List<String> tables = new ArrayList<String>();
				Cursor cur = db
						.rawQuery(
								"SELECT * FROM sqlite_master WHERE type='table';",
								null);
				cur.moveToFirst();
				while (!cur.isAfterLast()) {
					String tableName = cur.getString(1);
					if ((!tableName.equals("android_metadata"))
							&& (!tableName.equals("sqlite_sequence")))
						tables.add(tableName);
					cur.moveToNext();
				}
				cur.close();

				for (String eachTable : tables) {
					if (!eachTable.equals("bookmarks"))

						db.execSQL("DROP TABLE IF EXISTS " + eachTable);
				}
				new MySQLiteHelper(getApplicationContext()).onCreate(db);

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

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				finish();

			}

			return null;
		}

	}

	private boolean isOnline() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

}
package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.List;

import com.crossovernepal.transportation.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SearchDetailActivity extends Activity {

	MySQLiteHelper dbHelper;
	String transportation_id, route_id;

	Cursor cur;

	List<String> arrivaltimes = new ArrayList<String>();
	TextView sourcendestination, tvcost;
	TextView tvdeparturetime, tvarrivaltime, tvfromlocation, tvtolocation;
	String transportation_cost;

	String arrived_time;
	TextView tvdeparturetimefirstinner, tvdeparturetimesecondinner,
			tvdeparturetimethirdinner;
	TextView tvfromLocationfirstinner, tvfromLocationsecondinner,
			tvfromLocationthirdinner;

	List<String> routes_individuals_trimmed = new ArrayList<String>();
	ListView lvfull_route;
	List<FullRouteArrivalTime> route_arrival = new ArrayList<FullRouteArrivalTime>();
	SearchDetailAdapter adapter_seachdetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_detail);

		ImageButton backBtn;
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();

			}
		});

		sourcendestination = (TextView) findViewById(R.id.tvSourceNDestination_nointernet);
		tvcost = (TextView) findViewById(R.id.tvCost_nointernet);

		dbHelper = new MySQLiteHelper(getApplicationContext());

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

			String[] routes_individuals = full_route.split(",");

			for (int i = 0; i < routes_individuals.length; i++) {

				routes_individuals_trimmed.add(routes_individuals[i].trim());
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

		@SuppressWarnings("unused")
		String lastvalue = arrivaltimes.get(arrivaltimes.size() - 1);

		arrived_time = extra.getString("use_arrivedtime");

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

	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}

}

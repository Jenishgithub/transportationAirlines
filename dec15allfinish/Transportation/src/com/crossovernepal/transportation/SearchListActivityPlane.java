package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class SearchListActivityPlane extends ListActivity {

	MySQLiteHelper dbHelper;
	Cursor cur;
	int v_id;
	MyArrayAdapter1 adpater1;
	public ImageButton backBtn;
	public Button priceBtn, timeBtn;
	TextView tvnomatchfound;
	Boolean head = false;
	ListView list;
	String myquery = null;
	public boolean price_clicked = false;
	public boolean time_clicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_list_plane);

		list = getListView();
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		priceBtn = (Button) findViewById(R.id.orderByPriceBtn);
		timeBtn = (Button) findViewById(R.id.orderByTimeBtn);
		tvnomatchfound = (TextView) findViewById(R.id.tvnomatchfound);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

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
		});

		priceBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unused")
				String arrange = selectQuery("cost", true);

				Toast.makeText(getApplicationContext(),
						"Transports ordered in price", Toast.LENGTH_SHORT)
						.show();
				if (price_clicked == false) {
					price_clicked = true;
					time_clicked = false;
					cur = dbHelper.getData(myquery
							+ " order by transportation.cost" + " asc");
					adpater1.cursor = cur;
					System.out.println("arrange");
					adpater1.notifyDataSetChanged();
				}
			}
		});

		timeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unused")
				String arrange = selectQuery("cost", true);
				Toast.makeText(getApplicationContext(),
						"Transports ordered in time", Toast.LENGTH_SHORT)
						.show();
				if (time_clicked == false) {
					price_clicked = false;
					time_clicked = true;
					cur = dbHelper.getData(myquery
							+ " order by transportation.interval" + " asc");
					adpater1.cursor = cur;
					adpater1.notifyDataSetChanged();
				}
			}
		});

		dbHelper = new MySQLiteHelper(getApplicationContext());

		if (shared.vehicle != "all")
			v_id = dbHelper.getIdForName(dbHelper.TAG_TYPE, shared.vehicle);

		try {
			myquery = selectQuery("cost", true);

		} catch (Exception e) {

		}

		cur = dbHelper.getData(myquery);

		try {
			if (cur.getCount() == 0) {
				// here cur is null so we will have to check if direct path is
				// possible without other search criteria before giving
				// suggestions
				myquery = selectQuery("cost", false);
				Cursor cur1 = dbHelper.getData(myquery);

				if (cur1.getCount() == 0) {

					head = true;
					Intent intentnomatch = new Intent(getApplicationContext(),
							NoMatchFound.class);

					startActivityForResult(intentnomatch, 2);

				} else {

					// --------------------
					list.setVisibility(View.GONE);
					tvnomatchfound.setVisibility(View.VISIBLE);

				}
			}
		} catch (Exception e) {

		}

		adpater1 = new MyArrayAdapter1(getApplicationContext(), cur);
		setListAdapter(adpater1);

		new HomeFragment().refresh();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 2) {
			SearchListActivityPlane.this.finish();
		}
	}

	public String selectQuery(String order, boolean check) {
		String MULTIPLE_VALID_ROUTE = "";
		String single_route = "";
		String ourCase = "";
		String myquery = "select "
				+ tablehelper.TransportationColumns.CONTACT_PHONE
				+ ", "
				+ tablehelper.TransportationColumns.DEPART_TIME
				+ ", "
				+ tablehelper.TransportationColumns.ARRIVAL_TIME
				+ ", "
				+ tablehelper.TransportationColumns.COST
				+ ", "
				+ tablehelper.TransportationColumns.REMARKS
				+ ", "
				+ tablehelper.TransportationColumns.TYPE_ID
				+ ", "
				+ tablehelper.TransportationColumns.ID
				+ ", "
				// + tablehelper.TransportationColumns.INTERVAL + ", "
				+ tablehelper.TransportationColumns.ROUTE_ID
				+ ", "
				+ tablehelper.AirlinesColumns.NAME
				+ ", "
				+ tablehelper.RouteColumns.FULL_ROUTE
				+ " from "
				+ tablehelper.TransportationColumns.TABLENAME
				+ " JOIN "
				+ tablehelper.ScheduleColumns.TABLENAME
				+ " ON "
				+ tablehelper.ScheduleColumns.TRANSPORTATION_ID
				+ " = "
				+ tablehelper.TransportationColumns.ID
				+ " JOIN "
				+ tablehelper.AirlinesColumns.TABLENAME
				+ " ON "
				+ tablehelper.AirlinesColumns.ID
				+ " = "
				+ tablehelper.TransportationAirlineDetailsColumns.AIRLINE_ID
				+ " JOIN "
				+ tablehelper.TransportationAirlineDetailsColumns.TABLENAME
				+ " ON "
				+ tablehelper.TransportationColumns.ID
				+ " = "
				+ tablehelper.TransportationAirlineDetailsColumns.TRANSPORTATION_ID
				+ " JOIN "
				+ tablehelper.RouteColumns.TABLENAME
				+ " ON "
				+ tablehelper.TransportationColumns.ROUTE_ID
				+ " = "
				+ tablehelper.RouteColumns.ID
				+ " where route.full_route like '%"
				+ shared.fromString
				+ "%' and route.full_route like '%"
				+ shared.toString
				+ "%'  and "
				+ tablehelper.TransportationAirlineDetailsColumns.AIRLINE_CLASS_ID
				+ " = (select " + tablehelper.AirlinesClassesColumns.ID
				+ " from " + tablehelper.AirlinesClassesColumns.TABLENAME
				+ " where " + tablehelper.AirlinesClassesColumns.NAME + " = '"
				+ shared.airline_class + "')" + " and ('" + shared.fromString
				+ "' = (select name from node where name  = '"
				+ shared.fromString + "')) and ('" + shared.toString
				+ "' = (select name from node where name = '" + shared.toString
				+ "'))";
		if (check)
			myquery = appendParameterToQuery(myquery);
		myquery = myquery + " order by transportation." + order + " asc";

		cur = dbHelper.getData(myquery);
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
						+ ", "
						+ tablehelper.TransportationColumns.DEPART_TIME
						+ ", "
						+ tablehelper.TransportationColumns.ARRIVAL_TIME
						+ ", "
						+ tablehelper.TransportationColumns.COST
						+ ", "
						+ tablehelper.TransportationColumns.REMARKS
						+ ", "
						+ tablehelper.TransportationColumns.TYPE_ID
						+ ", "
						+ tablehelper.TransportationColumns.ID
						+ ", "
						// + tablehelper.TransportationColumns.INTERVAL + ", "
						+ tablehelper.TransportationColumns.ROUTE_ID
						+ ", "
						+ tablehelper.AirlinesColumns.NAME
						+ ", "
						+ tablehelper.RouteColumns.FULL_ROUTE
						+ " from "
						+ tablehelper.TransportationColumns.TABLENAME
						+ " JOIN "
						+ tablehelper.ScheduleColumns.TABLENAME
						+ " ON "
						+ tablehelper.ScheduleColumns.TRANSPORTATION_ID
						+ " = "
						+ tablehelper.TransportationColumns.ID
						+ " JOIN "
						+ tablehelper.AirlinesColumns.TABLENAME
						+ " ON "
						+ tablehelper.AirlinesColumns.ID
						+ " = "
						+ tablehelper.TransportationAirlineDetailsColumns.AIRLINE_ID
						+ " JOIN "
						+ tablehelper.TransportationAirlineDetailsColumns.TABLENAME
						+ " ON "
						+ tablehelper.TransportationColumns.ID
						+ " = "
						+ tablehelper.TransportationAirlineDetailsColumns.TRANSPORTATION_ID
						+ " JOIN "
						+ tablehelper.RouteColumns.TABLENAME
						+ " ON "
						+ tablehelper.TransportationColumns.ROUTE_ID
						+ " = "
						+ tablehelper.RouteColumns.ID
						+ " where route.full_route like '%"
						+ shared.fromString
						+ "%' and route.full_route like '%"
						+ shared.toString
						+ "%'  and "
						+ tablehelper.TransportationAirlineDetailsColumns.AIRLINE_CLASS_ID
						+ " = (select " + tablehelper.AirlinesClassesColumns.ID
						+ " from "
						+ tablehelper.AirlinesClassesColumns.TABLENAME
						+ " where " + tablehelper.AirlinesClassesColumns.NAME
						+ " = '" + shared.airline_class + "')" + " and ('"
						+ shared.fromString
						+ "' = (select name from node where name  = '"
						+ shared.fromString + "')) and ('" + shared.toString
						+ "' = (select name from node where name = '"
						+ shared.toString + "')) and route_id = " + r_id;

			} else {
				ourCase = "NO_ROUTE_ATALL_JPT";
				// return "select * from route where name = 'aaa'";
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
					// save r_id in another list
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
			MULTIPLE_ROUTE_VALID_ROUTE
					.append("select "
							+ tablehelper.TransportationColumns.CONTACT_PHONE
							+ ", "
							+ tablehelper.TransportationColumns.DEPART_TIME
							+ ", "
							+ tablehelper.TransportationColumns.ARRIVAL_TIME
							+ ", "
							+ tablehelper.TransportationColumns.COST
							+ ", "
							+ tablehelper.TransportationColumns.REMARKS
							+ ", "
							+ tablehelper.TransportationColumns.TYPE_ID
							+ ", "
							+ tablehelper.TransportationColumns.ID
							+ ", "
							// + tablehelper.TransportationColumns.INTERVAL +
							// ", "
							+ tablehelper.TransportationColumns.ROUTE_ID
							+ ", "
							+ tablehelper.AirlinesColumns.NAME
							+ ", "
							+ tablehelper.RouteColumns.FULL_ROUTE
							+ " from "
							+ tablehelper.TransportationColumns.TABLENAME
							+ " JOIN "
							+ tablehelper.ScheduleColumns.TABLENAME
							+ " ON "
							+ tablehelper.ScheduleColumns.TRANSPORTATION_ID
							+ " = "
							+ tablehelper.TransportationColumns.ID
							+ " JOIN "
							+ tablehelper.AirlinesColumns.TABLENAME
							+ " ON "
							+ tablehelper.AirlinesColumns.ID
							+ " = "
							+ tablehelper.TransportationAirlineDetailsColumns.AIRLINE_ID
							+ " JOIN "
							+ tablehelper.TransportationAirlineDetailsColumns.TABLENAME
							+ " ON "
							+ tablehelper.TransportationColumns.ID
							+ " = "
							+ tablehelper.TransportationAirlineDetailsColumns.TRANSPORTATION_ID
							+ " JOIN "
							+ tablehelper.RouteColumns.TABLENAME
							+ " ON "
							+ tablehelper.TransportationColumns.ROUTE_ID
							+ " = "
							+ tablehelper.RouteColumns.ID
							+ " where route.full_route like '%"
							+ shared.fromString
							+ "%' and route.full_route like '%"
							+ shared.toString
							+ "%'  and "
							+ tablehelper.TransportationAirlineDetailsColumns.AIRLINE_CLASS_ID
							+ " = (select "
							+ tablehelper.AirlinesClassesColumns.ID + " from "
							+ tablehelper.AirlinesClassesColumns.TABLENAME
							+ " where "
							+ tablehelper.AirlinesClassesColumns.NAME + " = '"
							+ shared.airline_class + "')" + " and ('"
							+ shared.fromString
							+ "' = (select name from node where name  = '"
							+ shared.fromString + "')) and ('"
							+ shared.toString
							+ "' = (select name from node where name = '"
							+ shared.toString + "')) and route_id in (");
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

	private String appendParameterToQuery(String myquery) {
		String query = myquery;

		if (shared.through1String.length() > 0)
			query = query + " and ((" + tablehelper.RouteColumns.FULL_ROUTE
					+ " like '%" + shared.through1String + "%') and ('"
					+ shared.through1String
					+ "'=(select name from node where name ='"
					+ shared.through1String + "')))";

		if (shared.through2String.length() > 0)
			query = query + " and ((" + tablehelper.RouteColumns.FULL_ROUTE
					+ " like '%" + shared.through2String + "%') and ('"
					+ shared.through2String
					+ "'=(select name from node where name ='"
					+ shared.through2String + "')))";

		if (shared.through3String.length() > 0)
			query = query + " and ((" + tablehelper.RouteColumns.FULL_ROUTE
					+ " like '%" + shared.through3String + "%') and ('"
					+ shared.through3String
					+ "'=(select name from node where name ='"
					+ shared.through3String + "')))";
		else

		if (shared.day.length() > 0)
			query = query + " and schedule." + shared.day + "='1'";

		if (shared.fromPrice.length() > 0 && shared.toPrice.length() > 0)
			query = query + " and " + tablehelper.TransportationColumns.COST
					+ " between " + Integer.parseInt(shared.fromPrice)
					+ " and " + Integer.parseInt(shared.toPrice);

		if (shared.vehicle != "all") {
			query = query + " and transportation.type_id='" + v_id + "'";
		}
		if (shared.return_type.equals("1"))
			query = query + " and transportation.return_type='1'";
		else
			query = query + " and transportation.return_type='0'";

		return query;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		cur.moveToPosition(position);

		@SuppressWarnings("unused")
		String fullroute = cur.getString(cur.getColumnIndex("full_route"));
		String tr_id = cur.getString(cur.getColumnIndex("id"));

		String route_id = cur.getString(cur.getColumnIndex("route_id"));
		// added today
		String arrived_time = cur.getString(cur.getColumnIndex("arrival_time"));

		HomeActivity.mFrameLayout.setVisibility(View.VISIBLE);
		Intent intent = new Intent(getApplicationContext(),
				SearchDetailActivityWithMap.class);
		intent.putExtra("id", tr_id);

		intent.putExtra("key_route_id", route_id);
		intent.putExtra("use_arrivedtime", arrived_time);
		Intent intent_noInternt = new Intent(getApplicationContext(),
				SearchDetailActivity.class);
		intent_noInternt.putExtra("id", tr_id);

		intent_noInternt.putExtra("key_route_id", route_id);
		// added_today
		intent_noInternt.putExtra("use_arrivedtime", arrived_time);

		boolean net_yes_or_no = isOnline();

		if (net_yes_or_no == true) {
			// checking if google play service is available or not
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getApplicationContext());
			// showing staus
			if (status == ConnectionResult.SUCCESS) {

				startActivity(intent);

				super.onListItemClick(l, v, position, id);
			} else
				Toast.makeText(getApplicationContext(),
						"No google play services are available on your device",
						Toast.LENGTH_SHORT).show();
		} else {
			startActivity(intent_noInternt);
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (head)
			finish();

	}
}
package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class AlternativeRouteList extends ListActivity {
	AlternativeRouteAdapter adapter;
	List<Route> route = new ArrayList<Route>();
	String[] values;
	ImageButton backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alternative_route_list);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shared.fromString = "";
				shared.toString = "";
				shared.through1String = "";
				shared.fromPrice = "";
				shared.toPrice = "";
				shared.departureDate = "";
				shared.day = "";
				shared.return_type = "";
				if (shared.through2String.length() > 0) {
					shared.through2String = "";
				}
				if (shared.through3String.length() > 0) {
					shared.through3String = "";
				}
				HomeActivity.mFrameLayout.setVisibility(View.GONE);
				finish();
			}
		});

		Bundle extra = getIntent().getExtras();
		if (extra != null)
			values = extra.getStringArray("alternative_routes");

		for (int i = 0; i < values.length; i += 2)
			route.add(new Route(values[i].trim(), values[i + 1].trim()));

		ListView mListView = getListView();
		adapter = new AlternativeRouteAdapter(this,
				R.layout.alternative_route_list_item, route);
		mListView.setAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

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
		shared.return_type = "";

		String source = route.get(position).source;
		String destination = route.get(position).destination;

		shared.fromString = source;
		shared.toString = destination;

		startActivity(new Intent(getApplicationContext(),
				SearchListActivity.class));

	}

	public class Route {
		String source, destination;

		public Route(String source, String destination) {
			this.source = source;
			this.destination = destination;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		shared.fromString = "";
		shared.toString = "";
		shared.through1String = "";
		shared.fromPrice = "";
		shared.toPrice = "";
		shared.departureDate = "";
		shared.day = "";
		shared.return_type = "";
		if (shared.through2String.length() > 0) {
			shared.through2String = "";
		}
		if (shared.through3String.length() > 0) {
			shared.through3String = "";
		}
		HomeActivity.mFrameLayout.setVisibility(View.GONE);
		finish();
	}

}

package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.List;

import com.crossovernepal.transportation.AlternativeRouteList.Route;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlternativeRouteAdapter extends ArrayAdapter<Route> {
	Context mContext;
	List<Route> routes = new ArrayList<Route>();

	public AlternativeRouteAdapter(Context context, int textViewResourceId,
			List<Route> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		routes = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		LayoutInflater li = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate(R.layout.alternative_route_list_item, null);
		Route single_route = routes.get(position);
		TextView tvFrom = (TextView) v.findViewById(R.id.tvFrom);
		TextView tvTo = (TextView) v.findViewById(R.id.tvTo);
		tvFrom.setText(single_route.source);
		tvTo.setText(single_route.destination);
		return v;
	}

}

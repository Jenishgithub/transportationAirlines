package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchDetailAdapter extends ArrayAdapter<FullRouteArrivalTime> {
	Context mContext;
	List<FullRouteArrivalTime> route_time = new ArrayList<FullRouteArrivalTime>();
	LinearLayout llsearchdetailadapterlistitem;
	String start, end;

	public SearchDetailAdapter(Context context, int resource,
			List<FullRouteArrivalTime> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		route_time = objects;
		start = route_time.get(0).arrival_time;
		end = route_time.get(route_time.size() - 1).arrival_time;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;
		LayoutInflater li = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate(R.layout.searchdetailadapterlistitem, null);
		FullRouteArrivalTime single_class = route_time.get(position);
		TextView tvtime = (TextView) v.findViewById(R.id.tvtime);
		TextView tvnode = (TextView) v.findViewById(R.id.tvnode);

		if (!((start.equals("00:00:00")) && (end.equals("00:00:00"))))
			tvtime.setText(single_class.arrival_time);

		tvnode.setText(single_class.route_name);

		if ((route_time.get(position).route_name.equals(shared.fromString))
				|| (route_time.get(position).route_name.equals(shared.toString))
				|| route_time.get(position).route_name
						.equals(shared.through1String)
				|| route_time.get(position).route_name
						.equals(shared.through2String)
				|| route_time.get(position).route_name
						.equals(shared.through3String)) {

			llsearchdetailadapterlistitem = (LinearLayout) v
					.findViewById(R.id.llsearchdetailadapterlistitem);
			llsearchdetailadapterlistitem.setBackgroundColor(Color.argb(100,
					153, 204, 128));
		}
		return v;
	}

}

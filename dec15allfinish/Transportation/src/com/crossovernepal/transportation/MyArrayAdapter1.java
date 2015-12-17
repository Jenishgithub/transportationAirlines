package com.crossovernepal.transportation;

import com.crossovernepal.transportation.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class MyArrayAdapter1 extends ArrayAdapter {
	Context mContext;
	Cursor cursor;
	String name;

	public MyArrayAdapter1(Context applicationContext, Cursor cur) {
		// TODO Auto-generated constructor stub
		super(applicationContext, R.layout.list1);
		this.mContext = applicationContext;
		cursor = cur;

	}

	public int getCount() {

		return cursor.getCount();
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	public View getView(int position, View view, ViewGroup parent) {
		// inflate the layout for each item of listView
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.list1, null);

		// move the cursor to required position
		cursor.moveToPosition(position);

		// if (shared.vehicle != "Plane")
		name = cursor.getString(cursor.getColumnIndex("alias"));

		String depTime = cursor.getString(cursor.getColumnIndex("depart_time"));
		String arrTime = cursor
				.getString(cursor.getColumnIndex("arrival_time"));
		String time = depTime + "-" + arrTime;
		String fullroute = cursor
				.getString(cursor.getColumnIndex("full_route"));
		String cost = cursor.getString(cursor.getColumnIndex("cost"));

		// get the reference of textViews
		TextView firsttv = (TextView) view.findViewById(R.id.first);
		TextView secondtv = (TextView) view.findViewById(R.id.second);
		TextView thirdtv = (TextView) view.findViewById(R.id.third);
		TextView costtv = (TextView) view.findViewById(R.id.cost);

		// Set the Sender number and smsBody to respective TextViews
		if (!time.equals("00:00-00:00")) {
			firsttv.setText(time);
		}
		// if (shared.vehicle != "Plane")
		secondtv.setText(name);
		thirdtv.setText(fullroute);
		costtv.setText(cost);

		return view;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
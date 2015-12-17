package com.crossovernepal.transportation;

import com.crossovernepal.transportation.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class MyArrayAdapter2 extends ArrayAdapter {
	Context mContext;
	Cursor cursor;

	public MyArrayAdapter2(Context applicationContext, Cursor cur) {
		// TODO Auto-generated constructor stub
		super(applicationContext, R.layout.list2);
		this.mContext = applicationContext;

		cursor = cur;

	}

	public int getCount() {

		return cursor.getCount();
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	public View getView(int position, View view, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.list2, null);

		cursor.moveToPosition(position);

		String depTime = cursor.getString(cursor.getColumnIndex("depart_time"));
		String arrTime = cursor
				.getString(cursor.getColumnIndex("arrival_time"));

		String details = cursor.getString(cursor.getColumnIndex("details"));
		String nodename = cursor.getString(cursor
				.getColumnIndex(tablehelper.NodeColumns.NAME));

		int count = cursor.getCount() - 1;
		TextView firsttv = (TextView) view.findViewById(R.id.first);
		TextView secondtv = (TextView) view.findViewById(R.id.second);
		TextView thirdtv = (TextView) view.findViewById(R.id.third);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);

		if (position != 0) {
			firsttv.setText(arrTime + " Arrived: " + nodename);
		} else
			firsttv.setText(arrTime + " Departure: " + nodename);
		secondtv.setText(details);
		if (position > 0 && position < count) {
			thirdtv.setVisibility(View.VISIBLE);
			if (position == 0) {
				thirdtv.setText(depTime + " Departure: " + nodename);
			} else
				thirdtv.setText(depTime);
			icon.setVisibility(View.VISIBLE);
		} else {
			if (position != 0)
				icon.setVisibility(View.GONE);
			thirdtv.setVisibility(View.GONE);
		}

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

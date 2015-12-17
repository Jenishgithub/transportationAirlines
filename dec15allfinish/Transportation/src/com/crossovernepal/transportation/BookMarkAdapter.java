package com.crossovernepal.transportation;

import java.util.List;

import com.crossovernepal.transportation.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class BookMarkAdapter extends ArrayAdapter<FullRoute> {
	Context mContext;
	Cursor cursor;
	List<FullRoute> outerlist_adpater_this;

	public BookMarkAdapter(Context applicationContext, int textViewResourceId,
			Cursor cur, List<FullRoute> outerlist_adapter) {
		// TODO Auto-generated constructor stub
		super(applicationContext, textViewResourceId, outerlist_adapter);
		this.mContext = applicationContext;
		cursor = cur;
		this.outerlist_adpater_this = outerlist_adapter;

	}

	// getView method is called for each item of ListView
	public View getView(int position, View view, ViewGroup parent) {
		// inflate the layout for each item of listView
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.list_bookmark, null);

		FullRoute single_fullroute = outerlist_adpater_this.get(position);
		// get the reference of textViews
		TextView firsttv = (TextView) view.findViewById(R.id.first);

		// Set the Sender number and smsBody to respective TextViews
		firsttv.setText(single_fullroute.fullroute);

		return view;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}

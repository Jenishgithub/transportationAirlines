package com.crossovernepal.transportation;

import java.util.ArrayList;
import java.util.List;

import com.crossovernepal.transportation.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

public class BookMarkActivity extends ListActivity {
	MySQLiteHelper dbHelper;
	Cursor cur;
	ProgressBar progressBar;
	BookMarkAdapter adapter1;

	List<FullRoute> outerlist = new ArrayList<FullRoute>();
	ImageButton backBtnBookmarks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_mark);
		progressBar = (ProgressBar) findViewById(R.id.pBar);

		backBtnBookmarks = (ImageButton) findViewById(R.id.backBtnBookmarks);
		backBtnBookmarks.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				finish();
			}
		});
		dbHelper = new MySQLiteHelper(getApplicationContext());
		new getBookmarks().execute();
	}

	public class getBookmarks extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			String query2 = "select " + tablehelper.TransportationColumns.ID
					+ " , " + tablehelper.TransportationColumns.ROUTE_ID
					+ " , " + tablehelper.RouteColumns.FULL_ROUTE + ", "
					+ tablehelper.TransportationColumns.ARRIVAL_TIME + " from "
					+ tablehelper.TransportationColumns.TABLENAME + " JOIN "
					+ tablehelper.RouteColumns.TABLENAME + " ON "
					+ tablehelper.TransportationColumns.ROUTE_ID + " = "
					+ tablehelper.RouteColumns.ID + " JOIN "
					+ tablehelper.BookmarksColumns.TABLENAME + " ON "
					+ tablehelper.BookmarksColumns.TRANSPORTATION_ID + " = "
					+ tablehelper.TransportationColumns.ID;

			cur = dbHelper.getData(query2);
			int row_count = cur.getCount();
			Log.d("freak", "no. of bookmarks " + row_count);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);
			if (cur.getCount() == 0) {

			} else {

				outerlist = new ArrayList<FullRoute>();
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {

					outerlist.add(new FullRoute(cur.getString(cur
							.getColumnIndex("full_route"))));

					cur.moveToNext();
				}

				adapter1 = new BookMarkAdapter(getApplicationContext(),
						R.layout.list_bookmark, cur, outerlist);

				ListView lv = getListView();
				lv.setAdapter(adapter1);
			}
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		super.onListItemClick(l, v, position, id);
		final int p = position;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				BookMarkActivity.this);

		alertDialogBuilder.setMessage("Choose");
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						cur.moveToPosition(p);
						// -------------------
						String tr_id_p = cur.getString(cur.getColumnIndex("id"));
						Log.d("crossover", "transportaionid of this position "
								+ tr_id_p);

						// ----------------------
						outerlist.remove(p);
						SQLiteDatabase db = dbHelper.getWritableDatabase();

						String quer_findid = "delete from bookmarks where transportation_id="
								+ tr_id_p + ";";

						db.execSQL(quer_findid);
						new getBookmarks().execute();

						adapter1.notifyDataSetChanged();
						adapter1.notifyDataSetInvalidated();
					}
				});

		alertDialogBuilder.setNegativeButton("Show full path",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						cur.moveToPosition(p);

						String tr_id_p = cur.getString(cur.getColumnIndex("id"));
						Log.d("crossover", "transportaionid of this position "
								+ tr_id_p);

						String tr_id = cur.getString(cur.getColumnIndex("id"));
						String route_id = cur.getString(cur
								.getColumnIndex("route_id"));

						String arrived_time = cur.getString(cur
								.getColumnIndex("arrival_time"));

						Intent intent = new Intent(getApplicationContext(),
								SearchDetailActivityWithMap.class);

						intent.putExtra("use_arrivedtime", arrived_time);
						intent.putExtra("id", tr_id);
						intent.putExtra("key_route_id", route_id);
						startActivity(intent);

					}
				});
		AlertDialog alertdialog = alertDialogBuilder.create();
		alertdialog.show();

	}

}

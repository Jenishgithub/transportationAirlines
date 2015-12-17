package com.crossovernepal.transportation;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.modellingpackages.Company;
import com.modellingpackages.Node;
import com.modellingpackages.Route;
import com.modellingpackages.Route_node;
import com.modellingpackages.Schedule;
import com.modellingpackages.Transportation;
import com.modellingpackages.Transportation_details;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;

@SuppressLint("SimpleDateFormat")
public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;

	public final String TAG_BOOKMARKS = "bookmarks";
	public final String TAG_CATEGORY = "category";
	public final String TAG_COMPANY = "company";
	public final String TAG_DISTRICT = "district";
	public final String TAG_LANGUAGE = "language";
	public final String TAG_NODE = "node";
	public final String TAG_ROUTE = "route";
	public final String TAG_ROUTE_NODE = "route_node";
	public final String TAG_SERVICE = "service";
	public final String TAG_TYPE = "type";
	public final String TAG_TRANSPORTATION = "transportation";
	public final String TAG_SCHEDULE = "schedule";
	public final String TAG_TRANSPORTATION_DETAILS = "transportation_details";

	// for airlines
	public final String TAG_AIRLINES = "airlines";
	public final String TAG_AIRLINES_CLASSES = "airlines_classes";
	// public final String TAG_PRICING = "pricing";
	public final String TAG_TRANSPORTATION_AIRLINE_DETAILS = "transportation_airline_details";

	String DB_PATH = "null";

	private static String DB_NAME = "travelguide.db";
	String sql;

	public MySQLiteHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		DB_PATH = context.getDatabasePath(DB_NAME).getPath();

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase() {

		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH;
			File file = new File(myPath);
			if (file.exists() && !file.isDirectory()) {

				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);
			}
		} catch (SQLiteException e) {

		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("CREATE TABLE IF NOT EXISTS bookmarks(id integer primary key autoincrement,transportation_id integer);");
		db.execSQL("CREATE TABLE category (id integer primary key autoincrement, name text);");
		db.execSQL("CREATE TABLE company(id integer primary key autoincrement ,name text, alias text, address text, phone text);");
		db.execSQL("CREATE TABLE district(id integer primary key autoincrement, name text);");
		db.execSQL("CREATE TABLE language(id integer primary key autoincrement, name text);");
		db.execSQL("CREATE TABLE node (id integer primary key autoincrement, name text, lat text, lon text, specialities text, district_id integer, foreign key(district_id) references district(id));");
		db.execSQL("CREATE TABLE route(id integer primary key autoincrement, name text,number text,full_route text);");
		db.execSQL("CREATE TABLE route_node(id integer primary key autoincrement ,route_id integer, node_id integer,route_count integer, foreign key(route_id) references route(id), foreign key(node_id) references node(id));");
		db.execSQL("CREATE TABLE service(id integer primary key autoincrement, name text);");
		db.execSQL("CREATE TABLE transportation(id integer primary key autoincrement,route_id integer,type_id integer,company_id integer,category_id integer,contact_name text,contact_phone text,depart_time text,arrival_time text,interval text,return_type integer,cost integer,remarks text,foreign key(route_id) references route_node(id), foreign key(type_id) references type(id), foreign key(company_id) references company(id),foreign key(category_id) references category(id));");
		db.execSQL("CREATE TABLE type(id integer primary key autoincrement,name text);");
		db.execSQL("CREATE TABLE schedule (id integer primary key autoincrement, transportation_id integer, schedule_type integer,sun integer,mon integer,tues integer, wednes integer, thurs integer, fri integer, satur integer );");
		db.execSQL("CREATE TABLE transportation_details(id integer primary key autoincrement,transportation_id integer,node_id integer,arrival_time text, depart_time text, details text,day text);");

		// for airlines
		db.execSQL("CREATE TABLE airlines(id integer primary key autoincrement, name text);");
		db.execSQL("CREATE TABLE airlines_classes(id integer primary key autoincrement, airline_id integer, name text, foreign key(airline_id) references airlines(id));");
		// db.execSQL("CREATE TABLE pricing(id integer, airlineclass_id integer, sourceId integer, destId integer, rate integer, foreign key(airlineclass_id) references airlines_classes(id), foreign key(sourceId) references node(id), foreign key(destId) references node(id));");
		db.execSQL("CREATE TABLE transportation_airline_details(id integer, transportation_id integer, airline_id integer, airline_class_id integer, foreign key(transportation_id) references transportation(id), foreign key(airline_id) references airlines(id), foreign key(airline_class_id) references airlines_classes(id))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		dropAllTables(db);
		onCreate(db);

	}

	private void dropAllTables(SQLiteDatabase db) {
		db.execSQL("delete from " + DB_NAME);
	}

	public boolean insertIntoTable(String tableName, ContentValues contentValue) {
		SQLiteDatabase db = this.getWritableDatabase();
		@SuppressWarnings("unused")
		Long id = db.insert(tableName, null, contentValue);

		return true;
	}

	public Cursor getData(String query) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery(query, null);
		return res;
	}

	@SuppressLint("NewApi")
	public Cursor getData(String settingTables, String[] selectcolumns,
			String whereString) {
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();

		_QB.setTables(settingTables);
		_QB.appendWhere(whereString);
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor res = _QB.query(db, selectcolumns, null, null, null, null, null);
		@SuppressWarnings("unused")
		String sql = _QB
				.buildQuery(selectcolumns, null, null, null, null, null);

		return res;
	}

	public int numberOfRows(String tableName) {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, tableName);
		return numRows;
	}

	public boolean updateContact(String tableName, Integer id,
			ContentValues contentValues) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(tableName, contentValues, "id = ? ",
				new String[] { Integer.toString(id) });
		return true;
	}

	public Integer deleteContact(String tableName, Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(tableName, "id = ? ",
				new String[] { Integer.toString(id) });
	}

	public ArrayList<ArrayList<String>> getAllColumnsData(String query)
			throws IOException {

		ArrayList<ArrayList<String>> array_list = new ArrayList<ArrayList<String>>();

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor res = db.rawQuery(query, null);

		res.moveToFirst();
		int count = res.getColumnCount();
		while (res.isAfterLast() == false) {
			ArrayList<String> array_list1 = new ArrayList<String>();

			for (int i = 0; i < count; i++) {
				array_list1.add(res.getString(i));
			}
			array_list.add(array_list1);
			res.moveToNext();
		}

		return array_list;

	}

	public int getIdForName(String tablename, String name) {
		int id = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select id from " + tablename
				+ " where name='" + name + "'", null);
		res.moveToFirst();
		@SuppressWarnings("unused")
		int count = res.getColumnCount();

		while (res.isAfterLast() == false) {
			id = res.getInt(0);
			res.moveToNext();
		}

		return id;
	}

	public void createInsertQuery(String tableName, JSONArray jsonarray)
			throws ParseException {

		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < jsonarray.length(); i++) {
			try {
				JSONObject jsonObject = jsonarray.getJSONObject(i);
				HashMap<String, String> hashMap = new HashMap<String, String>();
				Iterator<?> keys = jsonObject.keys();
				ContentValues contentValue = new ContentValues();

				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = jsonObject.getString(key).trim();
					hashMap.put(key, value);
					contentValue.put(key, value);
				}

				if (tableName == TAG_TRANSPORTATION) {
					String arrival = contentValue.getAsString("arrival_time");
					String depart = contentValue.getAsString("depart_time");
					Date departdate = simpledateformat.parse(depart);
					Date arrivaldate = simpledateformat.parse(arrival);
					long difference = arrivaldate.getTime()
							- departdate.getTime();

					@SuppressWarnings("unused")
					int interval = (int) difference / 1000;

				}

				// String valofis_delted =
				// contentValue.getAsString("is_deleted");

				// if (valofis_delted.equals("0")) {

				contentValue.remove("added_date");
				contentValue.remove("is_deleted");
				insertIntoTable(tableName, contentValue);

				// }

				arrayList.add(hashMap);

			} catch (JSONException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean checkIfDataExists(String query) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery(query, null);
		res.moveToFirst();
		int count = res.getCount();

		if (count > 0)
			return true;
		else
			return false;

	}

	public void insertRoute_node(String tableName) {
		// TODO Auto-generated method stub

		sql = "insert into route_node (id, route_id, node_id, route_count) values (?, ?, ?, ?);";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		for (Route_node node : SplashActivity.transportInfo.getRoute_node()) {
			stmt.bindString(1, node.getId());
			stmt.bindString(2, node.getRoute_id());
			stmt.bindString(3, node.getNode_id());
			stmt.bindString(4, node.getRoute_count());
			stmt.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();

	}

	public void insertTransportation_details(String tableName) {

		sql = "insert into transportation_details (id, transportation_id, node_id, arrival_time, depart_time, details, day) values (?, ?, ?, ?, ?, ?, ?);";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		for (Transportation_details node : SplashActivity.transportInfo
				.getTransportation_details()) {
			stmt.bindString(1, node.getId());
			stmt.bindString(2, node.getTransportation_id());
			stmt.bindString(3, node.getNode_id());
			stmt.bindString(4, node.getArrival_time());
			stmt.bindString(5, node.getDepart_time());
			stmt.bindString(6, node.getDetails());
			stmt.bindString(7, node.getDay());
			// stmt.bindString(7, node.getIs_deleted());
			stmt.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void insertNode(String tableName) {

		sql = "insert into node (id, name, lat, lon, specialities, district_id) values (?, ?, ?, ?, ?, ?);";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		for (Node node : SplashActivity.transportInfo.getNode()) {
			stmt.bindString(1, node.getId());
			stmt.bindString(2, node.getName());
			stmt.bindString(3, node.getLat());
			stmt.bindString(4, node.getLon());
			stmt.bindString(5, node.getSpecialities());
			stmt.bindString(6, node.getDistrict_id());
			// stmt.bindString(7, node.getIs_deleted());
			stmt.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void insertRoute(String tableName) {

		sql = "insert into route (id, name, number, full_route) values (?, ?, ?, ?);";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		for (Route node : SplashActivity.transportInfo.getRoute()) {
			stmt.bindString(1, node.getId());
			stmt.bindString(2, node.getName());
			stmt.bindString(3, node.getNumber());
			stmt.bindString(4, node.getFull_route());
			stmt.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();

	}

	public void insertTransportation(String tableName) {

		sql = "insert into transportation (id, route_id, type_id, company_id, category_id, contact_name, contact_phone, depart_time, arrival_time, interval, return_type, cost, remarks) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		for (Transportation node : SplashActivity.transportInfo
				.getTransportation()) {
			stmt.bindString(1, node.getId());
			stmt.bindString(2, node.getRoute_id());
			stmt.bindString(3, node.getType_id());
			stmt.bindString(4, node.getCompany_id());
			stmt.bindString(5, node.getCategory_id());
			stmt.bindString(6, node.getContact_name());
			stmt.bindString(7, node.getContact_phone());
			stmt.bindString(8, node.getDepart_time());
			stmt.bindString(9, node.getArrival_time());
			stmt.bindString(10, "");
			stmt.bindString(11, node.getReturn_type());
			stmt.bindString(12, node.getCost());
			stmt.bindString(13, node.getRemarks());
			// stmt.bindString(7, node.getIs_deleted());
			stmt.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void insertCompany(String tableName) {

		sql = "insert into company (id, name, alias, address, phone) values (?, ?, ?, ?, ?);";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		for (Company node : SplashActivity.transportInfo.getCompany()) {
			stmt.bindString(1, node.getId());
			stmt.bindString(2, node.getName());
			stmt.bindString(3, node.getAlias());
			stmt.bindString(4, node.getAddress());
			stmt.bindString(5, node.getPhone());

			// stmt.bindString(7, node.getIs_deleted());
			stmt.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void insertSchedule(String tableName) {

		sql = "insert into schedule (id, transportation_id, schedule_type, sun, mon, tues, wednes, thurs, fri, satur) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		for (Schedule node : SplashActivity.transportInfo.getSchedule()) {
			stmt.bindString(1, node.getId());
			stmt.bindString(2, node.getTransportation_id());
			stmt.bindString(3, node.getSchedule_type());
			stmt.bindString(4, node.getSun());
			stmt.bindString(4, node.getMon());
			stmt.bindString(4, node.getTues());
			stmt.bindString(4, node.getWednes());
			stmt.bindString(4, node.getThurs());
			stmt.bindString(4, node.getFri());
			stmt.bindString(4, node.getSatur());
			stmt.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

}

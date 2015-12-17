package com.crossovernepal.transportation;

import android.util.Log;

public class shared {
	public static String fromString = "";
	public static String toString = "";
	public static String through1String = "";
	public static String through2String = "";
	public static String through3String = "";
	public static String fromPrice = "";
	public static String toPrice = "";
	public static String departureDate = "";
	public static String vehicle = "all";
	public static String day = "";
	public static String return_type = "";

	// airlines classes yeti budha sita tara agni
	public static String airline_class = "";

	public static String YETICLASS1 = "Class A Yeti";
	public static String YETICLASS2 = "Class B Yeti";
	public static String YETICLASS3 = "Class C Yeti";
	public static String YETICLASS4 = "Class D Yeti";
	public static String YETICLASS5 = "Class E Yeti";

	public static String BUDDHACLAS1 = "Class A Buddha";
	public static String BUDDHACLAS2 = "Class B Buddha";
	public static String BUDDHACLAS3 = "Class C Buddha";
	public static String BUDDHACLAS4 = "Class D Buddha";
	public static String BUDDHACLAS5 = "Class E Buddha";

	public static String SITACLASS1 = "Class A Sita";
	public static String SITACLASS2 = "Class B Sita";
	public static String SITACLASS3 = "Class C Sita";
	public static String SITACLASS4 = "Class D Sita";
	public static String SITACLASS5 = "Class E Sita";

	public static String TARACLASS1 = "Class A Tara";
	public static String TARACLASS2 = "Class B Tara";
	public static String TARACLASS3 = "Class C Tara";
	public static String TARACLASS4 = "Class D Tara";
	public static String TARACLASS5 = "Class E Tara";

	public static String AGNICLASS1 = "Class A Agni";
	public static String AGNICLASS2 = "Class B Agni";
	public static String AGNICLASS3 = "Class C Agni";
	public static String AGNICLASS4 = "Class D Agni";
	public static String AGNICLASS5 = "Class E Agni";

	public static void print() {
		Log.d("test", "fromString=" + fromString);
		Log.d("test", "toString=" + toString);
		Log.d("test", "through1String=" + through1String);
		Log.d("test", "through2String=" + through2String);
		Log.d("test", "through3String=" + through3String);
		Log.d("test", "fromPrice=" + fromPrice);
		Log.d("test", "toPrice=" + toPrice);
		Log.d("test", "departureDate=" + departureDate);
		Log.d("test", "vehicle = " + vehicle);
		Log.d("test", "day = " + day);

	}
}

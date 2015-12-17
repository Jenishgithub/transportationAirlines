package com.crossovernepal.transportation;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	Context _context;
	SharedPreferences prefs;
	public static final String MyPREFERENCES = "MyPrefs";
	int PRIVATE_MODE = 0;
	public static final String KEY_DISTCOST = "DistCost";
	public static final String KEY_DISTCOSTVAL = "DistCostVal";
	public static final String KEY_NODELIST = "node_list";
	// Editor for Shared preferences
	Editor editor, editor2, editor3;

	public SessionManager(Context context) {
		this._context = context;
		prefs = _context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
		editor = prefs.edit();
		editor2 = prefs.edit();
		editor3 = prefs.edit();
	}

	public boolean checkDistCost() {
		return prefs.getBoolean(KEY_DISTCOST, false);
	}

	public void saveDistCost(boolean check) {
		editor.putBoolean(KEY_DISTCOST, true);
		editor.commit();
	}

	public String getDistCostVal() {
		return prefs.getString(KEY_DISTCOSTVAL, (new JSONObject()).toString());
	}

	public void saveDistCostVal(String jsonString) {
		editor2.putString(KEY_DISTCOSTVAL, jsonString);
		editor2.commit();
	}

	public String getNodeNames() {
		return prefs.getString(KEY_NODELIST, null);
	}

	public void saveNodeNames(String nodes_str) {
		editor3.putString(KEY_NODELIST, nodes_str);
		editor3.commit();

	}

}

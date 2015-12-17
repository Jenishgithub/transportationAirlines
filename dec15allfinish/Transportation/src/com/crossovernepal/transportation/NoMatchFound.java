package com.crossovernepal.transportation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class NoMatchFound extends Activity {
	public LinearLayout llShowAlternativePath;
	public Button btnShowAlternativeRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nomatchfound);
		llShowAlternativePath = (LinearLayout) findViewById(R.id.llShowAlternativePath);
		btnShowAlternativeRoute = (Button) findViewById(R.id.btnShowAlternativeRoute);

		btnShowAlternativeRoute.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				// startActivity(new Intent(getApplicationContext(),
				// AlternativeRoute.class));
				// startActivity(new Intent(getApplicationContext(),
				// AlternativeRouteSecond.class));
				startActivity(new Intent(getApplicationContext(),
						AlternativeRouteThirdDijkstra.class));

			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		HomeActivity.mFrameLayout.setVisibility(View.GONE);

		setResult(2);
		super.onPause();
		finish();
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

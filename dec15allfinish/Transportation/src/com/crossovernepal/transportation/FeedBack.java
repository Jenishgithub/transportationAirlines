package com.crossovernepal.transportation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;

import android.content.Context;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class FeedBack extends ActionBarActivity implements OnClickListener {
	EditText etyourEmail, etyourFeedback, etyourName;
	Button btnSendfeedback, btnCancelFeedback;
	String stryourEmail, strYourFeedback, stryourName;
	ImageButton backBtnFeedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.feedback_actionbar);

		etyourEmail = (EditText) findViewById(R.id.etyourEmail);
		etyourFeedback = (EditText) findViewById(R.id.etyourFeedback);
		etyourName = (EditText) findViewById(R.id.etyourName);
		btnSendfeedback = (Button) findViewById(R.id.btnSendfeedback);
		btnCancelFeedback = (Button) findViewById(R.id.btnCancelFeedback);
		backBtnFeedback = (ImageButton) findViewById(R.id.backBtnFeedback);

		btnSendfeedback.setOnClickListener(this);
		btnCancelFeedback.setOnClickListener(this);
		backBtnFeedback.setOnClickListener(this);
		clearErrorsInEdittextFields();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	private void clearErrorsInEdittextFields() {
		// TODO Auto-generated method stub
		etyourName.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View rv, boolean hasFocus) {

				etyourName.setError(null);
				etyourEmail.setError(null);
				etyourFeedback.setError(null);
				if (hasFocus) {
					if (etyourName.getText().length() != 0) {

						etyourName.setError(null);
					}

				}

			}
		});
		etyourEmail.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View rv, boolean hasFocus) {

				etyourName.setError(null);
				etyourEmail.setError(null);
				etyourFeedback.setError(null);

			}
		});
		etyourFeedback.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View rv, boolean hasFocus) {

				etyourName.setError(null);
				etyourEmail.setError(null);
				etyourFeedback.setError(null);

			}
		});

		etyourName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etyourName.setError(null);
			}
		});
		etyourEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etyourEmail.setError(null);
			}
		});
		etyourFeedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etyourFeedback.setError(null);
			}
		});
	}

	public class DoNetworkOperations extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			InputStream is = null;

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// adding string variables inside nameValuePair
			nameValuePairs
					.add(new BasicNameValuePair("user_name", stryourName));
			nameValuePairs.add(new BasicNameValuePair("user_email",
					stryourEmail));
			nameValuePairs.add(new BasicNameValuePair("user_feedback",
					strYourFeedback));

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						"http://demo.crossovernepal.com/transportation/admin/feedback");
				// passing the nameValuePairs inside the httpPost
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// Getting the repsonse
				HttpResponse response = httpClient.execute(httpPost);

				// setting up the entity
				HttpEntity entity = response.getEntity();

				is = entity.getContent();

				runOnUiThread(new Runnable() {
					public void run() {
						String msg = "Feedback sent succesfully";
						Toast.makeText(getApplicationContext(), msg,
								Toast.LENGTH_LONG).show();
						etyourEmail.setText("");
						etyourName.setText("");
						etyourFeedback.setText("");
					}
				});

			} catch (ClientProtocolException e) {
				// TODO: handle exception
			} catch (IOException e) {
				Log.e("Log_tag", "IOException");
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "Not inserted",
								Toast.LENGTH_LONG).show();
					}
				});

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSendfeedback:
			// if (isOnline()) {
			stryourEmail = etyourEmail.getText().toString().trim();
			strYourFeedback = etyourFeedback.getText().toString().trim();
			stryourName = etyourName.getText().toString().trim();

			if (stryourName.length() != 0) {
				if (stryourEmail.length() != 0) {
					if (strYourFeedback.length() != 0) {
						new DoNetworkOperations().execute();
					} else {
						etyourFeedback.setError("Give your Feedback!");
					}
				} else {
					etyourEmail.setError("Give your Email");
				}

			} else {
				etyourName.setError("Give your Name!");
			}
			// } else {
			// Toast.makeText(getApplicationContext(),
			// "!!!No network available.", Toast.LENGTH_SHORT).show();
			// }
			break;

		case R.id.btnCancelFeedback:
			finish();
			break;

		case R.id.backBtnFeedback:
			finish();
			break;

		default:
			break;
		}
	}

	private boolean isOnline() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}
}
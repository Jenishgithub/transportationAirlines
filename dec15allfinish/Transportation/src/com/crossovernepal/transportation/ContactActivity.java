package com.crossovernepal.transportation;

import com.crossovernepal.transportation.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends ActionBarActivity {
	ImageButton backBtnContact;
	EditText etSub, etTo, etCcbcc, etBody;
	LinearLayout llcontact;
	String email, subject, ccbcc, body;
	TextView send, cancel;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		setContentView(R.layout.contactus);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.contact_actionbar);

		etTo = (EditText) findViewById(R.id.etTo);
		etSub = (EditText) findViewById(R.id.etSubject);
		etCcbcc = (EditText) findViewById(R.id.etCcBcc);
		etBody = (EditText) findViewById(R.id.etMessageBody);
		llcontact = (LinearLayout) findViewById(R.id.llShowHide);
		send = (TextView) findViewById(R.id.tvSend);
		cancel = (TextView) findViewById(R.id.tvCancel);
		send.setClickable(true);
		cancel.setClickable(true);

		backBtnContact = (ImageButton) findViewById(R.id.backBtnContact);
		backBtnContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				email = etTo.getText().toString();
				ccbcc = etCcbcc.getText().toString();
				subject = etSub.getText().toString();
				body = etBody.getText().toString();
				String emailarray[] = { email };
				Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						emailarray);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						subject);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
				try {
					startActivity(emailIntent);
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(
							getApplicationContext(),
							"No email client applications installed in your device",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}
}

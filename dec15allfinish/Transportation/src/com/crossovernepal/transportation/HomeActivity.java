package com.crossovernepal.transportation;

import com.crossovernepal.transportation.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

@SuppressLint("NewApi")
public class HomeActivity extends ActionBarActivity implements
		OnMenuItemClickListener {

	ViewPagerAdapter mAdapter;
	public static ViewPager mPager;

	public static FrameLayout mFrameLayout;

	Menu menu;

	public static ImageButton backBtn;

	public static TextView titleTextView;
	public static ImageButton btnCustomMenuBar;
	public static LinearLayout pagerLayout;

	PopupMenu popupMenu;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.home_actionbar);

		backBtn = (ImageButton) findViewById(R.id.backBtn);

		btnCustomMenuBar = (ImageButton) findViewById(R.id.btnCustomMenuBar);

		titleTextView = (TextView) findViewById(R.id.tvTitle);

		backBtn.setVisibility(View.INVISIBLE);

		mFrameLayout = (FrameLayout) findViewById(R.id.container);

		// Instantiating the adapter
		mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

		// instantiate the Views
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		btnCustomMenuBar.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showPopupMenu(HomeActivity.this, view);
			}
		});

		setTab();

	}

	private void showPopupMenu(Context context, View view) {
		if (popupMenu == null) {
			popupMenu = new PopupMenu(context, view);
			popupMenu.inflate(R.menu.popup_menu);
			popupMenu.setOnMenuItemClickListener(HomeActivity.this);
		}
		popupMenu.show();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			showPopupMenu(this, btnCustomMenuBar);
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	private void setTab() {
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:

					backBtn.setVisibility(View.INVISIBLE);

					titleTextView.setText("HOME");
					mFrameLayout.setVisibility(View.GONE);
					break;

				}

			}

		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (HomeFragment.popupWindowInner != null
				&& HomeFragment.popupWindowInner.isShowing()) {
			HomeFragment.popupWindowInner.dismiss();
			HomeFragment.popupWindow.showAtLocation(HomeFragment.plane,
					Gravity.CENTER, -20, 10);
		} else if (HomeFragment.popupWindow != null
				&& HomeFragment.popupWindow.isShowing()) {
			HomeFragment.popupWindow.dismiss();
			HomeFragment.iSplaneClicked = false;
		} else {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					HomeActivity.this);

			// set dialog message
			alertDialogBuilder
					.setMessage("Do you want to exit the application?")
					.setCancelable(false)
					.setPositiveButton("Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									finish();

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

		}

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.item_contactUs:
			startActivity(new Intent(getApplicationContext(),
					ContactActivity.class));

			break;
		case R.id.item_faq:
			startActivity(new Intent(getApplicationContext(), FAQActivity.class));
			break;
		case R.id.item_checkupdate:

			startActivity(new Intent(this.getApplicationContext(),
					UpdataActivity.class));

			break;
		case R.id.item_bookmarks:
			startActivity(new Intent(getApplicationContext(),
					BookMarkActivity.class));
			break;
		case R.id.item_feedback:
			startActivity(new Intent(getApplicationContext(), FeedBack.class));
			break;

		default:
			break;
		}

		return true;
	}

}

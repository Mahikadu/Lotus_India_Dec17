package com.prod.sudesi.lotusherbalsnew;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import libs.AlarmManagerBroadcastReceiver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;
import com.prod.sudesi.lotusherbalsnew.R;

import com.sudesi.tester.TesterSubmitActivity;

import dbConfig.Dbcon;
import libs.ExceptionHandler;

public class DashboardNewActivity extends Activity {

	private ListView lvMenu;
	private String[] lvMenuItems;
	Button btMenu;
	TextView tvTitle;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ViewFlipper mViewFlipper;
	private AnimationListener mAnimationListener;
	private Context mContext;
	
	Fragment fragment;

	String selectedItem;
	String currentItem;

	// today
	private Dbcon db = null;
	String[] values;
	String username;

	SharedPreferences sp;
	SharedPreferences.Editor spe;
	private double lon = 0.0, lat = 0.0;
	String attendanceDate = "", attendmonth;
	String yesterdaydate1 = "";

	Button btn_attendance, btn_stock, btn_tester, btn_visibility,
			btn_notification, btn_reports, btn_datasync, btn_BAreport,
			btn_BAMonthreport, btn_sale, btn_dashboard,btn_super_atten;

	TextView tv_h_username;
	Button btn_home, btn_logout;

	private AlarmManagerBroadcastReceiver alarm;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_dashboard_new);

		//////////Crash Report
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		mContext = getApplicationContext();
		db = new Dbcon(mContext);
		db.open();

		sp = mContext.getSharedPreferences("Lotus", Context.MODE_PRIVATE);
		spe = sp.edit();

		btn_attendance = (Button) findViewById(R.id.btn_atten);
		btn_visibility = (Button) findViewById(R.id.btn_visibility);
		btn_stock = (Button) findViewById(R.id.btn_stock);
		btn_tester = (Button) findViewById(R.id.btn_tester);
		btn_reports = (Button) findViewById(R.id.btn_report);
		btn_notification = (Button) findViewById(R.id.btn_notifi);
		btn_datasync = (Button) findViewById(R.id.btn_master_sync);
		btn_BAreport = (Button) findViewById(R.id.btn_ba_sale_yr);
		btn_sale = (Button) findViewById(R.id.btn_sale);
		btn_super_atten = (Button) findViewById(R.id.btn_super_atten);

		btn_BAMonthreport = (Button) findViewById(R.id.btn_ba_sale_month_wise);

		btn_dashboard = (Button) findViewById(R.id.btn_dashboard);

		alarm = new AlarmManagerBroadcastReceiver();

		tv_h_username = (TextView) findViewById(R.id.tv_h_username);
		btn_home = (Button) findViewById(R.id.btn_home);
		btn_logout = (Button) findViewById(R.id.btn_logout);

		btn_home.setVisibility(View.INVISIBLE);
		Log.e("db.checkStockUploaded()", String.valueOf(db.checkStockUploaded()));
		
		if(db.checkStockUploaded())
		{
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					DashboardNewActivity.this);

			// set title
			alertDialogBuilder.setTitle("SYNC DATA ALERT");

			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Kindly do a Data Upload")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			
			
			btn_visibility.setClickable(false);
			btn_stock.setClickable(false);
			btn_sale.setClickable(false);
			
			btn_visibility.setEnabled(false);
			btn_stock.setEnabled(false);
			btn_sale.setEnabled(false);
		}
		
		

		btn_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		btn_tester.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// startActivity(new Intent(getApplicationContext(),
				// TesterFragment.class));

				startActivity(new Intent(getApplicationContext(),
						TesterSubmitActivity.class));

			}
		});

		btn_stock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						StockNewActivity.class));

			}
		});

		btn_reports.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						ReportsForUser.class));

			}
		});
		btn_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						NotificationFragment.class));

			}
		});

		btn_visibility.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						VisibilityFragment.class));

			}
		});

		btn_datasync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						SyncMaster.class));

			}
		});
		btn_attendance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						AttendanceFragment.class);
				i.putExtra("FromLoginpage", "");
				startActivity(i);

				// startActivity(new Intent(getApplicationContext(),
				// AttendanceFragment.class));

			}
		});

		btn_BAreport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						BAYearWiseReport.class);

				startActivity(i);

				// startActivity(new Intent(getApplicationContext(),
				// AttendanceFragment.class));

			}
		});

		btn_BAMonthreport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						BAMonthWiseReport.class);

				startActivity(i);

				// startActivity(new Intent(getApplicationContext(),
				// AttendanceFragment.class));

			}
		});

		btn_sale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						SaleNewActivity.class));

			}
		});

		btn_dashboard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						BocDashBoardActivity.class));

			}
		});

		btn_super_atten.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				startActivity(new Intent(getApplicationContext(),
						SupervisorAttendance.class));
			}
		});
		
		
		try {
			username = sp.getString("username", "");
			tv_h_username.setText(username);
			yesterdaydate1 = getYesterdayDateString();

			String getmonth1 = getmonthforinsert(yesterdaydate1);

			Log.v("", "getmonth" + getmonth1);

			Cursor c = null;
			db.open();
			c = db.getpreviousData(yesterdaydate1, username);

			// (int i = 0;i<c.getCount();i++){

			Log.v("", "c.getcount=" + c.getCount());

			if (c != null && c.getCount() > 0) {
				db.close();

			} else {
				db.close();
				// insertappsent(yesterdaydate1,getmonth1);

			}

			// }
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static boolean beforedatevalidate(String selecteddate,
			String currentdate) {
		boolean result = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date selectdate = sdf.parse(selecteddate);
			Date curntdate = sdf.parse(currentdate);

			Log.e("", "selecteddate=" + selecteddate);
			Log.e("", "currentdate=" + currentdate);
			// Log.v("befordatevalidation",""+selecteddate.compareTo(currentdate));
			// Log.v("befordatevalidation",""+selectdate.before(curntdate));
			if (selectdate.before(curntdate)) {

				Log.v("befordatevalidation", "" + result);
				result = true;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	@SuppressLint("SimpleDateFormat")
	private String getYesterdayDateString() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		return dateFormat.format(cal.getTime());
	}

	private void insertappsent(String yesterdaydate, String month) {

		try {
			final String[] columns = new String[] { "emp_id", "Adate",
					"attendance", "lat", "lon", "savedServer", "month",
					"holiday_desc" };
			db.open();

			values = new String[] { username, yesterdaydate, "A",
					String.valueOf(lat), String.valueOf(lon), "0", month, "" };
			Log.v("", "values for insert==" + values);
			db.insert(values, columns, "attendance");

			db.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void refreshDisplay() {
		refreshDisplay(new LocationInfo(mContext));
	}

	private void refreshDisplay(final LocationInfo locationInfo) {
		if (locationInfo.anyLocationDataReceived()) {
			lat = locationInfo.lastLat;
			lon = locationInfo.lastLong;
			Log.e("Longitude", String.valueOf(lon));
			Log.e("Latitude", String.valueOf(lat));
			// Toast.makeText(context, "Location Updated",
			// Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(mContext,
					"Unable to get GPS location! Try again later!!",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		// cancel any notification we may have received from
		// TestBroadcastReceiver

		startRepeatingTimer();// ------------------------------------

		((NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1234);

		refreshDisplay();

		// This demonstrates how to dynamically create a receiver to listen to
		// the location updates.
		// You could also register a receiver in your manifest.
		final IntentFilter lftIntentFilter = new IntentFilter(
				LocationLibraryConstants
						.getLocationChangedPeriodicBroadcastAction());
		mContext.registerReceiver(lftBroadcastReceiver, lftIntentFilter);
	}

	private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// extract the location info in the broadcast
			final LocationInfo locationInfo = (LocationInfo) intent
					.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
			// refresh the display with it
			refreshDisplay(locationInfo);
		}
	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		//
		// startActivity(new
		// Intent(getApplicationContext(),DashboardNewActivity.class));
		// FragmentManager fm = DashboardNewActivity.this
		// .getSupportFragmentManager();
		// fm.popBackStack();

	}

	public String getmonthforinsert(String yesterd) {

		Log.v("", "date-ingetmonthfuction=" + yesterd);
		if (yesterd != null) {
			if (yesterd.contains("-")) {
				String d[] = yesterd.split("-");

				// attendanceDate= d[0]+"-"+getmonthNo(d[1])+"-"+d[2];
				Log.v("", "ingetmonthfuction =" + d[1].toString());
				attendmonth = getmonthNo(d[1]);

			}
		}

		return attendmonth;
	}

	/*
	 * private String getYesterdayDateString1(String yesterdaydate12) {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return yesterdaydate12; // TODO Auto-generated method stub
	 * 
	 * }
	 */

	public String getmonthNo(String monthName) {
		String month = "";

		if (monthName.equals("01")) {
			month = "1";
		} else if (monthName.equals("02")) {
			month = "2";
		} else if (monthName.equals("03")) {
			month = "3";
		} else if (monthName.equals("04")) {
			month = "4";
		} else if (monthName.equals("05")) {
			month = "5";
		} else if (monthName.equals("06")) {
			month = "6";
		} else if (monthName.equals("07")) {
			month = "7";
		} else if (monthName.equals("08")) {
			month = "8";
		} else if (monthName.equals("09")) {
			month = "9";
		} else if (monthName.equals("10")) {
			month = "10";
		} else if (monthName.equals("11")) {
			month = "11";
		} else if (monthName.equals("12")) {
			month = "12";
		}

		return month;
	}

	public void startRepeatingTimer() {
		Context context = this.getApplicationContext();

		if (alarm != null) {
			alarm.SetAlarm(context);
		} else {
			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
		}

	}

}

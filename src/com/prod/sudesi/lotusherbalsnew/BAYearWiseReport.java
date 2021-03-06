package com.prod.sudesi.lotusherbalsnew;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import libs.ConnectionDetector;
import libs.ExceptionHandler;
import libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.R;
import com.sudesi.adapter.BAReportAdapter;

public class BAYearWiseReport extends Activity {
	
	SharedPreferences shp;
	SharedPreferences.Editor shpeditor;
	
	Context context;
	
	private ProgressDialog prgdialog;
	
	 private BAReportAdapter adapter;
	
	LotusWebservice service;
	
	ListView lv_ba_report;
	
	HorizontalScrollView horizantalscrollviewforbareport;
	
	static public ArrayList<HashMap<String, String>> todaymessagelist = new ArrayList<HashMap<String, String>>();
	
	TextView tv_h_username;
	Button btn_home,btn_logout;
	String username,bdename;
	
	TextView tv_current_year_n1,tv_current_year_n2,tv_previous_year_p1,tv_previous_year_p2;
	
	String current_year_n1,current_year_n2,previous_year_p1,previous_year_p2;
	
	int int_current_year_n1,int_current_year_n2,int_previous_year_p1,int_previous_year_p2;
	
	String current_server_date;
	
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_bayear_wise_report);
		//////////Crash Report
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		
		context = BAYearWiseReport.this;
		
		prgdialog = new ProgressDialog(context);
		service = new LotusWebservice(BAYearWiseReport.this);
		cd = new ConnectionDetector(BAYearWiseReport.this);
		
		shp = context.getSharedPreferences("Lotus", context.MODE_PRIVATE);
		shpeditor = shp.edit();
		
		lv_ba_report = (ListView)findViewById(R.id.listView_ba_year_report);

		tv_h_username = (TextView)findViewById(R.id.tv_h_username);
		btn_home = (Button)findViewById(R.id.btn_home);
		btn_logout =(Button)findViewById(R.id.btn_logout);
		
		username = shp.getString("username", "");
		Log.v("","username=="+username);

		bdename = shp.getString("BDEusername","");

		tv_h_username.setText(bdename);
		
		btn_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new  Intent(getApplicationContext(), LoginActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);	
			}
		});
		
		btn_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new  Intent(getApplicationContext(), DashboardNewActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);	
			//startActivity(new Intent(getApplicationContext(), DashboardNewActivity.class));	
			}
		});
		//---------------------
		
		try{
			new GetBAreport().execute();	
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		
		horizantalscrollviewforbareport =(HorizontalScrollView)findViewById(R.id.horizantal_scrollview_ba_report);
		
		horizantalscrollviewforbareport.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				int scrollX = v.getScrollX();
				int scrollY = v.getScrollY();

				horizantalscrollviewforbareport.scrollTo(scrollX, scrollY);
				return false;
			}
		});
		
		
		tv_current_year_n1 = (TextView)findViewById(R.id.tv_bar_Current_year_n1);
		tv_current_year_n2 = (TextView)findViewById(R.id.tv_bar_Current_year_n2);
		tv_previous_year_p1 = (TextView)findViewById(R.id.tv_bar_Previous_year_p1);
		tv_previous_year_p2 = (TextView)findViewById(R.id.tv_bar_Previous_year_p2);
		
		current_year_n1 = tv_current_year_n1.getText().toString();
		current_year_n2 = tv_current_year_n2.getText().toString();
		
		previous_year_p1 = tv_previous_year_p1.getText().toString();
		previous_year_p2 = tv_previous_year_p2.getText().toString();
		
		//int_current_year_n2 = Integer.parseInt(current_year_n2);
		try{
		current_year_n2 = shp.getString("current_year", "");
		int_current_year_n2 = Integer.parseInt(current_year_n2);
		
		String comparedatewith = current_year_n2+"-03-25";
		
		
		current_server_date = shp.getString("todaydate", "");
		
		Log.v("","current_server_date="+current_server_date);
		
		Log.v("","comparedatewith="+comparedatewith);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;Date date2 = null;
		try {
			date1 = sdf.parse(current_server_date);
			date2 = sdf.parse(comparedatewith);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    		
		if(!current_year_n2.equalsIgnoreCase("") && date1.compareTo(date2)>0){
			
			int int_current_year_n22 =  int_current_year_n2 + 1 ;
			
			String current_year_n2 = String.valueOf(int_current_year_n22);
			
			tv_current_year_n2.setText(current_year_n2);
			
			int_current_year_n1 = int_current_year_n22 - 1;
			
			current_year_n1 = String.valueOf(int_current_year_n1);
			
			tv_current_year_n1.setText(current_year_n1);
			
			
			tv_previous_year_p2.setText(current_year_n1);
			
			int_previous_year_p1 = int_current_year_n1 - 1;
			
			previous_year_p1 = String.valueOf(int_previous_year_p1);
			
			tv_previous_year_p1.setText(previous_year_p1);
			
		}else{
			
			tv_current_year_n2.setText(current_year_n2);
			
			int_current_year_n1 = int_current_year_n2 - 1;
			
			current_year_n1 = String.valueOf(int_current_year_n1);
			
			tv_current_year_n1.setText(current_year_n1);
			
			
			tv_previous_year_p2.setText(current_year_n1);
			
			int_previous_year_p1 = int_current_year_n1 - 1;
			
			previous_year_p1 = String.valueOf(int_previous_year_p1);
			
			tv_previous_year_p1.setText(previous_year_p1);
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public class GetBAreport extends AsyncTask<Void, Void, String>{
		String returnMessage = null;
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			try {
				todaymessagelist.clear();
				
				if (!cd.isConnectingToInternet()) {
					
					returnMessage="1";
				}
				else{
				 returnMessage = getBAreportfromWebservice();//soap messag call
				}
				 
			} catch (Exception e) {
				returnMessage="1";
			 e.getMessage();
			}
			return returnMessage;
			
		}
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			prgdialog.setMessage("Please Wait...");
			prgdialog.show();
			prgdialog.setCancelable(false);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			prgdialog.dismiss();
			
			if(result.equalsIgnoreCase("1")){
				
				Toast.makeText(BAYearWiseReport.this, "Connectivity Error!! Please try again", Toast.LENGTH_SHORT).show();
			}else{
			loadReports();
			}
		}
	}


	private String getBAreportfromWebservice() {
		// TODO Auto-generated method stub
		String flag="0";
try {
	

	SoapObject resultsRequestSOAP=null;

			// //soap call
			Log.e("","not2=="+username);
			 resultsRequestSOAP = service.GetBAOutletSales(username);
			//Log.e("","resultsRequestSOAP=="+resultsRequestSOAP.toString());

			if (resultsRequestSOAP != null) {
				Log.e("","not3");
			//	todaymessagelist.clear();
				Log.e("","not4");
				// soap response
				for (int i = 0; i < resultsRequestSOAP.getPropertyCount(); i++) {
					Log.e("","not5");

					SoapObject getmessaage = (SoapObject) resultsRequestSOAP
							.getProperty(i);
					HashMap<String, String> map = new HashMap<String, String>();

					// display messages by adding it to listview
					//if (!String.valueOf(getmessaage.getProperty("CreatedDate"))
					//		.equals("false")) {
					if(getmessaage !=null){
						
//						map.put("PreviousYearP", String.valueOf(getmessaage.getProperty("PreviousYearP")));
//						
//						map.put("NetAmountP", String.valueOf(getmessaage.getProperty("NetAmountP")));
//						
//						map.put("GrowthP",String.valueOf(getmessaage.getProperty("GrowthP")));
//						
//						map.put("CurrentYearC",String.valueOf(getmessaage.getProperty("CurrentYearC")));
//						
//						map.put("NetAmountC",String.valueOf(getmessaage.getProperty("NetAmountC")));
//						
//						map.put("GrowthC",String.valueOf(getmessaage.getProperty("GrowthC")));
						
						map.put("PreviousYearP", String.valueOf(getmessaage.getProperty("PreviousYearP")));
						
						map.put("SkinAmountP", String.valueOf(getmessaage.getProperty("SkinAmountP")));
						
						map.put("ColorAmountP", String.valueOf(getmessaage.getProperty("ColorAmountP")));
						
						//map.put("GrowthP",String.valueOf(getmessaage.getProperty("GrowthP")));
						map.put("GrowthPSkin",String.valueOf(getmessaage.getProperty("GrowthPSkin")));
						
						map.put("GrowthPColor",String.valueOf(getmessaage.getProperty("GrowthPColor")));
						
						map.put("CurrentYearC",String.valueOf(getmessaage.getProperty("CurrentYearC")));
						
						map.put("SkinAmountC",String.valueOf(getmessaage.getProperty("SkinAmountC")));
						
						map.put("ColorAmountC",String.valueOf(getmessaage.getProperty("ColorAmountC")));
						
						//map.put("GrowthC",String.valueOf(getmessaage.getProperty("GrowthC")));
						map.put("GrowthCSkin",String.valueOf(getmessaage.getProperty("GrowthCSkin")));
						map.put("GrowthCColor",String.valueOf(getmessaage.getProperty("GrowthCColor")));
						
						
						
						todaymessagelist.add(map);
					}
				}
			}else{
				
				//
				flag ="1";
			}

		} catch (Exception e) {
			// TODO: handle exception
			flag="1";
			e.printStackTrace();
		}

return flag;
		
	}
	
	private void loadReports() {
		adapter = new BAReportAdapter(BAYearWiseReport.this, todaymessagelist);
		lv_ba_report.setAdapter(adapter);// add custom adapter to
													// listview
	}
}

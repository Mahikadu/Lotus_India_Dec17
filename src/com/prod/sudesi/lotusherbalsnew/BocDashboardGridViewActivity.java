package com.prod.sudesi.lotusherbalsnew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sudesi.adapter.BADashboardGridViewReportAdapter;

import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import dbConfig.Dbcon;
import libs.ConnectionDetector;
import libs.ExceptionHandler;
import libs.LotusWebservice;

public class BocDashboardGridViewActivity extends Activity {

	GridView gridView;

	Dbcon db;

	ListView listView;
	String month, year;

	TextView tv_h_username;
	Button btn_home, btn_logout;
	String username;

	SharedPreferences shp;
	SharedPreferences.Editor shpeditor;

	Context context;

	String FromDate;
	String ToDate;

	TextView tv_heading_boc;

	RadioGroup rg_BOC;
	RadioButton rb_ALL;
	RadioButton rb_COLOR;
	RadioButton rb_SKIN;

	TextView tv_month_total, tv_month_color, tv_month_skin;
	String Type_radio_button;
	
	GetBAreport getdashboarreportAsync;

	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

	private ProgressDialog prgdialog;
	
	static public ArrayList<HashMap<String, String>> todaymessagelist = new ArrayList<HashMap<String, String>>();
	
	LotusWebservice service;
	
	ConnectionDetector cd;
	 private BADashboardGridViewReportAdapter adapter;
	 
	 GetTotalBAReport getTotalReport;
	 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_boc_grid_view);
		//////////Crash Report
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		// gridView = (GridView) findViewById(R.id.grid_view);
		context = BocDashboardGridViewActivity.this;
		prgdialog = new ProgressDialog(context);
		tv_heading_boc = (TextView) findViewById(R.id.tv_heading_boc);

		tv_h_username = (TextView) findViewById(R.id.tv_h_username);
		btn_home = (Button) findViewById(R.id.btn_home);
		btn_logout = (Button) findViewById(R.id.btn_logout);

		tv_month_total = (TextView) findViewById(R.id.tv_month_total);
		tv_month_color = (TextView) findViewById(R.id.tv_month_color);
		tv_month_skin = (TextView) findViewById(R.id.tv_month_skin);

		shp = context.getSharedPreferences("Lotus", context.MODE_PRIVATE);
		shpeditor = shp.edit();
		
		cd = new ConnectionDetector(context);
		
		service = new LotusWebservice(context);
		

		username = shp.getString("username", "");
		Log.v("", "username==" + username);

		tv_h_username.setText(username);

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

		btn_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						DashboardNewActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				// startActivity(new Intent(getApplicationContext(),
				// DashboardNewActivity.class));
			}
		});

		rg_BOC = (RadioGroup) findViewById(R.id.radioGroup1);
		rb_ALL = (RadioButton) findViewById(R.id.rb_all);
		rb_SKIN = (RadioButton) findViewById(R.id.rb_skin);
		rb_COLOR = (RadioButton) findViewById(R.id.rb_color);

		// Set custom adapter (GridAdapter) to gridview
		listView = (ListView) findViewById(android.R.id.list);
		ArrayList<String> Boc = new ArrayList<String>();

		db = new Dbcon(BocDashboardGridViewActivity.this);

		// db.open();
		// Boc = db.getBocdates();
		// db.close();

		try {
			Log.e("", "month=1");
			Intent i = getIntent();
			month = i.getStringExtra("month");
			Log.e("", "month=" + month);
			year = i.getStringExtra("year");

			Log.e("", "year=" + year);

			tv_heading_boc.setText(month);

			Get_dates_from_to(year, month);
			
			//getlist("create");

//			gettotalformonth();
			
			getTotalReport = new GetTotalBAReport();
			
			getTotalReport.execute();

			rg_BOC.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					try {

						if (rb_ALL.isChecked()) {

							/*
							 * db.open(); data =
							 * db.getBocDatadatewise(month,FromDate
							 * ,ToDate,"ALL"); db.close();
							 */
						Type_radio_button="ALL";
						//	getdashboarreportAsync= new GetBAreport().execute();
							//getlist("ALL");
						
						GetBAreport  aaaaa = new GetBAreport();
						aaaaa.execute();
						}
						if (rb_SKIN.isChecked()) {

							/*
							 * db.open(); data =
							 * db.getBocDatadatewise(month,FromDate
							 * ,ToDate,"SKIN"); db.close();
							 */
							Type_radio_button="SKIN";
							//getdashboarreportAsync= (GetBAreport) new GetBAreport().execute();
							
							//getlist("SKIN");
							GetBAreport  aaaaa = new GetBAreport();
							aaaaa.execute();
							
						}

						if (rb_COLOR.isChecked()) {

							/*
							 * db.open(); data =
							 * db.getBocDatadatewise(month,FromDate
							 * ,ToDate,"COLOR"); db.close();
							 */
							Type_radio_button="COLOR";
						//	getdashboarreportAsync= new GetBAreport().execute();
							GetBAreport  aaaaa = new GetBAreport();
							aaaaa.execute();

							//getlist("COLOR");

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			// Get_dates_from_to(year,month);

			// db.open();
			// data = db.getBocDatadatewise(month,FromDate,ToDate);
			// db.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		// String [] gridArr = Boc.toArray(new String[Boc.size()]);
		// gridView.setAdapter( new CustomGridAdapter( this,gridArr ,data) );

		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// listView.setAdapter(new CustomGridAdapter( this,//gridArr ,
		// data));
	}

	/*protected void getlist(String type) {
		// TODO Auto-generated method stub

		if(type.equalsIgnoreCase("create"))
		{
		
		db.open();
		data = db.getBocDatadatewise(month, FromDate, ToDate, "ALL");
		db.close();
		
		double total=0.0,skin=0.0,color=0.0;

		for(int i=0;i<data.size();i++)
		{
			total =total + Double.parseDouble(data.get(i).get("Total"));
			
			if(data.get(i).get("Type").equalsIgnoreCase("skin"))
			{
				skin = skin + Double.parseDouble(data.get(i).get("Total"));
			}
			 
			if(data.get(i).get("Type").equalsIgnoreCase("color"))
			{
				color = color + Double.parseDouble(data.get(i).get("Total"));
			}
			
			
		}
		
		//tv_month_total.setText(String.valueOf(total));
		
		//tv_month_skin.setText(String.valueOf(skin));
		
		//tv_month_color.setText(String.valueOf(color));
		
		//String total_string = String.valueOf(total);
				
		
		tv_month_total.setText(String.format("%.2f", total));
		
		//String month_skin_string_total = String.valueOf(skin);
		
		tv_month_skin.setText(String.format("%.2f", skin));
		
		//String month_color_string_total = String.valueOf(color);
		
		
		tv_month_color.setText(String.format("%.2f", color));
		
		
		
		}
		else
		{
			db.open();
			data = db.getBocDatadatewise(month, FromDate, ToDate, type);
			db.close();
			
			listView.setAdapter(new CustomGridAdapter(this,// gridArr ,
					data));
			
			
		}
		

	}*/

	private void Get_dates_from_to(String year2, String month2) {
		// TODO Auto-generated method stub
		String yearsp[] = year2.split("-");
		String from_year = yearsp[0];
		String to_year = yearsp[1];

		String bb;

		if (month.equalsIgnoreCase("BOC1")) {

			FromDate = from_year + "-" + "03" + "-" + "26";
			ToDate = from_year + "-" + "04" + "-" + "25";

			// Toast.makeText(context, "boc12 = "+bb, Toast.LENGTH_LONG).show();
		}

		if (month.equalsIgnoreCase("BOC2")) {

			FromDate = from_year + "-" + "04" + "-" + "26";
			ToDate = from_year + "-" + "05" + "-" + "25";

			// Toast.makeText(context, "boc1 = "+bb, Toast.LENGTH_LONG).show();
		}
		if (month.equalsIgnoreCase("BOC3")) {

			FromDate = from_year + "-" + "05" + "-" + "26";
			ToDate = from_year + "-" + "06" + "-" + "25";
			// Toast.makeText(context, "boc2 = "+bb, Toast.LENGTH_LONG).show();
		}
		if (month.equalsIgnoreCase("BOC4")) {

			FromDate = from_year + "-" + "06" + "-" + "26";
			ToDate = from_year + "-" + "07" + "-" + "25";

		}
		if (month.equalsIgnoreCase("BOC5")) {

			FromDate = from_year + "-" + "07" + "-" + "26";
			ToDate = from_year + "-" + "08" + "-" + "25";

		}
		if (month.equalsIgnoreCase("BOC6")) {

			FromDate = from_year + "-" + "08" + "-" + "26";
			ToDate = from_year + "-" + "09" + "-" + "25";
		}
		if (month.equalsIgnoreCase("BOC7")) {

			FromDate = from_year + "-" + "09" + "-" + "26";
			ToDate = from_year + "-" + "10" + "-" + "25";
		}

		if (month.equalsIgnoreCase("BOC8")) {

			FromDate = from_year + "-" + "10" + "-" + "26";
			ToDate = from_year + "-" + "11" + "-" + "25";
		}
		if (month.equalsIgnoreCase("BOC9")) {

			FromDate = from_year + "-" + "11" + "-" + "26";
			ToDate = from_year + "-" + "12" + "-" + "25";
		}
		if (month.equalsIgnoreCase("BOC10")) {

			FromDate = from_year + "-" + "12" + "-" + "26";
			ToDate = to_year + "-" + "01" + "-" + "25";
		}
		if (month.equalsIgnoreCase("BOC11")) {

			FromDate = to_year + "-" + "01" + "-" + "26";
			ToDate = to_year + "-" + "02" + "-" + "25";
		}
		if (month.equalsIgnoreCase("BOC12")) {

			FromDate = to_year + "-" + "02" + "-" + "26";
			ToDate = to_year + "-" + "03" + "-" + "25";
		}

	}

	
	
	public class GetBAreport extends AsyncTask<Void, Void, String>{
		String returnMessage = "";
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			try {
				

				SoapObject resultsRequestSOAP=null;
				
				

						// //soap call
						Log.e("","not2=="+username);
						DateFormat inFormat = new SimpleDateFormat( "yyyy-MM-dd");
						
						Date FromDate_date = inFormat.parse(FromDate);
						
						Date ToDate_date = inFormat.parse(ToDate);
						
//						if(Type_radio_button.equalsIgnoreCase("ALL")){
						
						// resultsRequestSOAP = service.GetDashboradData(0,  FromDate_date,  ToDate_date,  username, Type_radio_button);
							resultsRequestSOAP = service.GetDashboradData("0",  FromDate,  ToDate,  username, "ALL");
						 if (resultsRequestSOAP != null) {
								
								todaymessagelist.clear();
								for (int i = 0; i < resultsRequestSOAP.getPropertyCount(); i++) {
									Log.e("","not5");

									SoapObject getmessaage = (SoapObject) resultsRequestSOAP.getProperty(i);
									HashMap<String, String> map = new HashMap<String, String>();

									
									if(getmessaage !=null){
										
										
										map.put("AndroidCreatedDate", String.valueOf(getmessaage.getProperty("AndroidCreatedDate")));
										
										map.put("SKIN", String.valueOf(getmessaage.getProperty("SKIN")));
										
										map.put("COLOR", String.valueOf(getmessaage.getProperty("COLOR")));
										
										map.put("TYPE", Type_radio_button);
										
										
										
										todaymessagelist.add(map);
									}
								}
							}else{
								
								//
								//flag ="1";
							}
//						}
						//Log.e("","resultsRequestSOAP=="+resultsRequestSOAP.toString());
					
//						if(Type_radio_button.equalsIgnoreCase("SKIN")){
//							
//							 resultsRequestSOAP = service.GetDashboradData("0",  FromDate,  ToDate,  username, Type_radio_button);
//							 
//							 if (resultsRequestSOAP != null) {
//									
//									todaymessagelist.clear();
//									for (int i = 0; i < resultsRequestSOAP.getPropertyCount(); i++) {
//										Log.e("","not5");
//
//										SoapObject getmessaage = (SoapObject) resultsRequestSOAP.getProperty(i);
//										HashMap<String, String> map = new HashMap<String, String>();
//
//										
//										if(getmessaage !=null){
//											
//											map.put("AndroidCreatedDate", String.valueOf(getmessaage.getProperty("AndroidCreatedDate")));
//											
//											map.put("SKIN", String.valueOf(getmessaage.getProperty("SKIN")));
//											
//											map.put("COLOR", String.valueOf(getmessaage.getProperty("COLOR")));
//											
//											
//											
//											
//											todaymessagelist.add(map);
//										}
//									}
//								}else{
//									
//									//
//									//flag ="1";
//								}
//							//Log.e("","resultsRequestSOAP=="+resultsRequestSOAP.toString());
//							}
//						if(Type_radio_button.equalsIgnoreCase("COLOR")){
//							
//							 resultsRequestSOAP = service.GetDashboradData("0",  FromDate,  ToDate,  username, Type_radio_button);
//							 
//							 if (resultsRequestSOAP != null) {
//									
//									todaymessagelist.clear();
//									for (int i = 0; i < resultsRequestSOAP.getPropertyCount(); i++) {
//										Log.e("","not5");
//
//										SoapObject getmessaage = (SoapObject) resultsRequestSOAP.getProperty(i);
//										HashMap<String, String> map = new HashMap<String, String>();
//
//										
//										if(getmessaage !=null){
//											
//											
//											map.put("AndroidCreatedDate", String.valueOf(getmessaage.getProperty("AndroidCreatedDate")));
//											
//											map.put("SKIN", String.valueOf(getmessaage.getProperty("SKIN")));
//											
//											map.put("COLOR", String.valueOf(getmessaage.getProperty("COLOR")));
//										
//											todaymessagelist.add(map);
//										}
//									}
//								}else{
//									
//									
//								}
//							
//							}
						

					} catch (Exception e) {
						// TODO: handle exception
						//flag="1";
						returnMessage = "1";
						e.printStackTrace();
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
				
				Toast.makeText(BocDashboardGridViewActivity.this, "Something went wrong!! Please try again", Toast.LENGTH_SHORT).show();
			}else{
				adapter = new BADashboardGridViewReportAdapter(BocDashboardGridViewActivity.this, todaymessagelist);
				listView.setAdapter(adapter);// add custom adapter to
			}
		}
	}
	
	public class GetTotalBAReport extends AsyncTask<Void, Void, SoapObject>
	{

		String resultString="";
		SoapObject soap_result;
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			prgdialog.setMessage("Please Wait...");
			prgdialog.show();
			prgdialog.setCancelable(false);
			
		}

		@Override
		protected void onPostExecute(SoapObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			prgdialog.dismiss();
			
			if(result != null)
			{
				//Dashboard=anyType{AndroidCreatedDate=null; COLOR=84751; SKIN=107551; TOTAL=192302; }; }

				SoapObject soapObjct = (SoapObject) result.getProperty(0);
				
				if(soapObjct.getProperty("COLOR") != null )
				{
					if(!soapObjct.getProperty("COLOR").toString().equalsIgnoreCase("anyType{}"))
					{
						tv_month_color.setText(soapObjct.getProperty("COLOR").toString());
					}
				}
				
				if(soapObjct.getProperty("SKIN") != null )
				{
					if(!soapObjct.getProperty("SKIN").toString().equalsIgnoreCase("anyType{}"))
					{
						tv_month_skin.setText(soapObjct.getProperty("SKIN").toString());
					}
				}
				
				if(soapObjct.getProperty("TOTAL") != null )
				{
					if(!soapObjct.getProperty("TOTAL").toString().equalsIgnoreCase("anyType{}"))
					{
						tv_month_total.setText(soapObjct.getProperty("TOTAL").toString());
					}
				}
				
			}
		
			
		}

		@Override
		protected SoapObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
			soap_result = service.GetDashboradData("1", FromDate, ToDate, username, "");
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return soap_result;
		}
		
	}


}

	

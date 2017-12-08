package com.prod.sudesi.lotusherbalsnew;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.prod.sudesi.lotusherbalsnew.R;

import libs.LotusWebservice;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import dbConfig.Dbcon;
import dbConfig.Dbhelper;

@SuppressLint("NewApi")
public class StockFragment extends Activity {

	Context context;
	Spinner product_category, product_type, products;
	Button btn_save, btn_back;
	EditText op, cl, rt_salable, rt_non_salable, sold, previous_bal,
			fresh_stock, total_gross_amt, total_net_amt, discount;
	
	EditText et_sold_extra,et_sold_extra1;

	TextView sizet, pricet, catidt, enaidt, dbidt,shadeno_n;
	String productstt;

	SharedPreferences shp;
	SharedPreferences.Editor shpeditor;

	private ProgressDialog mProgress;
	Cursor stock_array;

	LotusWebservice service;

	com.sudesi.adapter.MessageAdapter productadapter;

	ArrayList<HashMap<String, String>> productDetailsArray = new ArrayList<HashMap<String, String>>();

	ArrayList<String> productcategory = new ArrayList<String>();
	ArrayList<String> productArray = new ArrayList<String>();
	ArrayList<String> producttypeArray = new ArrayList<String>();
	ArrayList<String> priceArray = new ArrayList<String>();

	Dbcon db;
	private SQLiteDatabase dataBase;

	Cursor mCursor;
	// int selected_product_category_id, selected_product_type_id,
	// selected_product_id;

	String selected_product_category, selected_product_type,
			selected_product_name, selected_product_id1;
	String cll = "0";

	ListView listView;
	// LazyAdapterProductList adapter;

	TextView table_idlt, sizelt, pricelt, enalt, catlt, dbidlt,productst,shadeno;
	TableLayout tl_stock_calculation, Table_list_tages;
	LinearLayout ll;
	TextView previous_total_amount, previous_net_total_amount,
			previous_sold_items, previous_return_items,
			prvious_retrun_non_saleable;

	TextView tv_h_username;
	Button btn_home, btn_logout;
	String username;

	String Closing_balance;
	int Return_Salable,Retrun_non_Salable;
	
	
	RadioGroup rg_old_new_price;
	RadioButton rb_old;
	RadioButton rb_new;
	
	
	TextView tv_show_price;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fragment_stock);

		context = getApplicationContext();

		product_category = (Spinner) findViewById(R.id.sp_product_category);
		product_type = (Spinner) findViewById(R.id.sp_product_type);
		products = (Spinner) findViewById(R.id.sp_products);
		
		rg_old_new_price = (RadioGroup)findViewById(R.id.radioGroup1);
		
		rb_new =(RadioButton)findViewById(R.id.rb_new);
		rb_old =(RadioButton)findViewById(R.id.rb_old);

		btn_save = (Button) findViewById(R.id.btn_save);
		btn_back = (Button) findViewById(R.id.btn_back);

		op = (EditText) findViewById(R.id.et_stock_op_balance);
		cl = (EditText) findViewById(R.id.et_stock_cl_balance);
		
		tv_show_price = (TextView)findViewById(R.id.tvpriceshow);

		rt_salable = (EditText) findViewById(R.id.et_stock_return_saleable);
		rt_non_salable = (EditText) findViewById(R.id.et_stock_return_non_saleable);

		sold = (EditText) findViewById(R.id.et_stock_sold);
		previous_bal = (EditText) findViewById(R.id.et_stock_previous_balance);

		fresh_stock = (EditText) findViewById(R.id.et_stock_fresh_stock);
		total_gross_amt = (EditText) findViewById(R.id.et_stock_tatal_gross_amount);
		total_net_amt = (EditText) findViewById(R.id.et_stock_tatal_net_amount);

		discount = (EditText) findViewById(R.id.et_stock_discount);

		sizet = (TextView) findViewById(R.id.tv_product_size);
		pricet = (TextView) findViewById(R.id.tv_product_price);
		catidt = (TextView) findViewById(R.id.tv_category_code);
		enaidt = (TextView) findViewById(R.id.tv_enacode);
		dbidt = (TextView) findViewById(R.id.tv_db_id);
		shadeno_n =(TextView)findViewById(R.id.tv_shadeno);

		tl_stock_calculation = (TableLayout) findViewById(R.id.tl_stock_calculation);
		Table_list_tages = (TableLayout) findViewById(R.id.Tablelisttages);
		ll = (LinearLayout) findViewById(R.id.ll_spinerlayout);

		previous_total_amount = (TextView) findViewById(R.id.tv_previous_totalamount);
		previous_return_items = (TextView) findViewById(R.id.tv_previous_return_items);
		previous_sold_items = (TextView) findViewById(R.id.tv_previous_sold_items);

		previous_net_total_amount = (TextView) findViewById(R.id.tv_previous_net_total_amount);

		prvious_retrun_non_saleable = (TextView) findViewById(R.id.tv_previous_return_non_salebale_items);

		listView = (ListView) findViewById(R.id.lv_product_list);
		
		et_sold_extra = (EditText)findViewById(R.id.et_stock_sold_extra);
		et_sold_extra1 = (EditText)findViewById(R.id.et_stock_sold_extra1);

		db = new Dbcon(this);
		shp = context.getSharedPreferences("Lotus", context.MODE_PRIVATE);
		shpeditor = shp.edit();

		mProgress = new ProgressDialog(this);
		service = new LotusWebservice(context);

		try {

			// ------------------

			tv_h_username = (TextView) findViewById(R.id.tv_h_username);
			btn_home = (Button) findViewById(R.id.btn_home);
			btn_logout = (Button) findViewById(R.id.btn_logout);
			username = shp.getString("username", "");
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
					//startActivity(new Intent(getApplicationContext(),
					//		DashboardNewActivity.class));
				}
			});
			// ---------------------
			
			String div = shp.getString("div", "");
			if(div.equalsIgnoreCase("LH & LHM")){
				
				
				db.open();
				productcategory = db.getproductcategory1(); // ------------
				db.close();
				// System.out.println(productArray);
				Log.e("", "kkkklklk111");
				
			}else {
				productcategory.clear();
				productcategory.add("Select");
				productcategory.add("SKIN");
				
			}
			

			
			
			

			ArrayAdapter<String> product_adapter = new ArrayAdapter<String>(
			// context, android.R.layout.simple_spinner_item,
					context, R.layout.custom_sp_item, productcategory);

			product_adapter
			// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					.setDropDownViewResource(R.layout.custom_spinner_dropdown_text);

			product_category.setAdapter(product_adapter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		product_category
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						selected_product_category = product_category
								.getItemAtPosition(arg2).toString().trim();

						if (selected_product_category
								.equalsIgnoreCase("Select")
								|| selected_product_category
										.equalsIgnoreCase("")) {
							listView.setVisibility(View.GONE);
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									context,
									android.R.layout.simple_spinner_dropdown_item,
									new String[] {});
							product_type.setAdapter(adapter);
							products.setAdapter(adapter);

							Toast.makeText(StockFragment.this,
									"Select Category", Toast.LENGTH_SHORT)
									.show();
							rb_new.setChecked(false);
							rb_old.setChecked(false);
						} else {

							db.open();

							producttypeArray = db
									.getproductype1(selected_product_category); // -------------
							System.out.println(producttypeArray);
							db.close();
							ArrayAdapter<String> product_adapter1 = new ArrayAdapter<String>(
									// context,
									// android.R.layout.simple_spinner_item,
									context, R.layout.custom_sp_item,
									producttypeArray);

							product_adapter1
							// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									.setDropDownViewResource(R.layout.custom_spinner_dropdown_text);
							product_type.setAdapter(product_adapter1);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		product_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				selected_product_type = product_type.getItemAtPosition(arg2)
						.toString().trim();

				if (selected_product_type.equalsIgnoreCase("Select")
						|| selected_product_category.equalsIgnoreCase("")) {
					listView.setVisibility(View.GONE);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							context,
							android.R.layout.simple_spinner_dropdown_item,
							new String[] {});
					// product_type.setAdapter(adapter);
					products.setAdapter(adapter);

					Toast.makeText(StockFragment.this, "Select Product type",
							Toast.LENGTH_SHORT).show();
					rb_new.setChecked(false);
					rb_old.setChecked(false);
				} else {
					/*
					db.open();
					productArray = db.getproducts1(selected_product_type); // ---------------
					System.out.println(productArray);
					db.close();
					ArrayAdapter<String> product_adapter2 = new ArrayAdapter<String>(
					// context, android.R.layout.simple_spinner_item,
							context, R.layout.custom_sp_item, productArray);

					product_adapter2
					// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							.setDropDownViewResource(R.layout.custom_spinner_dropdown_text);
					products.setAdapter(product_adapter2);
					
					*/
					String selected_category = product_category
							.getSelectedItem().toString();
					String selected_type = product_type.getSelectedItem()
							.toString();

					Log.v("", "" + selected_category + " " + selected_type);
					
					listView.setVisibility(View.GONE);
					rb_new.setChecked(false);
					rb_old.setChecked(false);

				//	getallproducts(selected_category, selected_type);
					
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		rg_old_new_price.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				try{
				String selected_category = product_category
						.getSelectedItem().toString();
				if(selected_category==null){
					
					
				}
				
				String selected_type = product_type.getSelectedItem()
						.toString();
				if(selected_type==null){
					
					
				}

				Log.v("", "" + selected_category + " " + selected_type);

				
				if (selected_product_category
						.equalsIgnoreCase("Select")
						|| selected_product_category
								.equalsIgnoreCase("")) {
					listView.setVisibility(View.GONE);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							context,
							android.R.layout.simple_spinner_dropdown_item,
							new String[] {});
					product_type.setAdapter(adapter);
					products.setAdapter(adapter);

					Toast.makeText(StockFragment.this,
							"Select Category", Toast.LENGTH_SHORT)
							.show();
				}  else 
				if (selected_product_type.equalsIgnoreCase("Select")
						|| selected_product_category.equalsIgnoreCase("")) {
					listView.setVisibility(View.GONE);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							context,
							android.R.layout.simple_spinner_dropdown_item,
							new String[] {});
					// product_type.setAdapter(adapter);
					products.setAdapter(adapter);

					Toast.makeText(StockFragment.this, "Select Product type",
							Toast.LENGTH_SHORT).show();
				} else {
			

				if(rb_new.isChecked()){
					
					getallproducts(selected_category, selected_type,"N");
				}
				
				if(rb_old.isChecked()){
					
					getallproducts(selected_category, selected_type,"O");	
					
					
				}
				
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		

		/*products.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				// selected_product_id = products.getSelectedItemPosition();
				selected_product_name = products.getItemAtPosition(arg2)
						.toString().trim();

				if (selected_product_name.equalsIgnoreCase("Select")
						|| selected_product_category.equalsIgnoreCase("")) {
					listView.setVisibility(View.GONE);
					Toast.makeText(StockFragment.this, "Select Product Name",
							Toast.LENGTH_SHORT).show();
				} else {

					

					String selected_product = products.getSelectedItem()
							.toString();
					String selected_category = product_category
							.getSelectedItem().toString();
					String selected_type = product_type.getSelectedItem()
							.toString();

					Log.v("", "" + selected_category + " " + selected_type
							+ " " + selected_product);

					//getallproducts(selected_category, selected_type,
					//		selected_product);
					

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});*/

		fresh_stock.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				Log.e("", "ppp=");

				// op.setText("25");
				String p_bal = previous_bal.getText().toString().trim();
				Log.e("", "p_bal=" + p_bal);
				if(p_bal.equalsIgnoreCase("")){
					p_bal ="0";
				}
				int p = Integer.parseInt(p_bal);
				Log.e("", "ppp=" + p);
				String f_bal = fresh_stock.getText().toString().trim();
				Log.e("", "f_bal=" + f_bal);
				if (f_bal.equalsIgnoreCase("")) {
					f_bal = "0";
				}
				int f = Integer.parseInt(f_bal);
				Log.e("", "f=" + f);
				int ob = p + f;
				Log.e("", "ob=" + ob);
				String opb = String.valueOf(ob);

				op.setText(opb);
				cl.setText("");
				sold.setText("");
				rt_salable.setText("");
				rt_non_salable.setText("");
				total_gross_amt.setText("");
				total_net_amt.setText("");

			}
		});

		sold.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				try {

					String op_stk;
					String rt_stk;
					String sold_stk;

					if (op.getText().toString().trim().length() == 0) {

						op.setError("Enter Opening Balance");

					} else if (sold.getText().toString().trim().length() == 0) {
						op.setError(null);
						// sold.setError("Enter Sold Balance");
						total_gross_amt.setText("");
						total_net_amt.setText("");
						cl.setText("");

					}/*
					 * else if (rt.getText().toString().length()==0) {
					 * sold.setError(null); rt.setError("Enter Return value");
					 * 
					 * 
					 * }
					 */

					else {

						op_stk = op.getText().toString().trim();
						if (op_stk.equalsIgnoreCase("")) {

							op_stk = "0";
						}
						int op_stk1 = Integer.parseInt(op_stk);

						rt_stk = rt_salable.getText().toString().trim();

						if (rt_stk.equalsIgnoreCase("")) {

							rt_stk = "0";
						}

						int rt_stk1 = Integer.parseInt(rt_stk);

						sold_stk = sold.getText().toString().trim();
						int sld = Integer.parseInt(sold_stk);

						if (sold_stk.equalsIgnoreCase("") || sld <= 0) {

							sold_stk = "0";

							total_gross_amt.setText("0");
							total_net_amt.setText("0.0");
							

						} else {

							int sold_stk1 = Integer.parseInt(sold_stk);

							if (sold_stk1 <= 0) {

								sold_stk1 = 0;
							}

							int p = rt_stk1 + sold_stk1;

							if (op_stk1 < sold_stk1) {
								Toast.makeText(
										StockFragment.this,
										"'Sold' must not greater than Opening Balance",
										Toast.LENGTH_LONG).show();
								cl.setText("");
								sold.setText("");
							} else

							if (op_stk1 < p) {

								Toast.makeText(
										StockFragment.this,
										"'Sold + Return' must not greater than Opening Balance",
										Toast.LENGTH_LONG).show();
								cl.setText("");

							} else {

								int closingbal = op_stk1 - sold_stk1;
								// int closingbal = op_stk1 - sold_stk1;

								String rtsalable = rt_salable.getText()
										.toString().trim();
								String rtnsalable = rt_non_salable.getText()
										.toString().trim();

								//if (rtsalable.equalsIgnoreCase("")) {
								//	rtsalable = "0";
								//}
								if (rtnsalable.equalsIgnoreCase("")) {
									rtsalable = "0";
								}

							//	int rtsalablei = Integer.parseInt(rtsalable);
							//	int rtnsalablei = Integer.parseInt(rtsalable);

							
								int clsi = 0;
								int clsii;

								// String solds = String.valueOf(soldi);

								if(rtsalable.equalsIgnoreCase("")){
									
									
									
									String op1 = op.getText().toString().trim();
									int opi = Integer.parseInt(op1);
									String soldi = sold.getText().toString();
									int soldi1 = Integer.parseInt(soldi);
									int clsi1 = opi - soldi1;
								//	cl.setText(String.valueOf(clsi1));
									
									Closing_balance = String.valueOf(clsi1);
								}
							}
							String s1 = sold.getText().toString().trim();

							if (s1.equalsIgnoreCase("")) {

								s1 = "0";
							}
							int sd = Integer.parseInt(s1);

							if (sd <= 0) {

								sd = 0;
							}
							String r = rt_salable.getText().toString().trim();
							// int rtt =Integer.parseInt(r);

							String prices = pricet.getText().toString().trim();

							if (prices.equalsIgnoreCase("")) {

								prices = "0";
							}
							int pricess = Integer.parseInt(prices);

							// int ta = (sd * pricess )-(rtt * pricess);
							int ta = (sd * pricess);

							// ---------------------

							String disc = discount.getText().toString().trim();

							if (disc.equalsIgnoreCase("")) {

								disc = "0";
							}
							double dis = Double.parseDouble(disc);
							double pric = Double.parseDouble(prices);

							double minus_dis = dis * pric;

							// ---------------------
							// String ta1 = total_amt.getText().toString();
							// int ta2 = Integer.parseInt(ta1);
							// int ta3 = ta + ta2 ;

							String tat = String.valueOf(ta);
							// --------------------
							double d_tatol = Double.parseDouble(tat);
							double d_t_n_total = d_tatol - minus_dis;
							String d_t_n_total_s = String.valueOf(d_t_n_total);

							// ----------------------
							total_gross_amt.setText(tat);
							total_net_amt.setText(d_t_n_total_s);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		});
		
		rt_salable.addTextChangedListener(new TextWatcher() {
			String soldsb;
			String soldsettext;
			String clbss;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try{
				String soldb = sold.getText().toString().trim();
				String rt_sal = rt_salable.getText().toString().trim();
				
				
				
				if (op.getText().toString().trim().length() == 0) {

					op.setError("Enter Opening Balance");
					
				}else if(sold.getText().toString().trim().length()==0){
					
					sold.setError("Enter sold stock");
				}else if(rt_salable.getText().toString().trim().length()==0){
					
					rt_salable.setError("Enter return saleable");
					rt_non_salable.setEnabled(false);
					et_sold_extra.setText("");
					cl.setText("");
					rt_non_salable.setText("");
				}else{
				
					rt_non_salable.setEnabled(true);
				
				if(rt_sal.equalsIgnoreCase("")){
					rt_sal = "0";
				}
				
				if(soldb.equalsIgnoreCase("")||soldb.equalsIgnoreCase("0")){
					
					int rt_sali = Integer.parseInt(rt_sal);
				Return_Salable = rt_sali;
					
				}else{
				int soldbi = Integer.parseInt(soldb);
				int rt_sali = Integer.parseInt(rt_sal);
				
				int soldbii = soldbi - rt_sali;
				
				if(soldbii<=0){
					
					soldbii = 0;
				}
					et_sold_extra.setText(String.valueOf(soldbii));
					
				}
				
				
				
				
			}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		rt_salable.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				
				if(et_sold_extra.getText().toString().trim().length()!=0){
				sold.setText(et_sold_extra.getText().toString().trim());
				et_sold_extra.setText("");
				}
				
				
				
			}
		});

		

		
		rt_non_salable.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try{
				String opb = op.getText().toString().trim();
				String slb = sold.getText().toString().trim();
		
				String rtns = rt_non_salable.getText().toString().trim();
				
				
				String rt_s_stk = rt_salable.getText().toString().trim();
				//if(rtns.equalsIgnoreCase("")||rtns.equalsIgnoreCase("0")){
					
				//	cl.setText("");
				//}
				if (op.getText().toString().trim().length() == 0) {

					op.setError("Enter Opening Balance");
					
				}else if(sold.getText().toString().trim().length()==0){
					
					sold.setError("Enter sold stock");
				}else if(rt_salable.getText().toString().trim().length()==0){
					
					rt_salable.setError("Enter return saleable");
					
				}else if (rt_non_salable.getText().toString().trim().length()==0){
				
					rt_non_salable.setError("Enter return non saleable");
				
					et_sold_extra1.setText("");
					cl.setText("");
					
				}else{
				
					
					
					if(opb.equalsIgnoreCase("")){
						
						opb = "0";
					}
					
					if(rtns.equalsIgnoreCase("")){
						
						rtns = "0";
					}
					 if(rt_s_stk.equalsIgnoreCase("")){
						 
						 rt_s_stk = "0";
					 }
					 
					if(slb.equalsIgnoreCase("")||slb.equalsIgnoreCase("0")){
						
						slb = "0";
						
						int rts = Integer.parseInt(rt_s_stk);
						int rtnsi = Integer.parseInt(rtns);
						
						int opbi= Integer.parseInt(opb);
						int slbi = Integer.parseInt(slb);
						
						int cls = (opbi - slbi)+rts  + rtnsi;
						
						cl.setText(String.valueOf(cls));
						
					}else{
						
						
					int rtnsi = Integer.parseInt(rtns);
					
					Retrun_non_Salable = rtnsi;
					
					
					
					int opbi= Integer.parseInt(opb);
					
					int slbi = Integer.parseInt(slb);
					
				//	int cls = (opbi - slbi) - rtnsi;
					
				//	cl.setText(String.valueOf(cls));
					
					
					int soldbii = slbi - rtnsi;
					
					if(soldbii<=0){
						
						soldbii = 0;
					}
						et_sold_extra1.setText(String.valueOf(soldbii));
						
					}
					
				
				}	
				}catch(Exception e){
					
					e.printStackTrace();
				}
			}
		});
		
		rt_non_salable.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				try{
				
					
				if(et_sold_extra1.getText().toString().trim().length()!=0){
					Log.v("","log3");
					sold.setText(et_sold_extra1.getText().toString().trim());
					et_sold_extra1.setText("");
					
					String opb = op.getText().toString().trim();
					String soldb = sold.getText().toString().trim();
					
					String rt = rt_salable.getText().toString().trim();
					String rtn = rt_non_salable.getText().toString().trim();
					
					if(rt.equalsIgnoreCase("")){
						
						rt="0";
					}
					if(rtn.equalsIgnoreCase("")){
						rtn="0";
					}
					
					if(opb.equalsIgnoreCase("")){
						opb = "0";
					}
					
					if(soldb.equalsIgnoreCase("")){
						soldb = "0";
					}
					
					
					int opbi= Integer.parseInt(opb);
					
					int slbi = Integer.parseInt(soldb);
					
					
					
					int cls = (opbi - slbi);
					
					//int clsii = Integer.parseInt(rtn)  + cls;
					
					
					
					cl.setText(String.valueOf(cls));
				//	}
					
					}
				if(sold.getText().toString().trim().length()==0){
					Log.v("","log1");
					String rt = rt_salable.getText().toString().trim();
					String rtn = rt_non_salable.getText().toString().trim();
					Log.v("","log2");
					if(rt.equalsIgnoreCase("")){
						rt = "0";
					}
					if(rtn.equalsIgnoreCase("")){
						rtn = "0";
					}
					
					int cls = Integer.parseInt(rt) +  Integer.parseInt(rtn); ;
					 
					cl.setText(String.valueOf(cls));
					
				}
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
			}
			
		});
		
		discount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try{
				String disc = discount.getText().toString().trim();

				if (disc.equalsIgnoreCase("")) {

					disc = "0";
				}
				String prices = pricet.getText().toString().trim();
				if(prices.equalsIgnoreCase("")){
					prices="0";
				}
				
				double dis = Double.parseDouble(disc);
				double dish = dis / 100;
				double pric = Double.parseDouble(prices);
				
				

				double minus_dis = dish * pric;

				// ---------------------

				// --------------------
				String tat = total_gross_amt.getText().toString().trim();
				if(tat.equalsIgnoreCase("")){
					tat="0";
				}
				double d_tatol = Double.parseDouble(tat);
				double d_t_n_total = d_tatol - minus_dis;
				String d_t_n_total_s = String.valueOf(d_t_n_total);
				total_net_amt.setText(d_t_n_total_s);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ll.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				Table_list_tages.setVisibility(View.VISIBLE);
				tl_stock_calculation.setVisibility(View.GONE);
				
				op.setText(""); cl.setText(""); rt_salable.setText(""); rt_non_salable.setText(""); 
				sold.setText(""); previous_bal.setText("");
				fresh_stock.setText(""); total_gross_amt.setText(""); total_net_amt.setText(""); discount.setText("");
				
				

			}
		});

		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					Calendar c = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String insert_timestamp = sdf.format(c.getTime());

					String op_stk1 = op.getText().toString().trim();
					String cl_stk1 = cl.getText().toString().trim();
					String rt_stk1 = rt_salable.getText().toString().trim();
					String sold_stk1 = sold.getText().toString().trim();

					String disc = discount.getText().toString().trim();
					String total_net_amt1 = total_net_amt.getText().toString()
							.trim();
					String p_balance = previous_bal.getText().toString().trim();
					String rt_non_stk = rt_non_salable.getText().toString()
							.trim();
					String fresh_stock11 = fresh_stock.getText().toString().trim();

					if(fresh_stock.getText().toString().trim().length() == 0
							|| fresh_stock11.startsWith(" ")){
						
						fresh_stock.setError("Enter Stock Received");
						
					}
					else if (op.getText().toString().trim().length() == 0
							|| op_stk1.startsWith(" ")) {

						op.setError("Enter Opening Balance");
					} else if (sold.getText().toString().trim().length() == 0
							|| sold_stk1.startsWith(" ")) {
						op.setError(null);
						sold.setError("Enter Sold Balance");
						fresh_stock.setError(null);
					}/*
					 * else if (rt.getText().toString().length()==0) {
					 * sold.setError(null); rt.setError("Enter Return value"); }
					 */
					
					else if (rt_salable.getText().toString().trim().length() == 0
							|| rt_stk1.startsWith(" ")){
						
						sold.setError(null);
						rt_salable.setError("Enter return saleable");
						
					}else if (rt_non_salable.getText().toString().trim().length() == 0
							|| rt_non_stk.startsWith(" ")){
						
						rt_salable.setError(null);
						rt_non_salable.setError("Enter return non saleable");
						
					}
					else if (cl.getText().toString().trim().length() == 0
							|| cl_stk1.startsWith(" ")) {

						rt_non_salable.setError(null);
						
						//cl.setError("Enter above needed fields");
						Toast.makeText(getApplicationContext(), "click on discount", Toast.LENGTH_SHORT).show();
						
					} else {

						cl.setError(null);
						String cll  = cl.getText().toString().trim();
						if(cll.equalsIgnoreCase(" ")||cll.equalsIgnoreCase("null")){
							
							cll="0";
						}
						
						int cli = Integer.parseInt(cll);
						
						Log.e("", "cli=="+cli);
						
						if(cli <0){
							
							cl.setError("Closing Balance is not proper");
							Log.v("", "cl if");	
						
						}else{
						
							Log.v("", "cl else");
						
						cl.setError(null);
						
						String op_stk = op.getText().toString().trim();
						String cl_stk = cl.getText().toString().trim();
						String rt_stk = rt_salable.getText().toString().trim();
						String sold_stk = sold.getText().toString().trim();

						String rt_n_s_stk = rt_non_salable.getText().toString()
								.trim();

						if (rt_n_s_stk.equalsIgnoreCase("")) {
							rt_n_s_stk = "0";
						}

						if (rt_stk.equalsIgnoreCase("")) {
							rt_stk = "0";
						}

						if (sold_stk.equalsIgnoreCase("")) {
							sold_stk = "0";
						}
						
						String emp_id = shp.getString("username", "");

						Log.e("", "emp_id==" + emp_id);

						// String product_id =
						// selected_product_id1;//selected_product_id1;
						String product_category = selected_product_category;
						Log.e("", "lOGl1");
						String product_type1 = selected_product_type;
						Log.e("", "lOGl2");
						//String product_name = selected_product_name;// selected_product_name;
						
						String product_name = productstt;//changed 06.12.2014        
						Log.e("", "lOGl3");
						String fresh_stock1 = fresh_stock.getText().toString()
								.trim();
						if(fresh_stock1.equalsIgnoreCase("")){
							
							fresh_stock1="0";
						}
						
						Log.e("", "lOGl4");
						String Ttl_amount = total_gross_amt.getText()
								.toString().trim();
						
						
						if(Ttl_amount.equalsIgnoreCase("")){
							
							Ttl_amount="0";
						}
						
						Log.e("", "lOGl5");
						int ta = Integer.parseInt(Ttl_amount);
						Log.e("", "lOGl6");
						
						String Ttl_amount1 = previous_total_amount.getText()
								.toString().trim();
						
						if(Ttl_amount1.equalsIgnoreCase("")){
							
							Ttl_amount1="0";
						}
						
						Log.e("", "lOGl7="+Ttl_amount1);
						
						int ta1 = Integer.parseInt(Ttl_amount1);
						int ta2 = ta + ta1;
						
						Log.e("", "lOGl8="+ta2);
						String Ttl_amount2 = String.valueOf(ta2);
						
						Log.e("", "lOGl9");
						int current_sold = Integer.parseInt(sold_stk);
						
						Log.e("", "lOGl10");
						int current_return = Integer.parseInt(rt_stk);
						Log.e("", "lOGl11");

						String old_sold = previous_sold_items.getText()
								.toString().trim();
						
						
						
						Log.e("", "lOGl12");
						String old_return = previous_return_items.getText()
								.toString().trim();
						Log.e("", "lOGl13");

						
						String old_return_non_salable = prvious_retrun_non_saleable
								.getText().toString().trim();
						Log.e("", "lOGl15");
						if (old_return_non_salable.equalsIgnoreCase("")) {
							old_return_non_salable = "0";
						}

						if (old_return.equalsIgnoreCase("")) {
							old_return = "0";
						}
						Log.e("", "lOGl2");
						
						if (old_sold.equalsIgnoreCase("")) {
							old_sold = "0";
						}
						
						int previous_sold = Integer.parseInt(old_sold);
						Log.e("", "lOGl3");
						
						int previous_rtn = Integer.parseInt(old_return);
						Log.e("", "lOGl4");
						
						int prvious_rtn_n_sold = Integer
								.parseInt(old_return_non_salable);
						Log.e("", "lOGl5");
						
						int current_rtn_n_sold = Integer.parseInt(rt_n_s_stk);
						Log.e("", "lOGl6");
						int add_current_old_sold = current_sold + previous_sold;
						Log.e("", "lOG7");
						
						int add_current_old_rtn = current_return + previous_rtn;
						Log.e("", "lOG8");
						int add_current_old_rtn_n_sold = current_rtn_n_sold
								+ prvious_rtn_n_sold;
						Log.e("", "lOG9");
						
						String new_rtn_n_sold = String
								.valueOf(add_current_old_rtn_n_sold);
						Log.e("", "lOG10");
						
						String new_sold = String.valueOf(add_current_old_sold);
						Log.e("", "lOG11");
						
						String new_retn = String.valueOf(add_current_old_rtn);

						Log.e("", "sdffsdf==1");
						String price = pricet.getText().toString().trim();
						
						if (price.equalsIgnoreCase("")) {
							price = "0";
						}

						String size = sizet.getText().toString().trim();
						
						if (size.equalsIgnoreCase("")) {
							size = "0";
						}
						Log.e("", "sdffsdf==2");

						Log.e("", "sdffsdf==3");

						String ptnamt = previous_net_total_amount.getText()
								.toString().trim();
						
						if (ptnamt.equalsIgnoreCase("")) {
							ptnamt = "0";
						}
						Log.e("", "sdffsdf==4");

						double tnamt = Double.parseDouble(total_net_amt1);

						Log.e("", "lOG12");
						double iptnamt = Double.parseDouble(ptnamt);
						Log.e("", "lOG13");
						double tnamtadd = tnamt + iptnamt;
						Log.e("", "sdffsdf==5");
						String ttnamt = String.valueOf(tnamtadd);

						String eancode = enaidt.getText().toString().trim();
						Log.v("", "enacode==" + eancode);
						
						String db_id1 = dbidt.getText().toString().trim();
						Log.e("", "lOG14");
						
						String cat_id = catidt.getText().toString().trim();
						Log.e("", "lOG15");
						
						String shadenon = shadeno_n.getText().toString().trim();
						Log.e("", "lOG16");
						
						if(shadenon.equalsIgnoreCase("")||shadenon.equalsIgnoreCase("null")){
							
							shadenon="";
						}

						String[] insert_timestamps = insert_timestamp
								.split(" ");
						
						Log.e("", "lOG17");
						String check_timestamp = insert_timestamps[0];
						// String check_timestamp = "2014-10-30";
						Log.e("", "lOG18");

						Cursor mCursor;
						db.open();
						mCursor = db.getuniquedata1(cat_id, eancode, db_id1,
								check_timestamp);

						Log.v("", "" + mCursor.getCount());

						db.close();

						if (mCursor.getCount() == 0) {

							db.open();
							db.Insertstock(
									// product_id,
									product_category, product_type1,
									product_name, emp_id, op_stk, cl_stk,
									fresh_stock1, Ttl_amount2, sold_stk, price,
									size, eancode, db_id1, cat_id,
									insert_timestamp, disc, total_net_amt1,
									p_balance, rt_stk, rt_non_stk,shadenon); // --------
							db.close();

							// new SendToSeverStockData().execute();

							/*
							 * Fragment frag1 = new DashboardActivity();
							 * 
							 * FragmentTransaction ft1 =
							 * getFragmentManager().beginTransaction();
							 * ft1.replace(R.id.activity_main_content_fragment,
							 * frag1); ft1.setTransition(FragmentTransaction.
							 * TRANSIT_FRAGMENT_FADE); ft1.addToBackStack(null);
							 * ft1.commit();
							 */
							Toast.makeText(StockFragment.this, "Data save successfully", Toast.LENGTH_SHORT).show();
							startActivity(new Intent(context,
									DashboardNewActivity.class));

						} else {
							
							db.open();
							db.UpdateStock(
									// product_id,
									product_category, product_type1,
									product_name, emp_id, op_stk, cl_stk,
									fresh_stock1, Ttl_amount2, new_sold, price,
									size, eancode, db_id1, cat_id,
									insert_timestamp, disc, ttnamt, p_balance,
									new_retn, new_rtn_n_sold);
							db.close();

							/*
							 * Fragment frag1 = new DashboardActivity();
							 * 
							 * FragmentTransaction ft1 =
							 * getFragmentManager().beginTransaction();
							 * ft1.replace(R.id.activity_main_content_fragment,
							 * frag1); ft1.setTransition(FragmentTransaction.
							 * TRANSIT_FRAGMENT_FADE); ft1.addToBackStack(null);
							 * ft1.commit();
							 */
							Toast.makeText(StockFragment.this, "Data save successfully", Toast.LENGTH_SHORT).show();
							

							startActivity(new Intent(context,
									DashboardNewActivity.class));
						}
						}
					}

				} catch (Exception e) {

				}
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				try {
					table_idlt = (TextView) view.findViewById(R.id.tv_table_id);
					sizelt = (TextView) view.findViewById(R.id.tv_size_list);
					pricelt = (TextView) view.findViewById(R.id.tv_price_list);
					enalt = (TextView) view.findViewById(R.id.tv_eancode_list);
					catlt = (TextView) view.findViewById(R.id.tv_category_list);
					dbidlt = (TextView) view.findViewById(R.id.tv_db_id_list);
					productst=(TextView) view.findViewById(R.id.tv_product_list);
					
					shadeno =(TextView)view.findViewById(R.id.tv_shade_list);
					

					sizet.setText(sizelt.getText().toString().trim());
					pricet.setText(pricelt.getText().toString().trim());
					enaidt.setText(enalt.getText().toString().trim());
					catidt.setText(catlt.getText().toString().trim());
					dbidt.setText(dbidlt.getText().toString().trim());
					shadeno_n.setText(shadeno.getText().toString().trim());
					
					productstt = productst.getText().toString().trim();

					listView.setVisibility(View.GONE);
					Table_list_tages.setVisibility(View.GONE);
					tl_stock_calculation.setVisibility(View.VISIBLE);

					String eanid = enalt.getText().toString().trim();
					String catid = catlt.getText().toString().trim();
					String dbid = dbidlt.getText().toString().trim();

					getLastInsertIDofStock1(catid, dbid, eanid,pricet.getText().toString().trim());

				} catch (Exception e) {

					e.printStackTrace();

				}

			}
		});

		// super.onCreateView(inflater, container, savedInstanceState);

	}

	public void getallproducts(String selected_category, String selected_type,String flag
			//String selected_product
			) {
		// TODO Auto-generated method stub

		try {
			db.open();

			// idd.clear();
			// size_l.clear();
			// mrp_l.clear();
			// db_id_l.clear();
			// eancode_l.clear();
			// catid_l.clear();

			productDetailsArray.clear();

			//Cursor cursor = db.fetchAllproducts(selected_category,
			//		selected_type, selected_product);
			Cursor cursor = db.fetchAllproductslistforstock(selected_category,selected_type,flag);

			if (cursor.getCount() == 0) {
				productDetailsArray.clear();
				productadapter = new com.sudesi.adapter.MessageAdapter(
						StockFragment.this, productDetailsArray);
				Table_list_tages.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				tl_stock_calculation.setVisibility(View.GONE);

				listView.setAdapter(productadapter);

				
			} else {
				cursor.moveToFirst();
				do {

					HashMap<String, String> map = new HashMap<String, String>();

					map.put("id", cursor.getString(cursor
							.getColumnIndex(Dbhelper.KEY_ID)));
					map.put("size", cursor.getString(cursor
							.getColumnIndex(Dbhelper.KEY_SIZE)));
					map.put("mrp", cursor.getString(cursor
							.getColumnIndex(Dbhelper.KEY_MRP)));
					map.put("dbID", cursor.getString(cursor
							.getColumnIndex(Dbhelper.KEY_DB_ID)));
					map.put("catID", cursor.getString(cursor
							.getColumnIndex(Dbhelper.KEY_CATEGROYID)));
					map.put("eancode", cursor.getString(cursor
							.getColumnIndex(Dbhelper.KEY_EANCODE)));
					
					map.put("product",cursor.getString(cursor.getColumnIndex(Dbhelper.KEY_PRODUCTS)));
					
					map.put("shadeNO", cursor.getString(cursor.getColumnIndex(Dbhelper.KEY_SHADENO)));

					productDetailsArray.add(map);

				} while (cursor.moveToNext());

				Log.e("productDetailsArray", String.valueOf(productDetailsArray.size()));
				
				productadapter = new com.sudesi.adapter.MessageAdapter(
						StockFragment.this, productDetailsArray);
				Table_list_tages.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				tl_stock_calculation.setVisibility(View.GONE);

				listView.setAdapter(productadapter);

			}

			cursor.close();
			db.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@SuppressLint("SimpleDateFormat")
	public void getLastInsertIDofStock1(String catid, String dbid, String eanid,String PRICE) {

		String catid1 = "", eanid1 = "", dbid1 = "", closebal = "", totalamount = "", sold = "", retn = "", retn_n_salebale = "", total_net_amt = "",price="";
		;

		Calendar c = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String date = sdf.format(c.getTime());

		Log.e("pm", "pm5--");
		db.open();
		mCursor = db.getuniquedata1(catid, eanid, dbid, date);

		int count = mCursor.getCount();
		Log.v("", "" + count);

		if (mCursor != null) {

			if (mCursor.moveToFirst()) {

				do {

					closebal = mCursor.getString(mCursor
							.getColumnIndex("close_bal"));

					Log.v("", "closing_bal===" + closebal);

					totalamount = mCursor.getString(mCursor
							.getColumnIndex("total_gross_amount"));
					Log.v("", "totalamount===" + totalamount);

					catid1 = mCursor.getString(mCursor
							.getColumnIndex("product_id"));
					Log.v("", "CategoryId===" + catid);

					eanid1 = mCursor.getString(mCursor
							.getColumnIndex("eancode"));
					Log.v("", "eancode===" + eanid);

					dbid1 = mCursor.getString(mCursor.getColumnIndex("db_id"));
					Log.v("", "db_id===" + dbid);

					sold = mCursor.getString(mCursor
							.getColumnIndex("sold_stock"));
					Log.v("", "sold===" + sold);

					retn = mCursor.getString(mCursor
							.getColumnIndex("return_saleable"));
					Log.v("", "retn===" + retn);

					retn_n_salebale = mCursor.getString(mCursor
							.getColumnIndex("return_non_saleable"));
					Log.v("", "ret_n_sale===" + retn_n_salebale);

					total_net_amt = mCursor.getString(mCursor
							.getColumnIndex("total_net_amount"));
					Log.v("", "total_net_amt==" + total_net_amt);
					
					

				} while (mCursor.moveToNext());

				previous_bal.setText(closebal);
				// total_amt.setText(totalamount);
				previous_total_amount.setText(totalamount);

				previous_return_items.setText(retn);
				previous_sold_items.setText(sold);
				prvious_retrun_non_saleable.setText(retn_n_salebale);
				previous_net_total_amount.setText(total_net_amt);
				tv_show_price.setText(PRICE);

			} else {

				previous_bal.setText("0");
				previous_total_amount.setText("0");

				previous_return_items.setText("0");
				prvious_retrun_non_saleable.setText("0");
				previous_sold_items.setText("0");

				previous_net_total_amount.setText("0");
				
				tv_show_price.setText(PRICE);
				
				

			}

		}
		db.close();

		ll.setVisibility(View.GONE);

	}

}

package com.sudesi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.sudesi.imageloder.ImageLoader1;

@SuppressLint("UseValueOf")
public class ProductAdapter extends BaseAdapter {

	String TAG1 = "Class:LazyAdapter2:";
	private Activity activity;
	private String[] data;
	private String[] msize;
	private String[] mid;
	private String[] mmrp;
	private String[] mdb_id;
	private String[] meancode;
	private String[] mcat_id;
	private String[] mshade;

	private static LayoutInflater inflater = null;
	public ImageLoader1 imageLoader;
	boolean[] checkBoxState;
	CheckBox checkbox;
	TextView tv;
	// protected ArrayList<List> mStrings;
	String val;
	String flag1;
	int flag;

	/*public public ProductAdapter(Activity a, String[] d, String[] size, String[] id,
			String[] mrp, String[] db_id, String[] catid,
			String[] eancode, String[] shadeno, flag) {
		Log.v(TAG1, "ELSE -----=================4" + d.toString());
		Log.v(TAG1, "ELSE -----=================6" + a);
		activity = a;
		data = d;
		stringdata = q;
		spId = spinnerId;
		mgroupId = menuGroupId;
		mdb_id = db_id;
		meancode = eancode;
		mcat_id = cat_id;
		mshade = shade;
		flag1 = getflag;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader1(activity.getApplicationContext());
		checkBoxState = new boolean[data.length];
		Log.v(TAG1, "ELSE -----=================5" + data.length);
		flag = Integer.parseInt(flag1);
		Log.v(TAG1, "Flag======" + flag);

	}*/

	public ProductAdapter(Activity saleActivity, String[] product,
			String[] size, String[] id, String[] mrp, String[] db_id,
			String[] catid, String[] eancode, String[] shadeno, String getflag) {
		// TODO Auto-generated constructor stub
		
		Log.v(TAG1, "ELSE -----=================4" + product.toString());
		
		activity = saleActivity;
		data = product;
		msize = size;
		mid= id;
		mmrp = mrp;
		mdb_id = db_id;
		meancode = eancode;
		mcat_id = catid;
		mshade = shadeno;
		flag1 = getflag;
		
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader1(activity.getApplicationContext());
		
		checkBoxState = new boolean[data.length];
		
		Log.v(TAG1, "ELSE -----=================5" + data.length);
		
		flag = Integer.parseInt(flag1);
		
		Log.v(TAG1, "Flag======" + flag);
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.product_check_list, null);

		if (flag == 1) {

			TextView tv_table_id = (TextView) vi.findViewById(R.id.tv_table_id);
			TextView tv_gms_list = (TextView) vi.findViewById(R.id.tv_gms_list);
			TextView tv_price_list = (TextView) vi.findViewById(R.id.tv_price_list);
			TextView tv_shadeno_stock = (TextView) vi.findViewById(R.id.tv_shadeno_stock_list);
			TextView tv_ean_code_list = (TextView) vi.findViewById(R.id.tv_ean_code_list);
			TextView tv_catid_list = (TextView) vi.findViewById(R.id.tv_catid_list);
			TextView tv_db_id_list = (TextView) vi.findViewById(R.id.tv_db_id_list);
			TextView tv_product_list = (TextView) vi.findViewById(R.id.tv_product_list);
			
			
			checkbox = (CheckBox) vi.findViewById(R.id.checkBox2_list);
			
			
		
			imageLoader.DisplayProductList(
					tv_gms_list,					
					msize[position], tv_product_list, data[position],checkbox,mdb_id[position], tv_price_list,mmrp[position], tv_shadeno_stock,mshade[position]);

			tv = (TextView) vi.findViewById(R.id.text);
			checkbox.setTag(new Integer(position));

			checkbox.setOnCheckedChangeListener(null);
			if (checkBoxState[position])
				checkbox.setChecked(true);
			else
				checkbox.setChecked(false);

			checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Integer pos = (Integer) buttonView.getTag();
					if (isChecked) {

						checkBoxState[pos.intValue()] = true;

					} else {
						checkBoxState[pos.intValue()] = false;
						Log.e("checked", "unchecked");

					}
				}
			});

		} else if (flag == 2) {

			Log.e("TAG1", "Flag======1:" + flag);

			TextView text = (TextView) vi.findViewById(R.id.text);
			TextView text1 = (TextView) vi.findViewById(R.id.text1);
			TextView sp_id = (TextView) vi.findViewById(R.id.tv_sp_id);
			TextView m_group_id = (TextView) vi.findViewById(R.id.tv_m_gr_id);
			TextView shadeno = (TextView) vi.findViewById(R.id.tv_shadeno_tester);
			
			// sp_id.setVisibility(View.GONE);

			ImageView image = (ImageView) vi.findViewById(R.id.image);
			image.setVisibility(View.GONE);
			checkbox = (CheckBox) vi.findViewById(R.id.checkBox2);
			checkbox.setVisibility(View.GONE);

			Log.e("TAG1", "Flag======2:" + data[position]);

			//imageLoader.DisplayMenu1(data[position], text1,
				//	size[position], text, id[position], sp_id,
				//	mgroupId[position], m_group_id,shadeno,mshade[position]);

		}

		return vi;
	}


/*
	@SuppressWarnings("null")
	public String[] getcheckinsert() {

		String pro_name[] = new String[data.length];

		for (int i = 0; i < data.length; i++) {

			// Log.v("ccheck", "status[i]="+status[i].toString());
			if (checkBoxState[i] == true) {
				// if(status[i].equalsIgnoreCase("1")){
				Log.v("ccheck", "log3_i");

				pro_name[i] = data[i];

			} else {

				pro_name[i] = "0";

			}

		}
		return pro_name;

	}

	public String[] getcheckID() {
		String check_id[] = new String[mdb_id.length];

		for (int i = 0; i < mdb_id.length; i++) {

			// Log.v("ccheck", "status[i]="+status[i].toString());
			if (checkBoxState[i] == true) {
				// if(status[i].equalsIgnoreCase("1")){
				Log.v("ccheck", "log3_i");

				check_id[i] = mdb_id[i];

			} else {

				check_id[i] = "0";

			}

		}
		return check_id;
	}
	
	public String[] getData(String fieldname)
	{
		String check_id[] = null;
		if(fieldname.equalsIgnoreCase("EAN"))
		{
			check_id = new String[meancode.length];

			for (int i = 0; i < meancode.length; i++) {

				// Log.v("ccheck", "status[i]="+status[i].toString());
				if (checkBoxState[i] == true) {
					// if(status[i].equalsIgnoreCase("1")){
				
					check_id[i] = meancode[i];

				} else {

					check_id[i] = "0";

				}

			}
		}
		
		if(fieldname.equalsIgnoreCase("CAT"))
		{

			check_id = new String[mcat_id.length];

			for (int i = 0; i < mcat_id.length; i++) {

				// Log.v("ccheck", "status[i]="+status[i].toString());
				if (checkBoxState[i] == true) {
					// if(status[i].equalsIgnoreCase("1")){
				
					check_id[i] = mcat_id[i];

				} else {

					check_id[i] = "0";

				}

			}
		
		}
		
		if(fieldname.equalsIgnoreCase("Size"))
		{
			check_id = new String[id.length];

			for (int i = 0; i < id.length; i++) {

				// Log.v("ccheck", "status[i]="+status[i].toString());
				if (checkBoxState[i] == true) {
					// if(status[i].equalsIgnoreCase("1")){
				
					check_id[i] = id[i];

				} else {

					check_id[i] = "0";

				}

			}
		}
		
		if(fieldname.equalsIgnoreCase("Shade"))
		{

			check_id = new String[mshade.length];

			for (int i = 0; i < mshade.length; i++) {

				// Log.v("ccheck", "status[i]="+status[i].toString());
				if (checkBoxState[i] == true) {
					// if(status[i].equalsIgnoreCase("1")){
				
					check_id[i] = mshade[i];

				} else {

					check_id[i] = "0";

				}

			}
		}
		
		return check_id;
	}
	*/

}
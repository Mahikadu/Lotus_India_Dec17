package com.sudesi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;


public class MessageAdapter extends BaseAdapter {
	
	//custom message adapter for displaying messages

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	private String fromtester ="";
	 boolean[] checkBoxState;
	//ViewHolder mHolder;
	private String data1[];
	Context context;
	TextView txt_id;
	TextView txt_size;
	TextView txt_mrp;
	CheckBox checkbox;
	TextView txt_catid;
	TextView txt_ean;
	TextView txt_db_id;
	TextView txt_product;
	TextView txt_shadeNO;
	
	Animation animation = null;
	
	//constructor
	public MessageAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data=d;
		context = a;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		checkBoxState = new boolean[data.size()];
	}
	
	

	//count of data
	public int getCount() {
		
		return data.size();
	}
	
	//position of object
	public Object getItem(int position) {
		return position;
	}
	//position by index
	public long getItemId(int position) {
		return position;
	}
	//view holder
	//static class ViewHolder {
		
		
	//}

	public View getView(final int position, View child, ViewGroup parent) {
	//view for the custom adapter
		
		animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_in);
		
			if(child==null){
				
				 
			
				
				Log.v("","for stock");
				child = inflater.inflate(R.layout.product_list, null);//inflater for view
			
				txt_id = (TextView) child
						.findViewById(R.id.tv_table_id);
				
				txt_size = (TextView) child
						.findViewById(R.id.tv_size_list);
				txt_mrp = (TextView) child
						.findViewById(R.id.tv_price_list);
				
				txt_catid = (TextView) child
						.findViewById(R.id.tv_category_list);
				
				txt_db_id = (TextView) child
						.findViewById(R.id.tv_db_id_list);
				
				txt_ean = (TextView) child
						.findViewById(R.id.tv_eancode_list);
			
				checkbox = (CheckBox) child.findViewById(R.id.chk);
				//mHolder.checkbox.setChecked(false);
				txt_product = (TextView)child.findViewById(R.id.tv_product_list);
				
				txt_shadeNO = (TextView)child.findViewById(R.id.tv_shade_list);
				
			//	child.setTag(converV);
			checkbox.setTag(new Integer(position));

			checkbox.setOnCheckedChangeListener(null);
				if (checkBoxState[position])
				{
			checkbox.setChecked(true);
				}
					else
					{
			checkbox.setChecked(false);
					}
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

				
			}
			
//			else
//			{
//				mHolder = (ViewHolder) child.getTag();
//			}
			
			
			HashMap<String, String> map = new HashMap<String, String>();
			map = data.get(position);        
			txt_id.setText(map.get("id"));
			txt_db_id.setText(map.get("dbID"));
			txt_catid.setText(map.get("catID"));
			
			txt_ean.setText(map.get("eancode"));
			txt_mrp.setText(map.get("mrp"));
			txt_size.setText(map.get("size"));
			txt_product.setText(map.get("product"));
			txt_shadeNO.setText(map.get("shadeNO"));
			
			 animation.setDuration(500);
			 
	 		 child.startAnimation(animation);
			 
			    animation = null;
		
			    
		return child;
		
		
	}
	
	
	
	


}
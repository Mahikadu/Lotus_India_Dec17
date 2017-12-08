package com.sudesi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.ReportsForUser;


public class ReportAdapter1 extends BaseAdapter {

	
	//private Activity activity;
	Context context1;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	String receiver;

	ViewHolder viewHolder;
	
	ReportsForUser fff;
	
	public ReportAdapter1(Context context, ArrayList<HashMap<String, String>> d) {
		
		context1 = context;
		data=d;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		fff = new ReportsForUser();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	static class ViewHolder {
		TextView category;
		TextView type;
		TextView product;
		//TextView op;
		//TextView sl;
		//TextView rt;
		//TextView cl;
		//TextView size;
		TextView product_status;
		Button btn_change_status;
		
		TextView catid,dbid,enacode;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			Log.e("","check1");
			viewHolder=new ViewHolder();//create new holder
			convertView = inflater.inflate(R.layout.report_layout1, null);//inflater for view
			Log.e("","check4");
			
			viewHolder.category = (TextView)convertView.findViewById(R.id.tv_category); 
			viewHolder.type =(TextView)convertView.findViewById(R.id.tv_type);
			viewHolder.product =(TextView)convertView.findViewById(R.id.tv_product);
			//viewHolder.op = (TextView)convertView.findViewById(R.id.tv_open);
			//viewHolder.sl = (TextView)convertView.findViewById(R.id.tv_sold);
			//viewHolder.rt = (TextView)convertView.findViewById(R.id.tv_return);
		//	viewHolder.cl = (TextView)convertView.findViewById(R.id.tv_close);
		//	viewHolder.size = (TextView)convertView.findViewById(R.id.tv_report_size_tester);
			viewHolder.product_status = (TextView)convertView.findViewById(R.id.tv_product_status);
			
			
			viewHolder.catid = (TextView)convertView.findViewById(R.id.tv_r_catid);
			viewHolder.dbid = (TextView)convertView.findViewById(R.id.tv_r_db_id);
			viewHolder.enacode = (TextView)convertView.findViewById(R.id.tv_r_eanid);
			
			convertView.setTag(viewHolder);
			
			
			
		}else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//display the messages in view
		HashMap<String, String> map = new HashMap<String, String>();
		map = data.get(position);        
		
		
		viewHolder.category.setText(map.get("category"));
		viewHolder.type.setText(map.get("type"));
		viewHolder.product.setText(map.get("product"));
	//	viewHolder.op.setText(map.get("opening"));
	//	viewHolder.sl.setText(map.get("sold"));
	//	viewHolder.rt.setText(map.get("return"));
	//	viewHolder.cl.setText(map.get("closing"));
	//	viewHolder.size.setText(map.get("size"));
		viewHolder.product_status.setText(map.get("product_status"));
		
		final String catid = map.get("product_id");
		final String dbid = map.get("db_id");
		final String eancode = map.get("eancode");
		
		
		
		
		/*viewHolder.btn_change_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Log.v("","butn clikck");
				
				fff.update_tester(catid,dbid,eancode);
			}
		});*/
		return convertView;
		
	}

}

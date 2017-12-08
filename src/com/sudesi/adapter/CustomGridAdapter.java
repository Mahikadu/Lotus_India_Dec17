package com.sudesi.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.sudesi.adapter.NotificationAdapter.ViewHolder;

public class CustomGridAdapter extends BaseAdapter{

	
	private Context context;
    //private final String[] gridValues;
    
    ViewHolder viewHolder;
    
    ArrayList<HashMap<String, String>> data;
    
    Animation animation = null;
 
    //Constructor to initialize values
    public CustomGridAdapter(Context context, // String[ ] gridValues, 
    		ArrayList<HashMap<String, String>> data1) {

        this.context        = context;
      //  this.gridValues     = gridValues;
        this.data = data1;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return data.size();
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	static class ViewHolder {
		TextView textView;
		TextView tvcolor;
		TextView tv_total;
		TextView tvdate;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 LayoutInflater inflater = (LayoutInflater) context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  
//         View gridView = null;
       
         viewHolder=new ViewHolder();
         
         animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_in);

  
         if (convertView == null) {
  
//             gridView = new View(context);
  
             
             // get layout from grid_item.xml ( Defined Below )
             convertView = inflater.inflate( R.layout.boc_grid_item , null);
            
             // set value into textview
              
          //   viewHolder.textView = (TextView) gridView
            //         .findViewById(R.id.tv);
             
             viewHolder.tvcolor = (TextView) convertView
                     .findViewById(R.id.tv_type);
             
             viewHolder.tv_total = (TextView) convertView
                     .findViewById(R.id.tv_total);

             viewHolder.tvdate = (TextView) convertView
                     .findViewById(R.id.tvdate);
             
             convertView.setTag(viewHolder);
           
             //viewHolder.textView.setText(gridValues[position]);
              
         } else {
        	 
        	 viewHolder = (ViewHolder) convertView.getTag();
//             gridView = (View) convertView;
          }
         
         HashMap<String, String> map = new HashMap<String, String>();
 		map = data.get(position);   
 		String m,d,r;
 		
 		if(map.get("Type").toString().equalsIgnoreCase("null")){
 			
 			m="";
 		}else{
 			
 			m= map.get("Type");
 		}
 		if(map.get("Date").toString().equalsIgnoreCase("null")){
 			d="";
 		}else{
 			
 			d=map.get("Date");
 		}
 		if(map.get("Total").toString().equalsIgnoreCase("null")){
 			r="";
 		}else{
 			
 			r=map.get("Total");
 		}

 		
 		viewHolder.tv_total.setText(r);
 		viewHolder.tvcolor.setText(m+"=");
 		viewHolder.tvdate.setText(d);
 		
		//viewholder.tv
		//viewHolder.tvskin.setText(text);
 		
 		  animation.setDuration(500);
			 
 		 convertView.startAnimation(animation);
		 
		    animation = null;
	
  
		return convertView;
	}

}

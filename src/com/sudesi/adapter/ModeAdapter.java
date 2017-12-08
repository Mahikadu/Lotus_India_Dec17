package com.sudesi.adapter;
import com.prod.sudesi.lotusherbalsnew.R;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

public class ModeAdapter extends BaseAdapter {
	private Activity activity;
	private String[] date;
	private String[] LH_salval1;
	private String[] LH_salval2;
	private String[] LM_salval1;
	private String[] LM_salval2;
	private String[] C_salva1;
	private String[] C_salval1;
	
	private static LayoutInflater inflater = null;
	 Animation animation = null;//
	 Context context;//
	  public ModeAdapter (Activity a, String[] date, String[] lsal1, String[] lsal2,
				String[] lmsal1, String [] lmsal2, String[] CM1,
				String[] CM2) {
	 
			context = a;//
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	  
	  }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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

	@Override

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		
		
		if (convertView == null)
			vi = inflater.inflate(R.layout.mode1, null);
		animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_in);//

		
		
			TextView date = (TextView) vi.findViewById(R.id.Date);
			date.setVisibility(View.GONE);
			TextView lh1 = (TextView) vi.findViewById(R.id.lh_s1);
			lh1.setVisibility(View.GONE);
			TextView lh2 = (TextView) vi.findViewById(R.id.lh_s2);
			lh2.setVisibility(View.GONE);
			TextView lm1 = (TextView) vi.findViewById(R.id.lm_s1);
			lm1.setVisibility(View.GONE);
			TextView lm2 = (TextView) vi.findViewById(R.id.lm_s2);
			lm2.setVisibility(View.GONE);
			TextView cm1 = (TextView) vi.findViewById(R.id.CM_s1);
			cm1.setVisibility(View.GONE);
			TextView cm2 = (TextView) vi.findViewById(R.id.CM_s2);
			cm2.setVisibility(View.GONE);
			
			


		
		 animation.setDuration(500);
		 
 		 vi.startAnimation(animation);
		 
		    animation = null;
		return vi;
	}


}

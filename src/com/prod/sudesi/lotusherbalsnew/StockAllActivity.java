package com.prod.sudesi.lotusherbalsnew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dbConfig.Dbcon;
import libs.ConnectionDetector;
import libs.ExceptionHandler;


public class StockAllActivity extends Activity {
    TableLayout tl_sale_calculation;
    TableRow tr;
    TextView titel;
    EditText edt_gross, edt_discount, edt_net;
    String old_stock_recive;
    String str_openingstock = "0";
    String str_price = "0";
    String str_grossamt = "0";
    String str_discount = "0",str_soldstock = "0";
    String str_close_bal = "0";
    String str_stockinhand;
    Button btn_save, btn_back, btn_home, btn_logout;
    int count = 0;
    String sclo = "";
    Dbcon db;
    String old_return;
    TextView edti;
    int current_return;
    String fresh_stock;
    Cursor mCursor = null;
    private static int ecolor;
    private static String namestring, fieldValue;
    private static ForegroundColorSpan fgcspan;
    private static SpannableStringBuilder ssbuilder;
    String rt_stk1 = "0";
    TextView tv_h_username;// -------
    String username;
    SharedPreferences shp;
    SharedPreferences.Editor shpeditor;
    static Context context;
    String enacod[];
    String catid[];
    String pro_name[];
    String size[];
    String old_return_non_salable = "", old_return_salable = "";
    ScrollView scrv_sale;
    String rt_n_s_stk, rt_s_stk;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_stock_all);
        //////////Crash Report
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        tl_sale_calculation = (TableLayout) findViewById(R.id.tl_sale_calculation);
        // scrv_sale = (ScrollView)findViewById(R.id.scrv_sale);

        context = getApplicationContext();
        cd = new ConnectionDetector(context);
        titel = (TextView) findViewById(R.id.textView1);
        titel.setText("" + StockNewActivity.PMODE);
        edt_gross = (EditText) findViewById(R.id.edt_gross);
        edt_discount = (EditText) findViewById(R.id.edt_discount);
        edt_net = (EditText) findViewById(R.id.edt_net);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        db = new Dbcon(StockAllActivity.this);
        shp = context.getSharedPreferences("Lotus", context.MODE_PRIVATE);
        shpeditor = shp.edit();

        Intent intent = getIntent();

        String db_id[] = intent.getStringArrayExtra("db_id");
        pro_name = intent.getStringArrayExtra("pro_name");
        String mrp[] = intent.getStringArrayExtra("mrp");
        String shadeno[] = intent.getStringArrayExtra("shadeNo");
        enacod = intent.getStringArrayExtra("encode");
        catid = intent.getStringArrayExtra("CAT");
        size = intent.getStringArrayExtra("Size");
        // ---------------------
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);

        username = shp.getString("username", "");
        tv_h_username.setText(username);

        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                startActivity(new Intent(StockAllActivity.this,
                        StockNewActivity.class));
            }
        });

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

        for (int i = 0; i < db_id.length; i++) {
            String s = ""
                    + getLastInsertIDofStock1(catid[i], db_id[i], "", mrp[i]);

            if (db_id[i].equalsIgnoreCase("0")) {

            } else {


                tr = new TableRow(this);
                tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                tr.setWeightSum(6f);

                TableRow.LayoutParams lp;
                lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3f);
                TextView productname = new TextView(this);
                productname.setText(pro_name[i]);
                productname.setTextColor(Color.WHITE);
                productname.setMaxEms(12);
                productname.setTextSize(11);
                productname.setMaxLines(3);
                productname.setLayoutParams(lp);
                tr.addView(productname);// add the column to the table row here

                lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                EditText qty = new EditText(this);
                qty.setTextColor(Color.WHITE);
                qty.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
                qty.setSingleLine(true);
                qty.setTextSize(15);
                qty.setMaxEms(5);
                qty.setLayoutParams(lp);
                qty.setGravity(Gravity.CENTER);
                qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                tr.addView(qty);

                qty.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }
                });

                lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                TextView price = new TextView(this);
                price.setText(mrp[i]);
                price.setTextSize(15);
                price.setLayoutParams(lp);
                price.setGravity(Gravity.CENTER);
                price.setTextColor(Color.WHITE);
                tr.addView(price);

                TextView tv2 = new TextView(this);
                tv2.setText(db_id[i]);
                tv2.setGravity(Gravity.CENTER);
                tv2.setVisibility(View.GONE);
                tr.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(shadeno[i]);
                tv3.setGravity(Gravity.CENTER);
                tv3.setVisibility(View.GONE);
                tr.addView(tv3);

                lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                edti = new TextView(this);
                edti.setText(s);
                edti.setTextSize(15);
                edti.setLayoutParams(lp);
                edti.setGravity(Gravity.CENTER);
                edti.setTextColor(Color.WHITE);
                tr.addView(edti);

                tl_sale_calculation.addView(tr,new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                tl_sale_calculation.setShrinkAllColumns(true);


              /*  TextView tv = new TextView(this);
                tv.setText(pro_name[i]);
                tv.setTextColor(Color.WHITE);
//                 tv.setLayoutParams(new WindowManager.LayoutParams(
//                 WindowManager.LayoutParams.FILL_PARENT,
//                 WindowManager.LayoutParams.WRAP_CONTENT));
//                tv.setGravity(Gravity.CENTER);
                tv.setMaxEms(12);
                tv.setTextSize(11);
                tv.setMaxLines(3);
                tr.addView(tv);

                EditText edt = new EditText(this);
                // edt.setText("dsfgdfg");
                edt.setTextColor(Color.WHITE);
                edt.setMaxEms(6);
                tr.addView(edt);
                edt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
                edt.setSingleLine(true);
//                edt.setInputType(android.text.InputType.TYPE_CLASS_TEXT
//                        | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
                edt.setInputType(InputType.TYPE_CLASS_NUMBER);
                edt.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }
                });

                TextView tv1 = new TextView(this);
                tv1.setText(mrp[i]);
                tv1.setTextColor(Color.WHITE);
                tr.addView(tv1);

                TextView tv2 = new TextView(this);
                tv2.setText(db_id[i]);
                tv2.setVisibility(View.GONE);
                tr.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(shadeno[i]);
                tv3.setVisibility(View.GONE);
                tr.addView(tv3);

                edti = new TextView(this);
                edti.setText(s);
                edti.setTextColor(Color.WHITE);
                edti.setMaxEms(5);
                tr.addView(edti);
                //
                tl_sale_calculation.addView(tr);
                tl_sale_calculation.setShrinkAllColumns(true);*/

            }

        }

        // -----------------------------shivani-----------------------------------------

        btn_save.setOnClickListener(new OnClickListener() {
            int rcounter = 0;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (cd.isCurrentDateMatchDeviceDate()) {
                    try {

                        int etcount = 0;
                        int count = 0;
                        boolean negative = false;
                        if (tl_sale_calculation.getChildCount() != 1) {
                            for (int i = 0; i < tl_sale_calculation.getChildCount() - 1; i++) {

                                TableRow t = (TableRow) tl_sale_calculation
                                        .getChildAt(i + 1);
                                EditText edt_qty = (EditText) t.getChildAt(1);
                                TextView tv_dbID = (TextView) t.getChildAt(3);

                                if (edt_qty.getText().toString().trim()
                                        .equalsIgnoreCase("0")
                                        || edt_qty.getText().toString().trim()
                                        .equalsIgnoreCase("")
                                        || edt_qty.getText().toString().trim()
                                        .equalsIgnoreCase(" ")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Please Enter in All Fields",
                                            Toast.LENGTH_SHORT);
                                } else {

                                    if (titel.getText().toString().equalsIgnoreCase("Return to Company")) {
                                        int closBal, enteredQty, difference = 0;
//									int difference = 
                                        db.open();
                                        Cursor cur = db.getuniquedata1("", "", tv_dbID.getText().toString().trim(), "");
                                        if (cur != null && cur.getCount() > 0) {
                                            cur.moveToFirst();
                                            if (isInteger(cur.getString(cur.getColumnIndex("close_bal"))))
                                                closBal = Integer.parseInt(cur.getString(cur.getColumnIndex("close_bal")));
                                            else
                                                closBal = 0;
                                            if (isInteger(edt_qty.getText().toString().trim())) {
                                                enteredQty = Integer.parseInt(edt_qty.getText().toString().trim());
                                            } else {
                                                enteredQty = 0;
                                            }

                                            difference = closBal - enteredQty;
                                        }
                                        if (edt_qty.getText().toString()
                                                .matches("[a-zA-Z ]+")) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Please Enter Only Numbers",
                                                    Toast.LENGTH_SHORT);
                                        }

                                        if (Integer.parseInt(edt_qty.getText()
                                                .toString().trim()) <= 0) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Please Enter in All Fields",
                                                    Toast.LENGTH_SHORT);
                                        } else if (difference <= 0) {
                                            negative = true;
                                        } else {
                                            etcount++;
                                        }
                                    } else {
                                        if (edt_qty.getText().toString()
                                                .matches("[a-zA-Z ]+")) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Please Enter Only Numbers",
                                                    Toast.LENGTH_SHORT);
                                        }

                                        if (Integer.parseInt(edt_qty.getText()
                                                .toString().trim()) <= 0) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Please Enter in All Fields",
                                                    Toast.LENGTH_SHORT);
                                        } else

                                            etcount++;
                                    }


                                    Log.e("etcount", String.valueOf(etcount));
                                }

                            }
                        }

                        int numberofproduct = (tl_sale_calculation.getChildCount() - 1);

                        if (numberofproduct == etcount) {
                            if (tl_sale_calculation.getChildCount() != 1) {

                                count = saveData();

                            }
                            showAlertDialog(count);

                        } else {
                            // mProgress.dismiss();

                            if (titel.getText().toString().equalsIgnoreCase("Return to Company")) {
                                if (negative == true) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            StockAllActivity.this);

                                    // set title
                                    alertDialogBuilder.setTitle("ERROR !!");

                                    // set dialog message
                                    alertDialogBuilder
                                            .setMessage("Closing Balance is going in negative. Do you want to continue ?")
                                            .setCancelable(false)

                                            .setNegativeButton(
                                                    "YES",
                                                    new DialogInterface.OnClickListener() {

                                                        public void onClick(

                                                                DialogInterface dialog,
                                                                int id) {

                                                            dialog.dismiss();

                                                            int count = saveData();
                                                            showAlertDialog(count);
                                                        }
                                                    })

                                            .setPositiveButton(
                                                    "NO",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {

                                                            dialog.dismiss();


                                                        }
                                                    });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder
                                            .create();

                                    // show it
                                    alertDialog.show();
                                } else {
                                    Toast.makeText(
                                            StockAllActivity.this,
                                            "Please fill up all valid value in quantity fields",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(
                                        StockAllActivity.this,
                                        "Please fill up all valid value in quantity fields",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(StockAllActivity.this,
                                "Please enter Only numbers", Toast.LENGTH_LONG)
                                .show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                Toast.makeText(StockAllActivity.this, "Your Handset Date Not Match Current Date", Toast.LENGTH_LONG).show();

            }
            }
        });

        // ----------------------------------------------------------------------------------

    }

    public boolean validateEdit(EditText edt, String errorString,
                                String valueString) {

        Boolean result = false;

        if (valueString != null) {
            if (!valueString.equals("")) {
                if (!valueString.equalsIgnoreCase(" ")) {
                    if (!valueString.equalsIgnoreCase("0")) {
                        if (Integer.parseInt(edt.getText().toString()) > Integer
                                .parseInt(valueString)) {
                            result = false;
                            ecolor = Color.RED; // whatever color you want
                            namestring = errorString;
                            fgcspan = new ForegroundColorSpan(ecolor);
                            ssbuilder = new SpannableStringBuilder(namestring);
                            ssbuilder.setSpan(fgcspan, 0, namestring.length(),
                                    0);
                            edt.setError(ssbuilder);
                        } else {
                            result = true;
                        }
                    } else {
                        result = false;
                        ecolor = Color.RED; // whatever color you want
                        namestring = "No Stock Available";
                        fgcspan = new ForegroundColorSpan(ecolor);
                        ssbuilder = new SpannableStringBuilder(namestring);
                        ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                        edt.setError(ssbuilder);
                    }
                } else {
                    result = false;
                    ecolor = Color.RED; // whatever color you want
                    namestring = "No Stock Available";
                    fgcspan = new ForegroundColorSpan(ecolor);
                    ssbuilder = new SpannableStringBuilder(namestring);
                    ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                    edt.setError(ssbuilder);
                }
            } else {
                result = false;
                ecolor = Color.RED; // whatever color you want
                namestring = "No Stock Available";
                ;
                fgcspan = new ForegroundColorSpan(ecolor);
                ssbuilder = new SpannableStringBuilder(namestring);
                ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                edt.setError(ssbuilder);
            }
        } else {
            result = false;
            ecolor = Color.RED; // whatever color you want
            namestring = errorString;
            fgcspan = new ForegroundColorSpan(ecolor);
            ssbuilder = new SpannableStringBuilder(namestring);
            ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
            edt.setError(ssbuilder);
        }
        return result;
    }

    public static String between(Date date) {
        String bb = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Calendar c = Calendar.getInstance();

            int dayofyear = c.get(Calendar.YEAR);

            // Toast.makeText(context, "year="+dayofyear,
            // Toast.LENGTH_LONG).show();

            Date BOC12 = null;
            Date BOC12a = null;

            Date BOC1 = null;
            Date BOC1a = null;

            Date BOC2 = null;
            Date BOC2a = null;

            Date BOC3 = null;
            Date BOC3a = null;

            Date BOC4 = null;
            Date BOC4a = null;

            Date BOC5 = null;
            Date BOC5a = null;

            Date BOC6 = null;
            Date BOC6a = null;

            Date BOC7 = null;
            Date BOC7a = null;

            Date BOC8 = null;
            Date BOC8a = null;

            Date BOC9 = null;
            Date BOC9a = null;

            Date BOC10 = null;
            Date BOC10a = null;

            Date BOC11 = null;
            Date BOC11a = null;

            String Boc12 = "26/02/" + dayofyear;
            String Boc12a = "25/03/" + dayofyear;

            String Boc1 = "26/03/" + dayofyear;
            String Boc1a = "25/04/" + dayofyear;

            String Boc2 = "26/04/" + dayofyear;
            String Boc2a = "25/05/" + dayofyear;

            String Boc3 = "26/05/" + dayofyear;
            String Boc3a = "25/06/" + dayofyear;

            String Boc4 = "26/06/" + dayofyear;
            String Boc4a = "25/07/" + dayofyear;

            String Boc5 = "26/07/" + dayofyear;
            String Boc5a = "25/08/" + dayofyear;

            String Boc6 = "26/08/" + dayofyear;
            String Boc6a = "25/09/" + dayofyear;

            String Boc7 = "26/09/" + dayofyear;
            String Boc7a = "25/10/" + dayofyear;

            String Boc8 = "26/10/" + dayofyear;
            String Boc8a = "25/11/" + dayofyear;

            String Boc9 = "26/11/" + dayofyear;
            String Boc9a = "25/12/" + dayofyear;

            String Boc10 = "26/12/" + dayofyear;

            String Boc10a = "25/01/" + dayofyear + 1;

            String Boc11 = "26/01/" + dayofyear + 1;
            String Boc11a = "25/02/" + dayofyear + 1;

            try {
                BOC12 = sdf.parse(Boc12);
                BOC12a = sdf.parse(Boc12a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                BOC1 = sdf.parse(Boc1);
                BOC1a = sdf.parse(Boc1a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC2 = sdf.parse(Boc2);
                BOC2a = sdf.parse(Boc2a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC3 = sdf.parse(Boc3);
                BOC3a = sdf.parse(Boc3a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC4 = sdf.parse(Boc4);
                BOC4a = sdf.parse(Boc4a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC5 = sdf.parse(Boc5);
                BOC5a = sdf.parse(Boc5a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC6 = sdf.parse(Boc6);
                BOC6a = sdf.parse(Boc6a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC7 = sdf.parse(Boc7);
                BOC7a = sdf.parse(Boc7a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC8 = sdf.parse(Boc8);
                BOC8a = sdf.parse(Boc8a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC9 = sdf.parse(Boc9);
                BOC9a = sdf.parse(Boc9a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC10 = sdf.parse(Boc10);
                BOC10a = sdf.parse(Boc10a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                BOC11 = sdf.parse(Boc11);
                BOC11a = sdf.parse(Boc11a);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (date.after(BOC12) && date.before(BOC12a) || date.equals(BOC12a)
                    || date.equals(BOC12)) {

                bb = "BOC12";
                // Toast.makeText(context, "boc12 = "+bb,
                // Toast.LENGTH_LONG).show();
            }

            if (date.after(BOC1) && date.before(BOC1a) || date.equals(BOC1a)
                    || date.equals(BOC1)) {

                bb = "BOC1";
                // Toast.makeText(context, "boc1 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC2) && date.before(BOC2a) || date.equals(BOC2a)
                    || date.equals(BOC2)) {

                bb = "BOC2";
                // Toast.makeText(context, "boc2 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC3) && date.before(BOC3a) || date.equals(BOC3a)
                    || date.equals(BOC3)) {

                bb = "BOC3";
                // Toast.makeText(context, "boc3 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC4) && date.before(BOC4a) || date.equals(BOC4a)
                    || date.equals(BOC4)) {

                bb = "BOC4";
                // Toast.makeText(context, "boc4 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC5) && date.before(BOC5a) || date.equals(BOC5a)
                    || date.equals(BOC5)) {

                bb = "BOC5";
                // Toast.makeText(context, "boc5 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC6) && date.before(BOC6a) || date.equals(BOC6a)
                    || date.equals(BOC6)) {

                bb = "BOC6";
                // Toast.makeText(context, "boc6 = "+bb,
                // Toast.LENGTH_LONG).show();
            }

            if (date.after(BOC7) && date.before(BOC7a) || date.equals(BOC7a)
                    || date.equals(BOC7)) {

                bb = "BOC7";
                // Toast.makeText(context, "boc7 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC8) && date.before(BOC8a) || date.equals(BOC8a)
                    || date.equals(BOC8)) {

                bb = "BOC8";
                // Toast.makeText(context, "boc8 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC9) && date.before(BOC9a) || date.equals(BOC9a)
                    || date.equals(BOC9)) {

                bb = "BOC10";
                // Toast.makeText(context, "boc10 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC10) && date.before(BOC10a) || date.equals(BOC10a)
                    || date.equals(BOC10)) {

                bb = "BOC10";
                // Toast.makeText(context, "boc10 = "+bb,
                // Toast.LENGTH_LONG).show();
            }
            if (date.after(BOC11) && date.before(BOC11a) || date.equals(BOC11a)
                    || date.equals(BOC11)) {

                bb = "BOC11";
                // Toast.makeText(context, "boc11 = "+bb,
                // Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bb;
    }

    @SuppressLint("SimpleDateFormat")
    public String getLastInsertIDofStock1(String catid, String dbid,
                                          String eanid, String PRICE) {

        String catid1 = "", eanid1 = "", dbid1 = "", closebal = "", totalamount = "", sold = "", retn = "", retn_n_salebale = "", total_net_amt = "", price = "";
        ;
        String last_modified_date;
        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String date = sdf.format(c.getTime());

        // 28.04.2015
        String[] day = date.split("-");

        String day1 = day[2];

        // 28.04.2015
        db.open();
        mCursor = db.getuniquedata1(catid, eanid, dbid, date);

        int count = mCursor.getCount();

        if (mCursor != null) {

            if (mCursor.moveToFirst()) {

                do {

                    closebal = mCursor.getString(mCursor
                            .getColumnIndex("close_bal"));


                    totalamount = mCursor.getString(mCursor
                            .getColumnIndex("total_gross_amount"));

                    catid1 = mCursor.getString(mCursor
                            .getColumnIndex("product_id"));

                    eanid1 = mCursor.getString(mCursor
                            .getColumnIndex("eancode"));

                    dbid1 = mCursor.getString(mCursor.getColumnIndex("db_id"));

                    sold = mCursor.getString(mCursor
                            .getColumnIndex("sold_stock"));

                    old_return_salable = mCursor.getString(mCursor
                            .getColumnIndex("return_saleable"));

                    old_return_non_salable = mCursor.getString(mCursor
                            .getColumnIndex("return_non_saleable"));

                    Log.e("dbold_return_nonsalable", old_return_non_salable);

                    total_net_amt = mCursor.getString(mCursor
                            .getColumnIndex("total_net_amount"));

                    old_stock_recive = mCursor.getString(mCursor
                            .getColumnIndex("stock_received"));


                    str_stockinhand = mCursor.getString(mCursor
                            .getColumnIndex("stock_in_hand"));

                    str_openingstock = mCursor.getString(mCursor
                            .getColumnIndex("opening_stock"));


                } while (mCursor.moveToNext());

                sclo = "" + closebal;// 28.04.2015//19.05.2015//23.05.2015//25.05.2015//22.06.2015
                old_return = "" + retn;
//				old_return_non_salable = "" + retn_n_salebale;
                // previous_bal.setText(str_openingstock);//19.05.2015.

                // previous_bal.setText(str_stockinhand);//23.05.2015//25.05.2015//22.06.2015

                // total_amt.setText(totalamount);
                // previous_total_amount.setText(totalamount);

                // // previous_return_items.setText(retn);
                // previous_sold_items.setText(sold);
                // prvious_retrun_non_saleable.setText(retn_n_salebale);
                // previous_net_total_amount.setText(total_net_amt);
                // tv_show_price.setText(PRICE);
                // tv_dbid.setText(dbid);

            } else {

                sclo = "0";// 28.04.2015//19.05.2015//23.05.2015//25.05.2015//22.06.2015
                old_return = "0";
                old_return_non_salable = "0";

            }

        }
        db.close();

        // ll.setVisibility(View.GONE);
        return closebal;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

    }

    public boolean isInteger(String str) {
        boolean result = true;
        try {
            int i = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;

    }


    public int saveData() {

        int count = 0;
        for (int i = 0; i < tl_sale_calculation
                .getChildCount() - 1; i++) {


            TableRow t = (TableRow) tl_sale_calculation
                    .getChildAt(i + 1);
            EditText edt_qty = (EditText) t.getChildAt(1);
            TextView tv_mrp = (TextView) t.getChildAt(2);
            TextView tv_dbID = (TextView) t.getChildAt(3);
            TextView tv_shadeno = (TextView) t
                    .getChildAt(4);
            TextView openingbal = (TextView) t
                    .getChildAt(5);

            String s = ""
                    + getLastInsertIDofStock1("", tv_dbID
                    .getText().toString(), "", "");

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String insert_timestamp = sdf.format(c
                    .getTime());

            String product_category = StockNewActivity.selected_product_category;

            String product_type1 = StockNewActivity.selected_product_type;
            // String product_name =
            // selected_product_name;//
            // selected_product_name;

            String product_name = pro_name[i];// changed
            // 06.12.2014

            String opt = titel.getText().toString();
            Log.e("Title", titel.getText().toString());

            if (opt.equalsIgnoreCase("Stock Received")) {
                fresh_stock = edt_qty.getText().toString();
                rt_n_s_stk = "0";
                rt_s_stk = "0";

                Log.e("fresh_stock", fresh_stock);
                Log.e("rt_n_s_stk", rt_n_s_stk);
                Log.e("rt_s_stk", rt_s_stk);

            } else if (opt
                    .equalsIgnoreCase("Return From Customer")) {
                fresh_stock = "0";
                rt_n_s_stk = "0";
                rt_s_stk = edt_qty.getText().toString();

                Log.e("fresh_stock", fresh_stock);
                Log.e("rt_n_s_stk", rt_n_s_stk);
                Log.e("rt_s_stk", rt_s_stk);


            } else if (opt
                    .equalsIgnoreCase("Return to Company")) {
                fresh_stock = "0";
                rt_n_s_stk = edt_qty.getText().toString();
                rt_s_stk = "0";

                Log.e("fresh_stock", fresh_stock);
                Log.e("rt_n_s_stk", rt_n_s_stk);
                Log.e("rt_s_stk", rt_s_stk);


            }
            if (old_stock_recive != null) {

                if (old_stock_recive.equalsIgnoreCase("")
                        || old_stock_recive
                        .equalsIgnoreCase("null")
                        || old_stock_recive
                        .equalsIgnoreCase(" ")) {

                    old_stock_recive = "0";
                }
            } else {
                old_stock_recive = "0";
            }

            Log.e("old_stock_recive", old_stock_recive);

            if (old_return_salable != null) {

                if (old_return_salable.equalsIgnoreCase("")
                        || old_return_salable
                        .equalsIgnoreCase("null")
                        || old_return_salable
                        .equalsIgnoreCase(" ")) {

                    old_return_salable = "0";
                }
            } else {
                old_return_salable = "0";
            }

            Log.e("old_return_salable", old_return_salable);


            if (old_return_non_salable != null) {

                if (old_return_non_salable
                        .equalsIgnoreCase("")
                        || old_return_non_salable
                        .equalsIgnoreCase("null")
                        || old_return_non_salable
                        .equalsIgnoreCase(" ")) {

                    Log.e("if-statement", "executed");

                    old_return_non_salable = "0";
                }
            } else {
                old_return_non_salable = "0";
                Log.e("else-statement", "executed");
            }

            Log.e("old_return_non_salable", old_return_non_salable);


            int new_fresh_stock = Integer
                    .parseInt(fresh_stock)
                    + Integer.parseInt(old_stock_recive);

            int new_retrn_sale = Integer.parseInt(rt_s_stk)
                    + Integer.parseInt(old_return_salable);

            int new_retrn_non_sale = Integer
                    .parseInt(rt_n_s_stk)
                    + Integer
                    .parseInt(old_return_non_salable);

            Log.e("new_fresh_stock", String.valueOf(new_fresh_stock));
            Log.e("new_retrn_sale", String.valueOf(new_retrn_sale));
            Log.e("new_retrn_non_sale", String.valueOf(new_retrn_non_sale));


            String emp_id = shp.getString("username", "");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat(
                    "MMMM");
            String month_name = month_date.format(cal
                    .getTime());

            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat year_format = new SimpleDateFormat(
                    "yyyy");
            String year_name = year_format.format(cal1
                    .getTime());

            String price = tv_mrp.getText().toString()
                    .trim();

            if (price.equalsIgnoreCase("")) {
                price = "0";
            }
            String eancode = "" + enacod[i];

            String db_id1 = tv_dbID.getText().toString()
                    .trim();

            String cat_id = catid[i];

            String size1 = "" + size[i];

            String shadenon = tv_shadeno.getText()
                    .toString().trim();

            if (shadenon.equalsIgnoreCase("")
                    || shadenon.equalsIgnoreCase("null")) {

                shadenon = "";
            }

            String solddd = "0";

            db.open();
            Cursor cur = db.fetchone(tv_dbID.getText()
                            .toString(), "stock", new String[]{
                            "sold_stock", "total_gross_amount",
                            "total_net_amount", "discount"},
                    "db_id");
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();

                if (cur.getString(0) != null) {

                    if (cur.getString(0).equalsIgnoreCase(
                            " ")
                            || cur.getString(0)
                            .equalsIgnoreCase("")) {
                        solddd = "0";
                    } else {
                        solddd = cur.getString(0)
                                .toString();
                    }
                } else {
                    solddd = "0";


                }

            } else {

                solddd = "0";


            }

            Log.e("solddd", solddd);


            // }
            db.close();

            Cursor mCursor;
            db.open();
            mCursor = db.getuniquedata1("", "", tv_dbID
                    .getText().toString(), "");


            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                str_openingstock = mCursor.getString(mCursor
                        .getColumnIndex("opening_stock"));
                if (str_openingstock != null) {

                    if (str_openingstock.equals("")) {

                        str_openingstock = "0";
                    }

                } else {
                    str_openingstock = "0";
                }

            } else {
                str_openingstock = "0";
            }

            Log.e("str_openingstock", str_openingstock);

            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                str_price = mCursor.getString(mCursor
                        .getColumnIndex("price"));
                if (str_price != null) {

                    if (str_price.equals("")) {

                        str_price = "0";
                    }

                } else {
                    str_price = "0";
                }

            } else {
                str_price = "0";
            }

            Log.e("str_price", str_price);

            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                str_grossamt = mCursor.getString(mCursor
                        .getColumnIndex("total_gross_amount"));
                if (str_grossamt != null) {

                    if (str_grossamt.equals("")) {

                        str_grossamt = "0";
                    }

                } else {
                    str_grossamt = "0";
                }

            } else {
                str_grossamt = "0";
            }

            Log.e("str_price", str_grossamt);

            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                str_discount = mCursor.getString(mCursor
                        .getColumnIndex("discount"));
                if (str_discount != null) {

                    if (str_discount.equals("")) {

                        str_discount = "0.0";
                    }

                } else {
                    str_discount = "0.0";
                }

            } else {
                str_discount = "0.0";
            }

            Log.e("str_discount", str_discount);

            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                str_soldstock = mCursor.getString(mCursor
                        .getColumnIndex("sold_stock"));
                if (str_soldstock != null) {

                    if (str_soldstock.equals("")) {

                        str_soldstock = "0";
                    }

                } else {
                    str_soldstock = "0";
                }

            } else {
                str_soldstock = "0";
            }

            Log.e("str_soldstock", str_soldstock);

            int soldstock = Integer.parseInt(str_soldstock) - new_retrn_sale;

            int pricecustomer = Integer.parseInt(str_price) * new_retrn_sale;

            int pricesold = Integer.parseInt(str_price) * Integer.parseInt(str_soldstock);

            int i_net_amt = pricesold - pricecustomer;

            float net_amt = Float.parseFloat(String.valueOf(i_net_amt))- Float.parseFloat(str_discount);

            //--------Old production apk use
            int i_stkinand = Integer
					.parseInt(str_openingstock)
					+ new_fresh_stock
					+ new_retrn_sale
					- new_retrn_non_sale;

            int i_stock_inand = Integer
                    .parseInt(str_openingstock)
                    + new_fresh_stock;

            Log.e("i_stkinand", String.valueOf(i_stkinand));

            //--------Old production apk use
			int i_close = i_stkinand- soldstock;

			Log.e("i_close", String.valueOf(i_close));

            if (mCursor.getCount() == 0) {

                db.open();
                db.Insertstock_new(
                        product_category,
                        product_type1,
                        product_name,
                        emp_id,
                        String.valueOf(i_stock_inand),
                        String.valueOf(i_close),
                        String.valueOf(new_fresh_stock),
                        price,
                        size1,
                        eancode,
                        db_id1,
                        cat_id,
                        insert_timestamp,
                        String.valueOf(soldstock),
                        String.valueOf(new_retrn_sale),
                        String.valueOf(new_retrn_non_sale),
                        String.valueOf(i_net_amt),
                        String.valueOf(net_amt),
                        str_discount,
                        shadenon, insert_timestamp,
                        month_name, year_name);
                db.close();

                Toast.makeText(StockAllActivity.this,
                        "Data save successfully",
                        Toast.LENGTH_SHORT).show();

                count++;

            } else {
                db.open();
                db.UpdateStock_new(

                        product_category, product_type1,
                        product_name, emp_id,
                        String.valueOf(i_stock_inand),
                        String.valueOf(i_close),

                        String.valueOf(new_fresh_stock),

                        price, size1, eancode, db_id1,
                        cat_id, insert_timestamp,
                        String.valueOf(soldstock),
                        String.valueOf(new_retrn_sale),
                        String.valueOf(new_retrn_non_sale),
                        String.valueOf(i_net_amt),
                        String.valueOf(net_amt),
                        str_discount,
                        shadenon, insert_timestamp,
                        month_name, year_name);
                db.close();

                // Toast.makeText(StockAllActivity.this,
                // "Data save successfully",
                // Toast.LENGTH_SHORT).show();
                count++;
            }

        }

        return count;

    }

    public void showAlertDialog(int c) {
        if (c == tl_sale_calculation.getChildCount() - 1) {

            // mProgress.dismiss();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    StockAllActivity.this);

            // set title
            alertDialogBuilder.setTitle("Saved Successfully!!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Go  TO  :")
                    .setCancelable(false)

                    .setNegativeButton(
                            "Stock Page",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();
//*******************
                                    finish();
                                    startActivity(new Intent(
                                            StockAllActivity.this,
                                            StockNewActivity.class));
                                }
                            })

                    .setPositiveButton(
                            "Home",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();

                                    finish();
                                    startActivity(new Intent(
                                            StockAllActivity.this,
                                            DashboardNewActivity.class));

                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder
                    .create();

            // show it
            alertDialog.show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    StockAllActivity.this);

            // set title
            alertDialogBuilder.setTitle("Data Not Saved!!!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Problem with Insert")
                    .setCancelable(false)

                    .setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();

                                    finish();
                                    startActivity(new Intent(
                                            StockAllActivity.this,
                                            StockNewActivity.class));

                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder
                    .create();

            // show it
            alertDialog.show();
        }
    }

}

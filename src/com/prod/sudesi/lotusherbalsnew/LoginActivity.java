package com.prod.sudesi.lotusherbalsnew;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dbConfig.Dbcon;
import dbConfig.Dbhelper;
import libs.ConnectionDetector;
import libs.ExceptionHandler;
import libs.LotusWebservice;

@SuppressLint("SimpleDateFormat")
public class LoginActivity extends Activity {

    String serverdate;
    Button btn_login;
    LocationInfo locationInfo;
    EditText edt_username;
    EditText edt_password;
    int logCounter = 0;
    int sizeCounter = 0;
    int insertCounter = 0;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    String alaram_flag = "false";

    LotusWebservice service;
    String username = "", pass = "", VERSION_NAME = "", OS_VERSION = "";

    private ProgressDialog pd;
    ConnectionDetector cd;
    private double lon = 0.0, lat = 0.0;
    Dbcon db;

    Context context;

    String month;
    String year;

    //
    Cursor attendance_array, image_array, image_array1, test_array,
            stock_array;
    private ProgressDialog mProgress = null;
    int soapresultforvisibilityid;
    String producttype;

    //
    String get_server_date = "";
    String server_date, todaydate1;

    // WriteErroLogs we;

    String div;
    String status;

    String deviceId = "";
    String loginstaus = "";
    String LogoutStatus = "";
    private static final int LOCATION_PERMISSION_ID = 1001;

    //Production
   /* public static final String  downloadURL = "http://lotussmartforce.com/apk/Lotus_Pro.apk"; //production
    public static final String downloadConfigFile = "http://lotussmartforce.com/apk/config.txt";//production*/

    //UAT
    public static final String downloadURL = "http://lotussmartforce.com/UATAPK/Lotus_UAT.apk"; //UAT
    public static final String downloadConfigFile = "http://lotussmartforce.com/UATAPK/config.txt";//UAT


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        //////////Crash Report
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));


        btn_login = (Button) findViewById(R.id.btn_login);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

        context = getApplicationContext();
        db = new Dbcon(context);
        cd = new ConnectionDetector(LoginActivity.this);
        sp = getSharedPreferences("Lotus", MODE_PRIVATE);
        spe = sp.edit();

        pd = new ProgressDialog(LoginActivity.this);
        mProgress = new ProgressDialog(LoginActivity.this);
        service = new LotusWebservice(LoginActivity.this);

        Calendar c = Calendar.getInstance();
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);

        month = String.valueOf(month1 + 1);
        year = String.valueOf(year1);

       /* Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("LogoutFlag") != null) {
                LogoutStatus = intent.getStringExtra("LogoutFlag");
                if(LogoutStatus.equals("LOGOUTSERVICE")){
                    disableBroadcastReceiver();
                }
            }
        }*/


        // we = new WriteErroLogs(getApplicationContext());

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();

        exportDB();
        AutologoutBroadcast();
        refreshDisplay();
        btn_login.setOnClickListener(new OnClickListener() {

            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //LoginUser();
                //if(isPermissionGranted()) {
                    try {
                        if (cd.isConnectingToInternet()) {

                            username = edt_username.getText().toString()
                                    .toUpperCase();
                            pass = edt_password.getText().toString();

                            PackageInfo info = null;
                            PackageManager manager = getPackageManager();
                            info = manager.getPackageInfo(getPackageName(), 0);

                            String packageName = info.packageName;
                            int versionCode = info.versionCode;
                            VERSION_NAME = info.versionName;
                            OS_VERSION = String.valueOf(android.os.Build.VERSION.SDK_INT);

                            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(VERSION_NAME)) {
                                // TODO check apk version
                                new SyncApkCheck().execute();

                            } else {
                                Toast.makeText(getApplicationContext(), "Fields Cannot be Empty", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
              //  }

            }

        });

    }

    private void DataUploadAlaramReceiver() {

        try {
            Intent intentAlarm = new Intent(this, UploadDataBrodcastReceiver.class);

            boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, intentAlarm, PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmRunning == false) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, intentAlarm, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 60 * 120, pendingIntent);
            }

            // create the object
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            //set the alarm for particular time
//            //  alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 15, PendingIntent.getBroadcast(this, 0, intentAlarm, 0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("WrongConstant")
    private void Boc26AlaramReceiver() {

        try {

            //Create pending intent & register it to your alarm notifier class
            Intent intent = new Intent(this, BocBroadcastReceiver.class);
            //intent.putExtra("Boc", true);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Log.v("jflksdjfk", "started");
            //set timer you want alarm to work (here I have set it to 24.00)
            Calendar calendar = Calendar.getInstance();
            Calendar setcalendar = Calendar.getInstance();
            setcalendar.setTimeInMillis(System.currentTimeMillis());
            setcalendar.set(Calendar.HOUR_OF_DAY, 6);
            setcalendar.set(Calendar.MINUTE, 0);
            setcalendar.set(Calendar.SECOND, 0);
            setcalendar.set(Calendar.DAY_OF_MONTH, 16);

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            // get current date time with Date()
            String currentDate = dateFormat.format(calendar.getTime()).split(" ")[0];
            db.open();
            String syncDate = db.getLastSyncDate("stock").split(" ")[0];
            db.close();
            if (syncDate == null) {
                syncDate = "";
            }

            /*Calendar now = Calendar.getInstance();
            int currentMonth = now.get(calendar.MONTH);
            int currentYear = now.get(Calendar.YEAR);

            String boc26date = String.valueOf(currentMonth)+"/15/"+String.valueOf(currentYear);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            Date bocdate = format.parse(boc26date);
            Date current = format.parse(currentDate);*/

            if (calendar.get(Calendar.DAY_OF_MONTH) == 16 && (sp.getBoolean("BOC26", false) || !currentDate.equals(syncDate))) {
                boolean boc26 = true;

                spe.putBoolean("BOC26", boc26);
                spe.commit();
            }

            AlarmManager mAlarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //set that timer as a RTC Wakeup to alarm manager object
            mAlarmManger.setRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            //Create alarm manager

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void exportDB() {

//        File sd = new File(Environment.getExternalStorageDirectory(),"/Android");
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "//data/" + "com.prod.sudesi.lotusherbalsnew" + "/databases/" + Dbhelper.DATABASE_NAME;
        String backupDBPath = Dbhelper.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
//            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("DefaultLocale")
    public class Check_Login extends AsyncTask<Void, Void, SoapObject> {

        ContentValues contentvalues = new ContentValues();
        private SoapObject soap_result = null;
        // private SoapPrimitive server_date_result = null;
        private SoapPrimitive soap_result2 = null;

        String Flag = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            pd.setMessage("Please Wait....");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            Flag = "0";
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                // Toast.makeText(getActivity(),"Check Your Internet Connection!!!",
                // Toast.LENGTH_LONG).show();

                Flag = "0";

                // stop executing code by return

            } else {

                try {

                    PackageInfo pInfo = getPackageManager().getPackageInfo(
                            getPackageName(), 0);
                    String version = pInfo.versionName;

                    soap_result = service.GetLogin(username, pass, version);

                    if (soap_result.equals("anyType{}")) {

                        // Toast.makeText(LoginActivity.this,
                        // "User not activated ,Please contact admin",
                        // Toast.LENGTH_SHORT).show();
                        Flag = "5";
                    } else {

                        if (soap_result != null) {

                            for (int i = 0; i < soap_result
                                    .getPropertyCount(); i++) {

                                SoapObject getmessaage = (SoapObject) soap_result
                                        .getProperty(i);

                                if (String.valueOf(
                                        getmessaage.getProperty("status"))
                                        .equals("INACTIVE")) {

                                    Flag = "INACTIVE";

                                } else if (String.valueOf(
                                        getmessaage.getProperty("status"))
                                        .equals("N")) {

                                    Flag = "N";

                                } else if (String.valueOf(
                                        getmessaage.getProperty("status"))
                                        .equalsIgnoreCase("Expired")) {
                                    Flag = "Expired";
                                }
                                // 27.03.2015
                                else if (String.valueOf(
                                        getmessaage.getProperty("status"))
                                        .equalsIgnoreCase("SE")) {
                                    Flag = "SE";
                                } else if (String.valueOf(
                                        getmessaage.getProperty("status"))
                                        .equalsIgnoreCase("V")) {
                                    Flag = "V";
                                }
                                // 27.03.2015

                                else {

                                    status = String.valueOf(getmessaage
                                            .getProperty("status"));

                                    if (String.valueOf(getmessaage
                                            .getProperty("div")) != null) {

                                        div = String.valueOf(getmessaage
                                                .getProperty("div"));
                                        if (div.contentEquals("anyType{}")) {
                                            div = "";
                                        }
                                        spe.putString("div", div);
                                        spe.commit();

                                    }

                                    if (String.valueOf(getmessaage
                                            .getProperty("username")) != null) {

                                        spe.putString(

                                                "username",
                                                String.valueOf(getmessaage
                                                        .getProperty("username")));
                                        spe.putString(
                                                "BDEusername",
                                                String.valueOf(getmessaage
                                                        .getProperty("bdename")));
                                        spe.putString(
                                                "BDE_Code",
                                                String.valueOf(getmessaage
                                                        .getProperty("bdecode")));
                                        spe.commit();

                                        Flag = String.valueOf(getmessaage
                                                .getProperty("username"));

											/*
                                             * final Calendar calendar =
											 * Calendar .getInstance();
											 * SimpleDateFormat formatter = new
											 * SimpleDateFormat(
											 * "MM/dd/yyyy HH:mm:ss"); String
											 * Createddate =
											 * formatter.format(calendar
											 * .getTime());
											 */

                                        contentvalues.put("username",
                                                username);
                                        contentvalues.put("password", pass);
                                        contentvalues.put(
                                                "bde_Name",
                                                getmessaage.getProperty(
                                                        "bdename")
                                                        .toString());
                                        contentvalues.put(
                                                "bde_Code",
                                                getmessaage.getProperty(
                                                        "bdecode")
                                                        .toString());

                                        contentvalues
                                                .put("status",
                                                        String.valueOf(getmessaage
                                                                .getProperty("status")));

                                        loginstaus = "true";
                                        soap_result2 = service.setDeviceId(
                                                username, deviceId);

                                        if (soap_result2 != null) {
                                            if (soap_result2.toString()
                                                    .equalsIgnoreCase(
                                                            "success")) {

                                                contentvalues.put(
                                                        "android_uid",
                                                        deviceId);

                                                // contentvalues.put("ACTIVE",1);

                                                db.open();
                                                String ddd = db.check_password_from_db(
                                                        username,
                                                        pass);
                                                db.close();

                                                if (ddd.equalsIgnoreCase("1")) {
                                                    contentvalues
                                                            .put("last_modified_date",
                                                                    serverdate);
                                                    db.open();
                                                    db.updatevalues(
                                                            "login",
                                                            contentvalues,
                                                            "username",
                                                            username);
                                                    db.close();
                                                    loginstaus = "valid_user";

                                                } else {

                                                    // db.insertValues(contentvalues,"login");
                                                    db.open();

                                                    db.insertLogin(
                                                            username,
                                                            pass,
                                                            deviceId,
                                                            serverdate,
                                                            "f",
                                                            div,
                                                            status,
                                                            getmessaage
                                                                    .getProperty(
                                                                            "bdename")
                                                                    .toString(),
                                                            getmessaage
                                                                    .getProperty(
                                                                            "bdecode")
                                                                    .toString());
                                                    db.close();

                                                }
                                                loginstaus = "true1";
                                                checkAndSaveMonthly();

                                            } else if (soap_result2
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "false")) {
                                                loginstaus = "true2";

                                            } else if (soap_result2
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "EXIST")) {

                                                loginstaus = "true3";

                                            } else if (soap_result2
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "NotActive")) {
                                                loginstaus = "NotActive";
                                            } else if (soap_result2
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "NoKey")) {
                                                loginstaus = "NoKey";
                                            } else if (soap_result2
                                                    .toString()
                                                    .equalsIgnoreCase(
                                                            "success")) {
                                                loginstaus = "success";
                                            } else if (soap_result2
                                                    .toString()
                                                    .equalsIgnoreCase("SE")) {
                                                loginstaus = "true4";

                                                final Calendar calendar = Calendar
                                                        .getInstance();
                                                SimpleDateFormat formatter = new SimpleDateFormat(
                                                        "MM/dd/yyyy HH:mm:ss");
                                                String Createddate = formatter
                                                        .format(calendar
                                                                .getTime());

                                                int n = Thread
                                                        .currentThread()
                                                        .getStackTrace()[2]
                                                        .getLineNumber();
                                                db.open();
                                                db.insertSyncLog(
                                                        "SE error in GetLogin()",
                                                        String.valueOf(n),
                                                        "GetLogin()",
                                                        Createddate,
                                                        Createddate,
                                                        sp.getString(
                                                                "username",
                                                                ""),
                                                        "Login Check",
                                                        "Fail");
                                                db.close();

                                            }
                                        }

                                    }

                                }

                            }
                        } else {
                            Flag = "SOUP NULL";
                            String errors = "Login soap giving null response. GetLogin Method";
                            // we.writeToSD(errors.toString());
                            final Calendar calendar = Calendar
                                    .getInstance();
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "MM/dd/yyyy HH:mm:ss");
                            String Createddate = formatter.format(calendar
                                    .getTime());

                            int n = Thread.currentThread().getStackTrace()[2]
                                    .getLineNumber();
                            db.open();
                            db.insertSyncLog(
                                    "Soup is Null While GetLogin()",
                                    String.valueOf(n), "GetLogin()",
                                    Createddate, Createddate,
                                    sp.getString("username", ""),
                                    "Login Check", "Fail");
                            db.close();

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub

            if ((pd != null) && pd.isShowing()) {
                pd.dismiss();
            }

            try {
                if (soap_result != null) {
                    if (!Flag.equalsIgnoreCase("0")) {

                        if (!Flag.equalsIgnoreCase("5")) {

                            if (!Flag.equalsIgnoreCase("SE")) {

                                if (Flag.toString().equalsIgnoreCase(
                                        "INACTIVE")) {

                                    Toast.makeText(LoginActivity.this,
                                            "User is Inactive",
                                            Toast.LENGTH_SHORT).show();

                                } else if (Flag.toString()
                                        .equalsIgnoreCase("N")) {

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Enter Correct UserName and Password.",
                                            Toast.LENGTH_SHORT).show();
                                } else if (Flag.equalsIgnoreCase("Expired")) {
                                    Toast.makeText(LoginActivity.this,
                                            "Activation Key is Expired",
                                            Toast.LENGTH_SHORT).show();
                                } else if (Flag.equalsIgnoreCase("V")) {
                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Please Update to Newer Version!",
                                            Toast.LENGTH_SHORT).show();
                                } else if (loginstaus
                                        .equalsIgnoreCase("true3")) {

                                    db.open();
                                    String userid = db.getUserId();
                                    db.close();

                                    if (userid.equalsIgnoreCase("no")) {

                                        Toast.makeText(
                                                LoginActivity.this,
                                                "This device is already assigned to another user.Please contact your system administrator!",
                                                Toast.LENGTH_LONG).show();

                                    } else {

                                        Toast.makeText(
                                                LoginActivity.this,
                                                "This device is already assigned to another user.Please contact your system administrator!. Already Login Id = "
                                                        + userid,
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else if (loginstaus
                                        .equalsIgnoreCase("true2")) {

                                    Toast.makeText(LoginActivity.this,
                                            "Invalid User!!",
                                            Toast.LENGTH_SHORT).show();
                                } else if (loginstaus
                                        .equalsIgnoreCase("true4")) {

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Server Error! Please try again!!",
                                            Toast.LENGTH_SHORT).show();
                                } else if (loginstaus
                                        .equalsIgnoreCase("NotActive")) {
                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Activation Key is Not Updated",
                                            Toast.LENGTH_LONG).show();
                                } else if (loginstaus
                                        .equalsIgnoreCase("NoKey")) {
                                    Toast.makeText(LoginActivity.this,
                                            "No Activation Key",
                                            Toast.LENGTH_LONG).show();
                                } else if (loginstaus
                                        .equalsIgnoreCase("success")) {

                                    String[] serverdatearray = serverdate
                                            .split(" ");

                                    server_date = serverdatearray[0];

                                    String[] serverdate1 = server_date
                                            .split("/");


                                    spe.putString("currentdate", serverdate);
                                    spe.commit();

                                    SimpleDateFormat sdf = new SimpleDateFormat(
                                            "MM/dd/yyyy");

                                    Date curntdte = null;
                                    try {
                                        curntdte = sdf.parse(server_date);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    sdf.applyPattern("yyyy-MM-dd");
                                    todaydate1 = sdf.format(curntdte);

                                    spe.putString("todaydate", todaydate1);
                                    spe.commit();

                                    //SetClosingISOpeningOnlyOnce();
                                    // checkAndSaveMonthly();
                                    checkpresentornot(todaydate1);
                                    // checkAndSaveMonthly();

                                } else {

                                    String[] serverdatearray = serverdate
                                            .split(" ");

                                    server_date = serverdatearray[0];

                                    String[] serverdate1 = server_date
                                            .split("/");

                                    spe.putString("current_year",
                                            serverdate1[2]);
                                    spe.commit();

                                    spe.putString("currentdate", serverdate);
                                    spe.commit();

                                    SimpleDateFormat sdf = new SimpleDateFormat(
                                            "MM/dd/yyyy");

                                    Date curntdte = null;
                                    try {
                                        curntdte = sdf.parse(server_date);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    sdf.applyPattern("yyyy-MM-dd");
                                    todaydate1 = sdf.format(curntdte);

                                    spe.putString("todaydate", todaydate1);
                                    spe.commit();

                                    //SetClosingISOpeningOnlyOnce();
                                    // checkAndSaveMonthly();
                                    checkpresentornot(todaydate1);
                                    // checkAndSaveMonthly();

                                }

                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Connectivity Error, Please check Internet connection!!",
                                        Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            Toast.makeText(getApplicationContext(),
                                    "Connectivity Error!!",
                                    Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "User not activated ,Please contact admin!!",
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Connectivity Error, Please check Internet connection!!",
                            Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                // TODO: handle exception
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));

                e.printStackTrace();
                // we.writeToSD(errors.toString());
            }

        }


    }

    public class InsertHolidayList extends AsyncTask<Void, Void, SoapObject> {

        private SoapObject soap_result = null;
        SoapObject soap_result1 = null;
        String Flag;

        @SuppressLint("SimpleDateFormat")
        @Override
        protected SoapObject doInBackground(Void... params) {

            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                // Toast.makeText(getActivity(),"Check Your Internet Connection!!!",
                // Toast.LENGTH_LONG).show();

                Flag = "0";
                // stop executing code by return

            } else {
                Flag = "1";
                // TODO Auto-generated method stub

                soap_result = service.GetHoliday(month, year);
                SoapObject ss = null;

                if (soap_result != null) {

                    for (int i = 0; i < soap_result.getPropertyCount(); i++) {
                        ss = (SoapObject) soap_result.getProperty(i);

                        try {

                            // String holiday_date="11-09-2014 01:30:44";

                            String holiday_date = String.valueOf(ss
                                    .getProperty("date"));
                            String holiday_desc = String.valueOf(ss
                                    .getProperty("HolidayDesc"));
                            String holiday_date1 = "";

                            try {
                                if (holiday_date.equalsIgnoreCase("anyType{}")) {
                                    holiday_date = " ";
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        "MM/dd/yyyy hh:mm:ss a");
                                Date d = sdf.parse(holiday_date);
                                sdf.applyPattern("yyyy-MM-dd hh:mm:ss");
                                holiday_date1 = sdf.format(d);

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            // if(holiday_date.equalsIgnoreCase("anyType{}")){
                            // Log.e("","anytype for holiday_date");
                            // holiday_date = " ";
                            // }
                            if (holiday_desc.equalsIgnoreCase("anyType{}")) {
                                holiday_desc = " ";

                            }
                            final String[] columns = new String[]{"emp_id",
                                    "Adate", "attendance", "lat", "lon",
                                    "savedServer", "month", "holiday_desc",
                                    "year"};
                            db.open();

                            String[] values = new String[]{username,
                                    holiday_date1, "H", "0.0", "0.0", "1",
                                    month, holiday_desc, year};
                            db.insert(values, columns, "attendance");

                            db.close();

                        } catch (Exception e) {
                            // TODO: handle exception
                            StringWriter errors = new StringWriter();
                            e.printStackTrace(new PrintWriter(errors));

                            e.printStackTrace();
                            // we.writeToSD(errors.toString());
                        }

                    }

                } else {
                    // Toast.makeText(context,
                    // "Data Not Available or Check Connectivity",
                    // Toast.LENGTH_SHORT).show();
                }
            }
            return soap_result;
        }

        @Override
        protected void onPostExecute(SoapObject result) {

            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if ((pd != null) && pd.isShowing()) {
                pd.dismiss();
            }
            if (Flag.equalsIgnoreCase("0")) {

                Toast.makeText(LoginActivity.this,
                        "Check Your Internet Connection!!!", Toast.LENGTH_LONG)
                        .show();
            } else {

                if (result != null) {
                    Toast.makeText(LoginActivity.this,
                            "Holiday Sync Successfully!!", Toast.LENGTH_SHORT)
                            .show();
                    checkpresentornot(server_date);

                } else {

                    Toast.makeText(LoginActivity.this, "Not Sync!!",
                            Toast.LENGTH_SHORT).show();

                    checkpresentornot(server_date);
                }

            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd.setMessage("Receiving.....");
            pd.show();
            pd.setCancelable(false);
        }

    }

    @SuppressLint("SimpleDateFormat")
    private boolean checkpresentornot(String givendate) {

        boolean result = false;

        db.open();
        String a = db.getdatepresentorabsent(givendate, username);
        db.close();
        // String a="P";
        if (a.equalsIgnoreCase("")) {


           /* if (sp.getBoolean("Upload_data_flag", false) == false) {
                Log.e("Upload Data Receiver", String.valueOf(sp.getBoolean("Upload_data_flag", true)));
                DataUploadAlaramReceiver();
                spe.putBoolean("Upload_data_flag", true);
                spe.commit();

            } else {

            }*/
            Boc26AlaramReceiver();
            Intent i = new Intent(getApplicationContext(), AttendanceFragment.class);
            i.putExtra("FromLoginpage", "L");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
        if (a.equalsIgnoreCase("P")) {


            result = true;

            Boc26AlaramReceiver();
            Intent i = new Intent(getApplicationContext(),
                    DashboardNewActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("FROM", "LOGIN");
            startActivity(i);

        }
        if (a.equalsIgnoreCase("A")) {

            result = false;
            Toast.makeText(context, "U r absent today", Toast.LENGTH_SHORT)
                    .show();
        }

        return result;

    }

    @SuppressLint("SimpleDateFormat")
    public void checkAndSaveMonthly() {

        final Calendar c1 = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formate = new SimpleDateFormat("MMMM");
        String dateee = formate.format(c1.getTime());

        String date = formatter.format(c1.getTime());
        //String date = "26-04-2015";

        String split[] = date.split("-");

        String dd = split[0];

        String year = split[1];

        String year1 = String.valueOf((Integer.parseInt(split[1]) + 1));

        db.open();

//	 String startEnd[] = getStartEnd(BOC, year, year1);

        if (dd.equals("26") && db.fetchmonthyear(dateee, split[2])) {
            //if (dd.equals("26") && db.fetchmonthyear("April", split[2]) ) {

            if (sp.getBoolean("FirstTime26", false) == false) {
                UploadDownload26();
                Log.e("UploadDownload26()", "Excecute");
                spe.putBoolean("FirstTime26", true);
                spe.commit();
            } else {
                Intent i = new Intent(getApplicationContext(), DashboardNewActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("FROM", "LOGIN");
                startActivity(i);
            }

        } else {

            spe.putBoolean("FirstTime26", false);
            spe.commit();
            //	 Intent i = new Intent(getApplicationContext(),DashboardNewActivity.class);
            //	 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //	 startActivity(i);
        }

    }


    public void SetClosingISOpeningOnlyOnce() {

        Cursor c = null;
        db.open();
        c = db.SetClosingISOpeningOnlyOnce();

        if (c != null) {

            if (c.getCount() > 0) {
                c.moveToFirst();

                do {
                    // Log.e("","log111");
                    db.update(c.getString(2), new String[]{"0", "0", "0",
                            "0", "0", c.getString(13), c.getString(13), "0",
                            "0", "s"}, new String[]{"stock_received",
                            "sold_stock", "total_gross_amount",
                            "total_net_amount", "discount", "opening_stock",
                            "stock_in_hand", "return_saleable",
                            "return_non_saleable", "flag"}, "stock", "db_id");

                } while (c.moveToNext());

            } else {

            }
        } else {

        }

        db.close();

    }

    private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // extract the location info in the broadcast
            locationInfo = (LocationInfo) intent
                    .getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
            // refresh the display with it
            refreshDisplay(locationInfo);
        }
    };

    private void refreshDisplay() {
        refreshDisplay(new LocationInfo(context));
    }

    private void refreshDisplay(final LocationInfo locationInfo) {
        if (locationInfo.anyLocationDataReceived()) {
            lat = locationInfo.lastLat;
            lon = locationInfo.lastLong;
            // Toast.makeText(context, "Location Updated",
            // Toast.LENGTH_LONG).show();
            logCounter = 1;
        } else {

            Toast.makeText(context,
                    "Unable to get GPS location! Try again later!!",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    public void UploadDownload26() {

        String RESULT;
        int updateCount = 0;

        /*try {
            db.open();
            stock_array = db.getStockdetails();
            // db.close();
            // ------------------

            if (stock_array.getCount() > 0) {

                if (stock_array != null && stock_array.moveToFirst()) {
                    stock_array.moveToFirst();

                    sizeCounter = stock_array.getCount();
                    String shad;
                    do {

                        if (stock_array.getString(23) != null
                                || !stock_array.getString(23).equalsIgnoreCase(
                                "null")) {

                            // Log.v("","shadeno="+stock_array.getString(23));
                            shad = stock_array.getString(23).toString();

                        } else {
                            // Log.v("","shadeno="+stock_array.getString(23));
                            shad = "";
                        }

                        SoapPrimitive soap_result_stock = service.SaveStock(
                                stock_array.getString(0),
                                stock_array.getString(2),
                                stock_array.getString(1),
                                stock_array.getString(3), username,
                                stock_array.getString(4),
                                stock_array.getString(5),
                                stock_array.getString(6), shad,

                                stock_array.getString(10),
                                stock_array.getString(11),
                                stock_array.getString(12),

                                stock_array.getString(16),
                                stock_array.getString(14),
                                stock_array.getString(15),

                                stock_array.getString(13),
                                stock_array.getString(17),

                                stock_array.getString(19),
                                stock_array.getString(18),
                                stock_array.getString(7),
                                stock_array.getString(8),
                                stock_array.getString(21)

                        );

                        if (soap_result_stock != null) {
                            String result_stock = soap_result_stock.toString();
                            // Log.v("", "result_stock=" + result_stock);
                            if (result_stock.matches(".*\\d+.*")) {
                                // Log.e("","stock id for update=="+stock_array.getString(0));
                                db.open();
                                db.update_stock_data(result_stock);
                                Log.e("UpdateStock", "Success");
                                db.close();
                                updateCount++;

                            } else if (result_stock.equalsIgnoreCase("SE")) {

                                final Calendar calendar1 = Calendar
                                        .getInstance();
                                SimpleDateFormat formatter1 = new SimpleDateFormat(
                                        "MM/dd/yyyy HH:mm:ss");
                                String Createddate = formatter1
                                        .format(calendar1.getTime());
                                Log.e("UpdateStock", "ERROR");
                                int n = Thread.currentThread().getStackTrace()[2]
                                        .getLineNumber();
                                db.insertSyncLog("SaveStock_SE",
                                        String.valueOf(n), "SaveStock()",
                                        Createddate, Createddate,
                                        sp.getString("username", ""),
                                        "SaveStock()", "Fail");

                            }

                        } else {

                            // String errors =
                            // "Soap in giving null while 'Stock' and 'checkSyncFlag = 2' in  data Sync";
                            // we.writeToSD(errors.toString());
                            final Calendar calendar1 = Calendar.getInstance();
                            SimpleDateFormat formatter1 = new SimpleDateFormat(
                                    "MM/dd/yyyy HH:mm:ss");
                            String Createddate = formatter1.format(calendar1
                                    .getTime());
                            Log.e("UpdateStock", "ERROR");
                            int n = Thread.currentThread().getStackTrace()[2]
                                    .getLineNumber();
                            db.insertSyncLog(
                                    "Internet Connection Lost, Soap in giving null while 'SaveStock'",
                                    String.valueOf(n), "SaveStock()",
                                    Createddate, Createddate,
                                    sp.getString("username", ""),
                                    "SaveStock()", "Fail");

                        }

                    } while (stock_array.moveToNext());

                    if (sizeCounter == insertCounter) {

                        Log.e("DATA", "Present");
                        db.open();
                        db.delete_stock_data();
//                        getBOCOpening();
                    }

                } else {

                    Log.e("DATA", "NotPresent");
                    RESULT = "NODATA";
                    db.open();
                    db.delete_stock_data();
//                    getBOCOpening();
                    // Log.e("","no data available");

                }

            } else if (stock_array == null) {


                Log.e("DATA", "NotPresent");
                RESULT = "NODATA";
                db.open();
                db.delete_stock_data();
//                getBOCOpening();

            } else {


                Log.e("DATA", "NotPresent");
                RESULT = "NODATA";
                db.open();
                db.delete_stock_data();
//                getBOCOpening();
                // Log.e("No any  Stock data for upload ",
                // String.valueOf(stock_array.getCount()));
            }

        } catch (Exception e) {

            e.printStackTrace();

            String Error = e.toString();

            final Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter1 = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            String Createddate = formatter1.format(cal.getTime());

            int n = Thread.currentThread().getStackTrace()[2].getLineNumber();
            db.insertSyncLog(Error, String.valueOf(n), "SaveStock()",
                    Createddate, Createddate, sp.getString("username", ""),
                    "SaveStock()", "Fail");

        }*/
    }

    public void getBOCOpening() {
        SoapObject soap_result = service.GetBOCOpening(sp.getString("username", ""));
        String db_stock_id_array = null;
        String EmpId = null;
        SoapPrimitive soap_update_stock_row;

        if (soap_result != null) {

            for (int i = 0; i < soap_result.getPropertyCount(); i++) {

                SoapObject soap_result1 = (SoapObject) soap_result
                        .getProperty(i);

                // Log.e("pm", "status="+soap_result1.getProperty("status")
                // .toString());

                if (soap_result1.getProperty("status").toString()
                        .equalsIgnoreCase("C")) {

                    String db_stock_id = soap_result1.getProperty("Id")
                            .toString();

                    String db_Id = soap_result1.getProperty("ProductId")
                            .toString();

                    // Log.v("","db_Id="+db_Id);

                    String CatCodeId = soap_result1.getProperty("CatCodeId")
                            .toString();

                    if (CatCodeId == null) {
                        CatCodeId = "";

                    }
                    // Log.v("","CatCodeId="+CatCodeId);

                    String ProductId = soap_result1.getProperty("ProductId")
                            .toString();
                    // Log.v("","ProductId="+ProductId);

                    if (ProductId == null) {
                        ProductId = "";

                    }

                    String EANCode = soap_result1.getProperty("EANCode")
                            .toString();

                    if (EANCode == null) {
                        EANCode = "";

                    }
                    EmpId = soap_result1.getProperty("EmpId").toString();

                    if (EmpId == null) {
                        EmpId = "";

                    }
                    String ProductCategory = soap_result1.getProperty(
                            "ProductCategory").toString();

                    if (ProductCategory == null) {
                        ProductCategory = "";

                    }
                    String ProductType = soap_result1
                            .getProperty("ProductType").toString();

                    if (ProductType == null) {
                        ProductType = "";

                    }
                    String ProductName = soap_result1
                            .getProperty("ProductName").toString();
                    if (ProductName == null) {
                        ProductName = "";

                    }

                    String Opening_Stock = soap_result1.getProperty(
                            "Opening_Stock").toString();
                    if (Opening_Stock == null) {
                        Opening_Stock = "";

                    }

                    String FreshStock = soap_result1.getProperty("FreshStock")
                            .toString();
                    if (FreshStock == null) {
                        FreshStock = "";

                    }

                    String Stock_inhand = soap_result1.getProperty(
                            "Stock_inhand").toString();

                    if (Stock_inhand == null) {
                        Stock_inhand = "";

                    }
                    String SoldStock = soap_result1.getProperty("SoldStock")
                            .toString();

                    if (SoldStock == null) {
                        SoldStock = "";

                    }
                    String S_Return_Saleable = soap_result1.getProperty(
                            "S_Return_Saleable").toString();

                    if (S_Return_Saleable == null) {
                        S_Return_Saleable = "";

                    }
                    String S_Return_NonSaleable = soap_result1.getProperty(
                            "S_Return_NonSaleable").toString();

                    if (S_Return_NonSaleable == null) {
                        S_Return_NonSaleable = "";

                    }
                    String ClosingBal = soap_result1.getProperty("ClosingBal")
                            .toString();

                    if (ClosingBal == null) {
                        ClosingBal = "";

                    }
                    String GrossAmount = soap_result1
                            .getProperty("GrossAmount").toString();

                    if (GrossAmount == null) {
                        GrossAmount = "";

                    }
                    String Discount = soap_result1.getProperty("Discount")
                            .toString();

                    if (Discount == null) {
                        Discount = "";

                    }
                    String NetAmount = soap_result1.getProperty("NetAmount")
                            .toString();

                    if (NetAmount == null) {
                        NetAmount = "";

                    }
                    String Size = soap_result1.getProperty("Size").toString();

                    if (Size == null) {
                        Size = "";

                    }
                    String Price = soap_result1.getProperty("Price").toString();

                    if (Price == null) {
                        Price = "";

                    }
                    String LMD = soap_result1.getProperty("LMD").toString();

                    if (LMD == null) {
                        LMD = "";

                    } else {

                        try {
                            String inputPattern = "MM/dd/yyyy hh:mm:ss a";
                            String outputPattern = "yyyy-MM-dd HH:mm:ss";

                            DateFormat inputFormat = new SimpleDateFormat(
                                    inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(
                                    outputPattern);

                            Date date = inputFormat.parse(LMD);
                            LMD = outputFormat.format(date);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    String AndroidCreatedDate = soap_result1.getProperty(
                            "AndroidCreatedDate").toString();

                    String MONTH = "", YEAR = "";
                    if (AndroidCreatedDate == null) {
                        AndroidCreatedDate = "";

                    } else {

                        try {
                            String inputPattern = "MM/dd/yyyy hh:mm:ss a";
                            String outputPattern = "yyyy-MM-dd HH:mm:ss";

                            SimpleDateFormat inputFormat = new SimpleDateFormat(
                                    inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(
                                    outputPattern);

                            Date date = inputFormat.parse(AndroidCreatedDate);
                            AndroidCreatedDate = outputFormat.format(date);

                            String[] addd = AndroidCreatedDate.split(" ");
                            String addd1 = addd[0];
                            String[] addd2 = addd1.split("-");

                            String month = addd2[1];
                            YEAR = addd2[0];
                            //
                            SimpleDateFormat monthParse = new SimpleDateFormat(
                                    "MM");
                            SimpleDateFormat monthDisplay = new SimpleDateFormat(
                                    "MMMM");
                            MONTH = monthDisplay
                                    .format(monthParse.parse(month));
                            //

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (db_Id.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for db_Id");
                        db_Id = " ";
                    }
                    if (CatCodeId.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for CatCodeId");
                        CatCodeId = " ";
                    }

                    if (ProductId.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for ProductId");
                        ProductId = " ";
                    }
                    if (EANCode.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for EANCode");
                        EANCode = " ";
                    }

                    if (EmpId.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for EmpId");
                        EmpId = " ";
                    }
                    if (ProductCategory.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for ProductCategory");
                        ProductCategory = " ";
                    }
                    if (ProductType.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for ProductType");
                        ProductType = " ";
                    }

                    if (ProductName.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for ProductName");
                        ProductName = " ";
                    }
                    if (Opening_Stock.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for Opening_Stock");
                        Opening_Stock = " ";
                    }

                    if (FreshStock.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for FreshStock");
                        FreshStock = " ";
                    }
                    if (Stock_inhand.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for Stock_inhand");
                        Stock_inhand = " ";
                    }
                    if (SoldStock.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for SoldStock");
                        SoldStock = " ";
                    }
                    if (S_Return_Saleable.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for S_Return_Saleable");
                        S_Return_Saleable = " ";
                    }
                    if (S_Return_NonSaleable.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for S_Return_NonSaleable");
                        S_Return_NonSaleable = " ";
                    }
                    if (ClosingBal.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for ClosingBal");
                        ClosingBal = " ";
                    }
                    if (GrossAmount.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for GrossAmount");
                        GrossAmount = " ";
                    }
                    if (Discount.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for Discount");
                        Discount = " ";
                    }
                    if (NetAmount.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for NetAmount");
                        NetAmount = " ";
                    }
                    if (Size.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for Size");
                        Size = " ";
                    }
                    if (Price.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for sku_l");
                        Price = " ";
                    }
                    if (LMD.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for LMD");
                        LMD = " ";
                    }
                    if (AndroidCreatedDate.equalsIgnoreCase("anyType{}")) {
                        // Log.e("", "anytype for AndroidCreatedDate");
                        AndroidCreatedDate = " ";
                    }

                    // if (flag.equalsIgnoreCase("e")) {

                    // Log.e("pm", "pm5--");
                    db.open();
                    // Cursor c = db.getuniquedata_stock(CatCodeId,
                    // EANCode, db_Id);

                    Cursor c1 = db.CheckDataExist("stock", db_Id,
                            ProductCategory, ProductType, ProductName);

                    // db_stock_id_array = db_stock_id;

                    int count = c1.getCount();
                    // Log.v("", "" + count);
                    db.close();
                    if (count > 0) {

                        db.open();
                        db.UpdateStockSync(ProductCategory, ProductType,
                                ProductName, EmpId, Stock_inhand, ClosingBal,
                                FreshStock, GrossAmount, SoldStock, Price,
                                Size, db_Id, LMD, Discount, NetAmount,
                                Opening_Stock, S_Return_Saleable,
                                S_Return_NonSaleable);
                        db.close();

                        db_stock_id_array = db_stock_id_array + "," + db_Id;

                    } else {

                        // Log.e("pm", "pm5");
                        db.open();
                        db.insertProductMasterFirsttime(db_stock_id, db_Id,
                                ProductId, CatCodeId, EANCode, EmpId,
                                ProductCategory, ProductType, ProductName,
                                Opening_Stock, FreshStock, Stock_inhand,
                                SoldStock, S_Return_NonSaleable,
                                S_Return_Saleable, ClosingBal, GrossAmount,
                                Discount, NetAmount, Size, Price, LMD,
                                AndroidCreatedDate, MONTH, YEAR);
                        db.close();

                        // Log.e("",
                        // "db_stock_id="+db_stock_id+" empid="+EmpId);
                        db_stock_id_array = db_stock_id_array + "," + db_Id;

                    }
                } else if (soap_result1.getProperty("status").toString()
                        .equalsIgnoreCase("E")) {
                    // Log.e("pm", "pm7");
                    // Flag = "1";

                    soap_update_stock_row = service.UpdateTableData(
                            db_stock_id_array, "S", EmpId);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");

                    Calendar cal = Calendar.getInstance();
                    // dateFormat.format(cal.getTime())
                    db.open();
                    db.updateDateSync(dateFormat.format(cal.getTime()), "stock");
                    db.close();

                } else if (soap_result1.getProperty("status").toString()
                        .equalsIgnoreCase("N")) {

                    // Flag = "3";
                    // Log.e("", "string ids== "+db_stock_id_array);
                    soap_update_stock_row = service.UpdateTableData(
                            db_stock_id_array, "S", EmpId);
                    // Log.e("",
                    // "soap_update_stock_row= "+soap_update_stock_row.toString());

                } else if (soap_result1.getProperty("status").toString()
                        .equalsIgnoreCase("SE")) {

                    soap_update_stock_row = service.UpdateTableData(
                            db_stock_id_array, "S", EmpId);
                    // Log.e("",
                    // "soap_update_stock_row= "+soap_update_stock_row.toString());

                    // Flag="2";
                    // Log.e("", "string ids== "+db_stock_id_array);

                    final Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String Createddate = formatter.format(calendar.getTime());
                    // Log.v("","se error");
                    int n = Thread.currentThread().getStackTrace()[2]
                            .getLineNumber();
                    db.open();
                    db.insertSyncLog("FirstTimeSync_SE", String.valueOf(n),
                            "SyncStockData()", Createddate, Createddate,
                            sp.getString("username", ""), "SyncStockData()",
                            "Fail");
                    db.close();
                }
            }

        } else {
            // Log.v("", "Soap result is null");
            // Toast.makeText(context,
            // "Data Not Available or Check Connectivity",
            // Toast.LENGTH_SHORT).show();

            final Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            String Createddate = formatter.format(calendar.getTime());
            int n = Thread.currentThread().getStackTrace()[2].getLineNumber();
            db.insertSyncLog("Soup is null - SyncStockData()",
                    String.valueOf(n), "SyncStockData()", Createddate,
                    Createddate, sp.getString("username", ""), "Data Download",
                    "Fail");

        }
    }

//	public void setOpeningClosingOn26()
//	{
//
//	}

//	public String[] getStartEnd(String BOC,String year,String year1)
//	{
//		String startend[] = new String[2];
//
//		if(BOC.equalsIgnoreCase("BOC1"))
//		{
//			startend[0] = year +"-03-26" ;
//			startend[1] = year +"-04-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC2"))
//		{
//			startend[0] = year +"-04-26" ;
//			startend[1] = year +"-05-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC3"))
//		{
//			startend[0] = year +"-05-26" ;
//			startend[1] = year +"-06-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC4"))
//		{
//			startend[0] = year +"-06-26" ;
//			startend[1] = year +"-07-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC5"))
//		{
//			startend[0] = year +"-07-26" ;
//			startend[1] = year +"-08-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC6"))
//		{
//			startend[0] = year +"-08-26" ;
//			startend[1] = year +"-09-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC7"))
//		{
//			startend[0] = year +"-09-26" ;
//			startend[1] = year +"-10-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC8"))
//		{
//			startend[0] = year +"-10-26" ;
//			startend[1] = year +"-11-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC9"))
//		{
//			startend[0] = year +"-11-26" ;
//			startend[1] = year +"-12-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC10"))
//		{
//			startend[0] = year +"-12-26" ;
//			startend[1] = year1 +"-01-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC11"))
//		{
//			startend[0] = year1 +"-01-26" ;
//			startend[1] = year1 +"-02-25" ;
//		}
//		else if(BOC.equalsIgnoreCase("BOC12"))
//		{
//			startend[0] = year1 +"-02-26" ;
//			startend[1] = year1 +"-03-25" ;
//		}
//
//		return startend;
//	}

    public String getBOC(String year, String yearPlus) {
        String str_BOC = null;

        String BOCDates[] = new String[2];

        final Calendar c1 = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(c1.getTime());


        return str_BOC;
    }

    public class SyncApkCheck extends AsyncTask<Void, Void, Boolean> {

        boolean result = false;

        ProgressDialog mprogress;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mprogress = new ProgressDialog(LoginActivity.this);
            mprogress.setTitle("Checking / Downloading APK");
            mprogress.setMessage("Please Wait..!");
            mprogress.setCancelable(false);
            mprogress.show();


        }


        @Override
        protected Boolean doInBackground(Void... params) {

            CheckServerApkVersionDownloadFile(downloadConfigFile);
            String version = ReadVersionFromSDCardFile();
            if (!version.equalsIgnoreCase("")) {
                if (!VERSION_NAME.trim().equalsIgnoreCase(version.trim())) {
                    result = Update(downloadURL);
                } else {
                    result = false;
                }
            } else {
                result = false;
            }

            return result;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if ((mprogress != null) && mprogress.isShowing()) {
                mprogress.dismiss();
            }

            if (result != null) {
                if (result) {
                    Toast.makeText(LoginActivity.this, "New Apk Version has been Installed..!", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(MainActivity.this, "No New Version !", Toast.LENGTH_SHORT).show();
                    //LoginUser();
                     new GetServerDate().execute();
                }
            } else {
//                Toast.makeText(MainActivity.this, "No New Version !", Toast.LENGTH_SHORT).show();
                //LoginUser();
                new GetServerDate().execute();

            }

        }

    }

    public void LoginUser() {

        //spe.clear();
        /*if (sp.getBoolean("Upload_data_flag", false) == false) {

            DataUploadAlaramReceiver();

        } else {
            Log.e("Upload Data Receivert", String.valueOf(sp.getBoolean("Upload_data_flag_true", true)));

        }*/

        if (edt_username.getText().toString().equalsIgnoreCase("")) {
        } else if (edt_password.getText().toString()
                .equalsIgnoreCase("")) {
            edt_username.setError(null);
            edt_password.setError("Enter Password");

        } else {

            username = edt_username.getText().toString()
                    .toUpperCase();
            pass = edt_password.getText().toString();

            db.open();

            int count = db.checkcount(username, pass);

            db.close();

            if (count > 0) {

                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date())
                        .toString();

                db.open();
                String a = db.getdatepresentorabsent(
                        currentDateandTime, username);
                db.close();
                // String a="P";

                if (a.equalsIgnoreCase("")) {

                    new Check_Login().execute();
                }
                if (a.equalsIgnoreCase("P")) {

                    // SetClosingISOpeningOnlyOnce();
                    Boc26AlaramReceiver();
                    Intent i = new Intent(getApplicationContext(),
                            DashboardNewActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("FROM", "LOGIN");
                    //i.putExtra("LoginFlag", LogoutStatus);
                    startActivity(i);

                }
                if (a.equalsIgnoreCase("A")) {

                    Toast.makeText(context, "U r absent today",
                            Toast.LENGTH_SHORT).show();
                }

            } else {

                db.open();

                int count1 = db.checkcount123();

                db.close();

                if (count1 == 1) {

                    Toast.makeText(
                            getApplicationContext(),
                            "Please Enter Correct user name and password",
                            Toast.LENGTH_SHORT).show();
                } else {

                    new Check_Login().execute();
                }

            }


        }
    }

    public void CheckServerApkVersionDownloadFile(String configFileUrl) {
        try {
            URL url = new URL(configFileUrl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(false);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "config.txt");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();//

        } catch (IOException e) {
            // Toast.makeText(LoginActivity.this,"Server APK Version not getting",
            // Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

    public String ReadVersionFromSDCardFile() {

        String serverApkVersion = "";


        try {

            String PATH = Environment.getExternalStorageDirectory()
                    + "/download/config.txt";
            File myFile = new File(PATH);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(
                    fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer = aDataRow;
            }

            return serverApkVersion = aBuffer;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return serverApkVersion   /*.split("\\$")[0]*/;

    }

    public Boolean Update(String apkurl) {
        try {
            URL url = new URL(apkurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(false);
            c.setReadTimeout(6000);
            c.setConnectTimeout(6000);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory()
                    + "/download/";

            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "Lotus_Pro.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory()
                            + "/download/"
                            + "Lotus_Pro.apk")),
                    "application/vnd.android.package-archive");
            startActivity(intent);

            // mProgress.dismiss();
            if (outputFile.exists()) {
                return true;
            }

        } catch (IOException e) {
//            Toast.makeText(getApplicationContext(), "Update error!",
//                    Toast.LENGTH_LONG).show();

            e.printStackTrace();
            return false;
        }
        return null;
    }

    /**
     * This method disables the Broadcast receiver registered in the AndroidManifest file.
     */
    public void disableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, MyScheduledReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();
    }


    public class GetServerDate extends AsyncTask<Void, Void, SoapPrimitive> {

        private SoapPrimitive server_date_result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgress.setMessage("Please Wait");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected SoapPrimitive doInBackground(Void... params) {
            if (cd.isConnectingToInternet()) {
                try {

                    server_date_result = service.GetServerDate();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return server_date_result;
        }

        @Override
        protected void onPostExecute(SoapPrimitive result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if ((mProgress != null) && mProgress.isShowing()) {
                mProgress.dismiss();
            }

            try {
                if (result != null) {

                    serverdate = server_date_result.toString();

                    spe.putString("SERVER_DATE", serverdate);
                    spe.commit();

                    final Calendar calendar1 = Calendar
                            .getInstance();
                    SimpleDateFormat formatter1 = new SimpleDateFormat(
                            "M/d/yyyy");
                    String systemdate = formatter1.format(calendar1
                            .getTime());

//                            String date = "8/29/2011 11:16:12 AM";
                    String[] parts = serverdate.split(" ");
                    String serverdd = parts[0];


                    if (systemdate != null && serverdd != null
                            && systemdate.equalsIgnoreCase(serverdd)) {
                        LoginUser();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please Check your Handset Date", Toast.LENGTH_LONG).show();
                    }

                } else {

                    final Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String Createddate = formatter.format(calendar
                            .getTime());

                    int n = Thread.currentThread().getStackTrace()[2]
                            .getLineNumber();
                    db.open();
                    db.insertSyncLog(
                            "Soup is Null While GetServerDate()",
                            String.valueOf(n), "GetServerDate()",
                            Createddate, Createddate,
                            sp.getString("username", ""),
                            "Login Check", "Fail");
                    db.close();

                    Toast.makeText(
                            getApplicationContext(),
                            "Connectivity Error, Please check Internet connection!!",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void AutologoutBroadcast() {
        try {

            AlarmManager mAlarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //Create pending intent & register it to your alarm notifier class
            Intent broadCastIntent = new Intent(this, MyScheduledReceiver.class);
            broadCastIntent.putExtra("uur", "1e"); // if you want
            boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, broadCastIntent, PendingIntent.FLAG_NO_CREATE) != null);
            if (!alarmRunning) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, broadCastIntent, 0);
                //set timer you want alarm to work (here I have set it to 7.00)
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 5);  //24
                calendar.set(Calendar.MINUTE, 0);   //0
                calendar.set(Calendar.SECOND, 0);

                //set that timer as a RTC Wakeup to alarm manager object
                mAlarmManger.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }*/



}

package bpsound.spryfit.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bpsound.spryfit.R;
import bpsound.spryfit.dialog.CustomDialog;


public class KLib {
	private static ProgressDialog mProgDlog;
	private static CustomDialog mDlog;
	
	public static void showLoadingProgDialog(Context context){
		mProgDlog = ProgressDialog.show(context, "", context.getResources().getString(R.string.loading), true);
	}
	
	public static void hideLoadingProgDialog(){
		if(mProgDlog!=null && mProgDlog.isShowing()){
			mProgDlog.hide();
		}
	}
	
	/**
	 * forced perfect termination to application
	 * 
	 * @param context
	 */
	static public void requestKillProcess(final Context context) {

		// #1. first check api level.
		int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		if (sdkVersion < 8) {
			// #2. if we can use restartPackage method, just use it.
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(context.getPackageName());
		} else {
			// #3. else, we should use killBackgroundProcesses method.
			new Thread(new Runnable() {
				public void run() {
					ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
					String name = context.getApplicationInfo().processName;
					// RunningServiceInfo si;

					// pooling the current application process importance
					// information.
					while (true) {
						List<RunningAppProcessInfo> list = am
								.getRunningAppProcesses();
						for (RunningAppProcessInfo i : list) {
							if (i.processName.equals(name) == true) {
								// #4. kill the process,
								// only if current application importance is
								// less than IMPORTANCE_BACKGROUND
								if (i.importance >= RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
									am.restartPackage(context.getPackageName()); // simple
																					// wrapper
																					// of
																					// killBackgrounProcess
								else
									Thread.yield();
								break;
							}
						}
					}
				}
			}, "Process Killer").start();
		}
		System.exit(0);
	}	
	
	/**
	 * checking network connectivity
	 * 
	 * @return
	 */
	public static boolean isOnline(Context _context) { // network 연결 상태 확인

		boolean connected = false;
		ConnectivityManager conMan = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if (mobile.isConnected()){
			connected = true;
		}
		if (wifi.isConnected()){
			connected = true;
		}

		return connected;
	}
	
	/**
	 * moving to activity
	 *
	 */
	public static void MoveActivity(Context packageContext, Class<?> newActivity, HashMap<String, String> mapParam)  {
   		try
   		{
   			Intent intent = new Intent(packageContext, newActivity);
   			if (mapParam != null)
   			{
   				Iterator<String> k = mapParam.keySet().iterator();
   			    while (k.hasNext()) {
   			      String key = (String) k.next();
   			      intent.putExtra(key, (String) mapParam.get(key));
   			    }
   			}
   			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
   			packageContext.startActivity(intent);
   		}
   		catch(Exception ex)
   		{
			
   		}
	} 
	
	/**
	 * making inert screen cloudy
	 *
	 */
	public static void setDimBehind(Activity _activity) {
		_activity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	
	/**
	 * alert to message
	 * 
	 * @param _context
	 * @param _title
	 * @param _msg
	 */
	static public void setAlertDialog(Context _context, String _title,
			String _msg) {
		new AlertDialog.Builder(_context).setTitle(_title).setMessage(_msg)
				.setPositiveButton(_context.getResources().getString(R.string.confirmation), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}
	
	static public void setAlertDialog(Context _context, String _title,
			String _msg, DialogInterface.OnClickListener _onClickListener) {
		new AlertDialog.Builder(_context).setTitle(_title).setMessage(_msg)
				.setPositiveButton("확인",_onClickListener).show();
	}

	static public void showErrorDialog(Context context, String message) {
		new AlertDialog.Builder(context)
				.setTitle("Error")
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
	}
	
	/**
	 * alert to message then move to activity
	 * 
	 * @param _context
	 * @param _title
	 * @param _msg
	 */
	static public void setAlertDialog2(final Context _context, String _title,
			String _msg,DialogInterface.OnClickListener _onClickListener) {
		new AlertDialog.Builder(_context).setTitle(_title).setMessage(_msg)
				.setPositiveButton(_context.getResources().getString(R.string.confirmation),_onClickListener)
				.setNegativeButton(_context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
	}
	
	static public void setAlertDialog2(final Context _context, String _title,
			String _msg,DialogInterface.OnClickListener _onClickListener, DialogInterface.OnClickListener _onClickListener2) {
		new AlertDialog.Builder(_context).setTitle(_title).setMessage(_msg)
				.setPositiveButton(_context.getResources().getString(R.string.confirmation),_onClickListener)
				.setNegativeButton(_context.getResources().getString(R.string.cancel), _onClickListener2)
				.show();
	}
	
	static public void setCustomAlertDialog(final Activity activity, String _msg, View.OnClickListener _onClickListener){
		mDlog = new CustomDialog(activity);
		mDlog.setContentView(R.layout.layout_dialog_choice);
		
		TextView tvContent = (TextView)mDlog.findViewById(R.id.tvContent);
		tvContent.setText(_msg);
		
		Button btnConfirm = (Button)mDlog.findViewById(R.id.confirmButton);
		btnConfirm.setOnClickListener(_onClickListener);
		
		Button btnCancel = (Button)mDlog.findViewById(R.id.cancelButton);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDlog.dismiss();
			}
		});
		mDlog.show();			
	}	
	
	static public void hideCustomAlertDialog(){
		if(mDlog!=null){
			mDlog.hide();
		}
	}
	
	/**
	 * alert in list dialog
	 * @param context
	 * @param title
	 * @param arr
	 * @param arrListner
	 */
	public static void showListDialog(Context context, String title, String[] arr, DialogInterface.OnClickListener arrListner){
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_menu_more)
		.setTitle(title)
		.setItems(arr, arrListner)
		.setNegativeButton(context.getResources().getString(R.string.cancel), null)
		.show();
	}
	
	/*
	 * show progress dialog
	 */
	private static CustomDialog mLoadingDlog;
	
	public static void showLoadingDialog(Activity activity)
	{
		if(mLoadingDlog == null)
		{
			if (activity != null)
			{
				mLoadingDlog = new CustomDialog(activity);
				mLoadingDlog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mLoadingDlog.setCanceledOnTouchOutside(false);				
				mLoadingDlog.setContentView(R.layout.layout_dialog_loading);
				mLoadingDlog.setLogoImage();
				mLoadingDlog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_background);
			}
		}
		
		if(mLoadingDlog != null)
		{
			mLoadingDlog.show();
		}
	}
	
	public static void hideLoadingDialog()
	{
		if (mLoadingDlog != null)
		{
			mLoadingDlog.dismiss();
		
			mLoadingDlog = null;
		}
	}
	
	public static String getPhoneNum(Context context){
		TelephonyManager systemService = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = systemService.getLine1Number();
		if(phoneNum==null){
			return null;
		}
		phoneNum = "0"+phoneNum.substring(phoneNum.length()-10, phoneNum.length());
		phoneNum = PhoneNumberUtils.formatNumber(phoneNum);
		return phoneNum;
	}
	
	public static String getFormatedDate(Context context, String format, Date date)
	{
		String year = DateFormat.format("yyyy", date).toString();
		String month = DateFormat.format("MM", date).toString();
		String day = DateFormat.format("dd", date).toString();
        Resources res = context.getResources();
		int[] strWeek = {R.string.week_sun,R.string.week_mon,R.string.week_tue,R.string.week_wed,R.string.week_thu,R.string.week_fri,R.string.week_sat};
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		
		int weekIdx = cal.get(Calendar.DAY_OF_WEEK) - 1;

		return String.format(format, year, month, day, res.getString(strWeek[weekIdx]));
	}
	
	public static String getAddressForLocation(Context context, double latitude, double longitude){
		if(latitude==0.0 && longitude==0.0){
			return "";
		}
		
		Geocoder geocoder = new Geocoder(context, Locale.KOREA);
		List<Address> addressList = null;
		try{
			addressList = geocoder.getFromLocation(latitude, longitude, 1);
		}catch(IOException e){
			e.printStackTrace();
			ToastUtils.showToast(context.getResources().getString(R.string.cant_find_loc));
			return "";
		}
		
		StringBuilder sbAddress = new StringBuilder();
		if(addressList.size()>0){
			Address address = addressList.get(0);
			sbAddress.append(address.getAdminArea()+" "+address.getLocality());			
		}
		String strAddress = sbAddress.toString();
		return strAddress;
	}
	
	/**
	 * checking to email format
	 * @param email
	 * @return true:false
	 */
	public static boolean isValidEmail(String email) {
		boolean err = false;
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		if (!m.matches()) err = true;
		return err;
	}
}

package bpsound.spryfit.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private static Context context;
	private static Toast toast;
	
	
	public static void setContext(Context _context) {
		if(context == null) {
			context = _context;
		}
	}
	
	public static void showToast(String _str) {

		try {
			if(toast == null) {
				toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
			}
			
			toast.setText(_str);
			toast.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showToast(int resId) {
		
		try {
			if(toast == null) {
				toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
			}
			
			toast.setText(resId);
			toast.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

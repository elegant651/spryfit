package bpsound.spryfit.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Pattern;

public class SoftKeyboardUtils {
	public static void showKeyboard(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}
	
	public static void hideKeyboard(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);   
	}
	
	//only use english+number
	public static InputFilter filterAlphaNum = new InputFilter(){
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend){
			Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
			if(!ps.matcher(source).matches()){
				return "";
			}
			return null;
		}
	};
}

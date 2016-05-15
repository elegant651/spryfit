package bpsound.spryfit.data;


import bpsound.spryfit.utils.SharedPrefsUtils;

public class UserInfo {

	public static void setUid(String uid) {
		SharedPrefsUtils.addDataToPrefs(Constants.PREFS_LOGIN, "uid", uid);
	}

	public static String getUid() {
		return SharedPrefsUtils.getStringFromPrefs(Constants.PREFS_LOGIN, "uid");
	}

	public static void setProvider(String provider) {
		SharedPrefsUtils.addDataToPrefs(Constants.PREFS_LOGIN, "provider", provider);
	}

	public static String getProvider() {
		return SharedPrefsUtils.getStringFromPrefs(Constants.PREFS_LOGIN, "provider");

	}

	public static void setUserName(String username) {
		SharedPrefsUtils.addDataToPrefs(Constants.PREFS_LOGIN, "username", username);
	}

	public static String getUserName() {
		return SharedPrefsUtils.getStringFromPrefs(Constants.PREFS_LOGIN, "username");
	}

	public static void setGender(String gender) {
		SharedPrefsUtils.addDataToPrefs(Constants.PREFS_LOGIN, "gender", gender);
	}

	public static String getGender() {
		return SharedPrefsUtils.getStringFromPrefs(Constants.PREFS_LOGIN, "gender");
	}

	public static void setLink(String link) {
		SharedPrefsUtils.addDataToPrefs(Constants.PREFS_LOGIN, "link", link);
	}

	public static String getLink() {
		return SharedPrefsUtils.getStringFromPrefs(Constants.PREFS_LOGIN, "link");
	}

	public static void setLocale(String locale) {
		SharedPrefsUtils.addDataToPrefs(Constants.PREFS_LOGIN, "locale", locale);
	}

	public static String getLocale() {
		return SharedPrefsUtils.getStringFromPrefs(Constants.PREFS_LOGIN, "locale");
	}
	
	public static void setMyPicPath(String path){
		SharedPrefsUtils.addDataToPrefs(Constants.PREFS_LOGIN, "picpath", path);
	}
	
	public static String getMyPicPath(){
		return SharedPrefsUtils.getStringFromPrefs(Constants.PREFS_LOGIN, "picpath");
	}

}

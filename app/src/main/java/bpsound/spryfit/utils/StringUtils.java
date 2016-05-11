package bpsound.spryfit.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bpsound.spryfit.R;


public class StringUtils {

	private static final char KOREAN_BEGIN_UNICODE = 44032; // 가 
	private static final char KOREAN_LAST_UNICODE = 55203; // 힣
	private static final char KOREAN_BASE_UNIT = 588;//각자음 마다 가지는 글자수
	
	//자음
	private static final char[] INITIAL_LETTER = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
	
	public static String getConvertedDateString(Context context, long date) {
		
		//Set regdate.
		String timeDistance = "";
		
		try {
			if(date > 0) {
				long deltaTime = System.currentTimeMillis() - date;
				int minute = 1000 * 60;
				int hour = minute * 60;
				int day = hour * 24;
				int week = day * 7;
				int month = day * 30;
				
				//defining string arguments.
				if(deltaTime < minute *10) {
					//방금.
					timeDistance = context.getResources().getString(R.string.aMomentAgo);
				} else if(deltaTime < hour){
					//몇분 전.
					timeDistance = (deltaTime / minute) + context.getResources().getString(R.string.minutesAgo);
				} else if(deltaTime < day){
					//몇시간 전
					timeDistance = (deltaTime / hour) + context.getResources().getString(R.string.hoursAgo);
				} else if(deltaTime < week){
					//몇일 전.
					timeDistance = (deltaTime / day) + context.getResources().getString(R.string.daysAgo);
				} else if(deltaTime < month){
					//몇주 전
					timeDistance = (deltaTime / week) + context.getResources().getString(R.string.weeksAgo);
				} else if(deltaTime < month *2){
					//한달 전.
					timeDistance = context.getResources().getString(R.string.oneMonthAgo);
				} else if(deltaTime < month *3){
					//두달 전.
					timeDistance = context.getResources().getString(R.string.twoMonthAgo);
				} else if(deltaTime < month *4){
					//세달 전.
					timeDistance = context.getResources().getString(R.string.threeMonthAgo);
				} else {
					//오래전.
					timeDistance = context.getResources().getString(R.string.longTimeAgo);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return timeDistance;
	}
	
	public static boolean checkNullOrDefault(String _str, String _defaultString) {
		
		String defaultString = "default";
		
		if(_defaultString != null) {
			defaultString = _defaultString;
		}
		
		if(_str == null)
			return true;
		
		if(_str.equals(""))
			return true;
		
		if(_str.equals(defaultString))
			return true;
		
		return false;
	}
	
	public static String getFormattedNumber(int _num) {
		
		ArrayList<Character> arList = new ArrayList<Character>();
		
		String formattedNumber = "";
		
		String str = Integer.toString(_num);
		
		//lengthOfStr
		int los = str.length();
		int mod = los % 3;
		
		for(int i=0; i<los; i++) {
			arList.add(str.charAt(i));
		}
		
		for(int i=0; i<los; i++) {
			
			if(i%3 == mod && i != 0) {
				formattedNumber += ",";
			}
			
			formattedNumber += arList.get(i);
		}

		return formattedNumber;
	}
	
	public static String getFormattedNumber(long _num) {
		
		ArrayList<Character> arList = new ArrayList<Character>();
		
		String formattedNumber = "";
		
		String str = Long.toString(_num);
		
		//lengthOfStr
		int los = str.length();
		int mod = los % 3;
		
		for(int i=0; i<los; i++) {
			arList.add(str.charAt(i));
		}
		
		for(int i=0; i<los; i++) {
			
			if(i%3 == mod && i != 0) {
				formattedNumber += ",";
			}
			
			formattedNumber += arList.get(i);
		}

		return formattedNumber;
	}

	public static String convertUrl(String str) {
		
		try{
		    String url = new String(str.trim().replace(" ", "%20").replace("&", "%26")
		            .replace(",", "%2c").replace("(", "%28").replace(")", "%29")
		            .replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
		            .replace(">", "%3E").replace("#", "%23").replace("$", "%24")
		            .replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
		            .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
		            .replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
		            .replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
		            .replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
		            .replace("|", "%7C").replace("}", "%7D"));
		    
		    return url;
	    }catch(Exception e){
	        e.printStackTrace();
	    }
		return str;
	}

	/**
	 * 입력받은 문자열의 한글을 자음으로 변환해준다.
	 * 
	 * @param str : 변환할 문자열.
	 * @return : 변환된 문자열.
	 */
	public static String getInitailLetters(String str) {
		
		if(str == null || str.equals("")) {
			return null;
		}
		
		try {
			String initialString = "";
			
			int length = str.length();
			for(int i=0; i<length; i++) {
				
				char c = str.charAt(i);
				
				if(isKorean(c)) {
					initialString += getInitialLetter(str.charAt(i));
				} else {
					initialString += c;
				}
			}
			
			if(!initialString.equals("")) {
				return initialString;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static boolean isInitialLetter(char searchar){ 
		
		try {
			for(char c : INITIAL_LETTER){ 
				if(c == searchar){ 
					return true; 
				} 
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	 } 
		
	private static char getInitialLetter(char c) { 
	
		try {
			if(!isInitialLetter(c) && isKorean(c)) {
				int hanBegin = (c - KOREAN_BEGIN_UNICODE); 
				int index = hanBegin / KOREAN_BASE_UNIT; 
					
				return INITIAL_LETTER[index];
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		 
		return c;
	} 
		 
	 /**
	  * 해당 문자가 한글인지 검사
	  * @param c 문자 하나
	  * @return
	  */
	private static boolean isKorean(char c) {
		
		return KOREAN_BEGIN_UNICODE <= c && c <= KOREAN_LAST_UNICODE; 
	}

	public static boolean isEqualSinceFirst(String target, String key) {
		 
		if(target == null || key == null || target.length() < key.length()) {
			return false;
		}
		 
		try {
			String s1 = target.toUpperCase();
			String s2 = key.toUpperCase();
			 
			int length = s2.length();
			for(int i=0; i<length; i++) {

				char c1 = s1.charAt(i);
				char c2 = s2.charAt(i);
				 
				if(c1 != c2) {
					break;
				}
				 
				if(i == length - 1) {
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	 
	public static boolean isEqualSinceFirstWithInitialLetters(String target, String key) {

		if(target == null || key == null || target.length() < key.length()) {
			return false;
		}
		 
		try {
			String s1 = target.toUpperCase();
			String s2 = key.toUpperCase();
			 
			int length = s2.length();
			for(int i=0; i<length; i++) {

				char c1 = s1.charAt(i);
				char c2 = s2.charAt(i);
				char c3 = '1';
				char c4 = '2';
				 
				if(isInitialLetter(c1) || isInitialLetter(c2)) {
					c3 = getInitialLetter(s1.charAt(i));
					c4 = getInitialLetter(s2.charAt(i));
				}
				 
				if(c1 != c2 && c3 != c4) {
					break;
				}
				 
				if(i == length - 1) {
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static String concatList(List<String> sList, String separator)
	{
	    Iterator<String> iter = sList.iterator();
	    StringBuilder sb = new StringBuilder();

	    while (iter.hasNext())
	    {
	        sb.append(iter.next()).append( iter.hasNext() ? separator : "");
	    }
	    
	    return sb.toString();
	}	
}

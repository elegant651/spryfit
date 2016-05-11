package bpsound.spryfit.classes;

import java.util.ArrayList;


public class FragmentManager {
	private static FragmentManager fragmentManager;
	private static ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
	
	
	public static FragmentManager getInstance(){
		try{
			if(fragmentManager == null){
				fragmentManager = new FragmentManager();
			}			
			return fragmentManager;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<BaseFragment> getFragments() {
		return fragments;
	}
	
	public void addFragment(BaseFragment fragment) {	
		try {
			fragments.add(fragment);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeFragment(BaseFragment fragment) {
		try {
			fragments.remove(fragment);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearFragments() {		
		try {
			for(int i=fragments.size() - 1; i>=0; i--) {
				fragments.get(i).finish();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearFragmentsWithoutMain() {		
		try {
			for(int i=fragments.size() - 1; i>0; i--) {
				fragments.get(i).finish();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public BaseFragment getTopFragment() {		
		try {
			if(fragments.size() == 0) {
				return null;
			} else {
				return fragments.get(fragments.size() - 1);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getFragmentsSize() {	
		return fragments.size();
	}
	
	public static boolean isAppRunning() {	
		if(fragments.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}

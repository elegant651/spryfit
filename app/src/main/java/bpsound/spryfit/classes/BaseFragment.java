package bpsound.spryfit.classes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import bpsound.spryfit.MainActivity;
import bpsound.spryfit.utils.ViewUnbindHelper;


public abstract class BaseFragment extends Fragment {
	
	protected Context mContext;
	protected View mThisView;
	protected MainActivity mActivity;
	
	protected abstract void bindViews();
	protected abstract void setListener();
	protected abstract boolean onBackKeyPressed();

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mActivity = (MainActivity)mContext;
		
		this.addFragmentToManager();
	}
	
	@Override
	public void onResume(){
		super.onResume();		
	}
	
	@Override
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);
	}
	
	@Override
	public void onDetach(){
		ViewUnbindHelper.unbindReferences(getView());
		this.removeFragmentFromManager();
		super.onDetach();
	}
	
	public void finish(){
		super.onDestroyView();
		
		try{
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
			this.removeFragmentFromManager();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			ViewUnbindHelper.unbindReferences(mThisView);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void addFragmentToManager(){
		FragmentManager.getInstance().addFragment(this);
	}
	
	protected void removeFragmentFromManager(){
		FragmentManager.getInstance().removeFragment(this);
	}
}

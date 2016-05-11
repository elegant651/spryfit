package bpsound.spryfit.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Window;

public class CustomDialog extends Dialog
{
	public CustomDialog(Activity mActivity)
	{
		super(mActivity);
		
		setOwnerActivity(mActivity);
		
		init();
	}
	
	private void init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        
        setOnKeyListener(new OnKeyListener()
        {
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2)
			{
				if (arg2.getKeyCode() == KeyEvent.KEYCODE_BACK)
				{
					return true;
				}
				
				return false;
			}
        });
	}
	
	public void setLogoImage(){
		
	}	
	
	@Override
	public void show()
	{
		Activity activity = getOwnerActivity();
		
		if (!activity.isFinishing())
		{
			super.show();
		}
	}
}

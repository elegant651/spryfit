package bpsound.spryfit.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AniUtils {

	public static Animation slideUp(){
		Animation slideUp = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
				TranslateAnimation.RELATIVE_TO_SELF, 1.0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        slideUp.setDuration(600);
        return slideUp;
	}
	
	public static Animation slideDown(){
		Animation slideDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 1.0f);
        slideDown.setDuration(600);
        return slideDown;
	}
}

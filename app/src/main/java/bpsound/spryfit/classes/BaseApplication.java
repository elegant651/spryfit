package bpsound.spryfit.classes;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import bpsound.spryfit.R;
import bpsound.spryfit.utils.AppInfoUtils;
import bpsound.spryfit.utils.ImageUploader;
import bpsound.spryfit.utils.LruBitmapCache;
import bpsound.spryfit.utils.SharedPrefsUtils;
import bpsound.spryfit.utils.ToastUtils;


/**
 * Created by elegantuniv on 16. 3. 7..
 */
public class BaseApplication extends Application{
    public static final String TAG = BaseApplication.class.getSimpleName();

    private Tracker mTracker;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

//        Timber.plant(new Timber.DebugTree());
        ToastUtils.setContext(this);
        SharedPrefsUtils.setContext(this);
        AppInfoUtils.setAllInfos(this);
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
        ImageUploader.imageLoaderInit(this);
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}

package bpsound.spryfit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import bpsound.spryfit.classes.BaseApplication;
import bpsound.spryfit.classes.BaseFragment;
import bpsound.spryfit.data.Constants;
import bpsound.spryfit.gcm.RegistrationIntentService;
import bpsound.spryfit.utils.BitmapUtils;
import bpsound.spryfit.utils.ToastUtils;
import bpsound.spryfit.views.LeftDrawerView;

public class MainActivity extends FragmentActivity {
    public final static int TAB_MAIN = 0;
    public final static int TAB_MENU2 = 1;
    public final static int TAB_MENU3 = 2;
    public final static int TAB_MENU4 = 3;

    private static final int SELECT_PICTURE = 1;
    private static final int KITKAT_SELECT_PICTURE = 2;

    private Context mContext;
//    private MainFeedPage mCurrentMainPage;

    private DrawerLayout mDrawerLayout;
    private LeftDrawerView mDrawerView;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Firebase mFirebaseRef;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseApplication base = (BaseApplication) getApplication();
        mTracker = base.getDefaultTracker();

        mContext = this;

        mTitle = mDrawerTitle = "";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setBackgroundColor(Color.parseColor("#ffa633"));
        mDrawerView = (LeftDrawerView)findViewById(R.id.left_drawer);
        mDrawerView.setDrawerListener(new LeftDrawerView.OnDrawerItemListener() {
            @Override
            public void onSelectItem(int position) {
                selectItem(position);
            }
        });

        final ViewGroup actionBarLayout = (ViewGroup)getLayoutInflater().inflate(R.layout.custom_action_bar, null);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Spryfit");
        actionBar.setCustomView(actionBarLayout);

        bindActionBarListener(actionBarLayout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }


            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        getInstanceIdToken();

        if (savedInstanceState == null) {
            selectItem(TAB_MAIN);
        }
    }

    private void bindActionBarListener(ViewGroup vg){
        ImageView btnDrawer = (ImageView)vg.findViewById(R.id.action_bar_drawer);
        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerView);
                if(drawerOpen){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else{
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        ImageView ivSearch = (ImageView)vg.findViewById(R.id.action_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///
            }
        });


    }

    private void selectItem(int position){
        switch(position){
            case TAB_MAIN:
//                MainFeedPage fragment = MainFeedPage.newInstance(MainFeedPage.TYPE_COLLECTIONS, "");
//                replacePage(fragment, null);
//                this.mCurrentMainPage = fragment;

                mDrawerLayout.closeDrawer(mDrawerView);
                break;
            case TAB_MENU2:


                mDrawerLayout.closeDrawer(mDrawerView);
                break;
            case TAB_MENU3:


                mDrawerLayout.closeDrawer(mDrawerView);
                break;
            case TAB_MENU4:

                mDrawerLayout.closeDrawer(mDrawerView);
                break;
            default:
                mDrawerView.selectTab(position);
        }
    }

//    public void moveProfilePage(String userId) {
//        ProfilePage fragment2 = ProfilePage.newInstance(userId);
//        replacePage(fragment2, null);
//    }

    public void hideActionBar(){
        getActionBar().hide();
    }

    private void replacePage(BaseFragment fragment, Bundle bundle){
        try{
            if(bundle != null){
                fragment.setArguments(bundle);
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.content_frame, fragment);
            ft.commitAllowingStateLoss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addPage(BaseFragment fragment, Bundle bundle){
        try{
            if(bundle != null){
                fragment.setArguments(bundle);
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.add(R.id.content_frame, fragment);
            ft.commitAllowingStateLoss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void hidePage(BaseFragment fragment){
        try{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(0, R.anim.slide_out_top);
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

//    public void getImageFromGallery(NewSchedulePage page){
//        this.mSchedulePage = page;
//
//        if(AppInfoUtils.shouldAskPermission()){
//            String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
//
//            int permsRequestCode = 200;
//
//            requestPermissionAboveMashmellow(perms, permsRequestCode);
//        } else {
//            proceedGetImageFromGallery();
//        }
//    }

    @TargetApi(23)
    private void requestPermissionAboveMashmellow(String[] perms, int permsRequestCode){
        requestPermissions(perms, permsRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);

        switch(permsRequestCode){

            case 200:
                boolean readAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(readAccepted){
                    proceedGetImageFromGallery();
                }
                break;
        }
    }

    private void proceedGetImageFromGallery() {
        if(Build.VERSION.SDK_INT<19){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "사진 선택"), SELECT_PICTURE);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, SELECT_PICTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = null;
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
            }

            String selectedImagePath = BitmapUtils.getMediaPath(this, selectedImageUri);
            if (selectedImagePath == null) {
                ToastUtils.setContext(this);
                ToastUtils.showToast("다른 이미지를 선택해주세요~!!");
            } else {
//                this.mSchedulePage.setImageAtImageView(selectedImagePath);
            }
        }
    }

    private void getInstanceIdToken(){
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                Log.i(Constants.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerView);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName("Main Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}

package bpsound.spryfit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import bpsound.spryfit.MainActivity;
import bpsound.spryfit.R;
import bpsound.spryfit.adapters.DrawerItemCustomAdapter;
import bpsound.spryfit.items.ObjectDrawerItem;


/**
 * Created by kyunghopark on 15. 4. 27..
 */
public class LeftDrawerView extends FrameLayout {

    private Context mContext;
    private MainActivity mActivity;
    private View mThisView;
    private OnDrawerItemListener mListener;

    private ViewSwitcher mVsLeftDrawer;
    private ListView mLvDrawer;
    private DrawerItemCustomAdapter mDrawerAdapter;
    private ObjectDrawerItem[] mDrawerItem;
    private String[] mMenuTitles;

    private LinearLayout mLlBackDrawer;
    private TextView mTvNavTitle;

    public LeftDrawerView(Context context){
        this(context, null, 0);
    }

    public LeftDrawerView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public LeftDrawerView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        mContext = context;
        mActivity = (MainActivity)context;
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mThisView = inflater.inflate(R.layout.layout_view_left_drawer, null);
        this.addView(mThisView);

        bindViews();
        setAdapters();
        setListener();
    }

    private void bindViews(){
        mVsLeftDrawer = (ViewSwitcher)mThisView.findViewById(R.id.vsLeftDrawer);
        mLvDrawer = (ListView)mThisView.findViewById(R.id.lvDrawer);
        mLlBackDrawer = (LinearLayout)mThisView.findViewById(R.id.llBackDrawer);
        mTvNavTitle = (TextView)mThisView.findViewById(R.id.tvNavTitle);
    }

    private void setAdapters(){
        mMenuTitles = getResources().getStringArray(R.array.menu_array);

        int menuNum = mMenuTitles.length;
        int[] drawer_icons = {R.drawable.anq_back_button, R.drawable.anq_back_button, R.drawable.anq_back_button, R.drawable.anq_back_button};
        mDrawerItem = new ObjectDrawerItem[menuNum];
        for(int i=0; i<menuNum; i++){
            mDrawerItem[i] = new ObjectDrawerItem(drawer_icons[i], mMenuTitles[i]);
        }
        mDrawerAdapter = new DrawerItemCustomAdapter(mContext, R.layout.layout_row_navdrawer, mDrawerItem);
        mLvDrawer.setAdapter(mDrawerAdapter);
        mLvDrawer.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void setListener(){
        mLlBackDrawer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mVsLeftDrawer.showPrevious();
            }
        });
    }

    public void selectTab(int position){
        mVsLeftDrawer.showNext();
        mTvNavTitle.setText(mMenuTitles[position]);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mListener!=null){
                mListener.onSelectItem(position);
            }
        }
    }

    public void setDrawerListener(OnDrawerItemListener listener){
        mListener = listener;
    }

    public interface OnDrawerItemListener{
        public void onSelectItem(int position);
    }


}

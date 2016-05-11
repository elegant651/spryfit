package bpsound.spryfit.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bpsound.spryfit.R;
import bpsound.spryfit.items.ObjectDrawerItem;


/**
 * Created by kyunghopark on 15. 4. 27..
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {
    private Context mContext;
    private int layoutResourceId;
    private ObjectDrawerItem data[] = null;
    private ArrayList<ImageView> alIvNewIcon = new ArrayList<ImageView>();

    private TextView mTvNewMsg;

    public DrawerItemCustomAdapter(Context context, int layoutResourceId, ObjectDrawerItem[] data){
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.ivIcon);
        TextView tvTitle = (TextView)listItem.findViewById(R.id.tvTitle);
        ImageView ivNew = (ImageView)listItem.findViewById(R.id.ivNew);
        alIvNewIcon.add(position, ivNew);

        ObjectDrawerItem folder = data[position];
        imageViewIcon.setImageResource(folder.icon);
        tvTitle.setText(folder.getTitle());

        return listItem;
    }

    public void visibleTabNewIcon(int index, boolean visible){
        if(index >= alIvNewIcon.size()){
            return;
        }

        ImageView ivNew = alIvNewIcon.get(index);
        if(visible){
            ivNew.setVisibility(View.VISIBLE);
        }else{
            ivNew.setVisibility(View.GONE);
        }
    }

    public void updateData(ObjectDrawerItem object, int position){
        this.insert(object, position);
        this.notifyDataSetChanged();
    }
}
